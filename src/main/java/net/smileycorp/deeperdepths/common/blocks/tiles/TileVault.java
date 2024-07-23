package net.smileycorp.deeperdepths.common.blocks.tiles;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.BlockTrial;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumVaultState;
import net.smileycorp.deeperdepths.common.items.DeeperDepthsItems;
import net.smileycorp.deeperdepths.common.items.ItemTrialKey;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileVault extends TileEntity implements ITickable, ILootContainer {
    
    private EnumVaultState state = EnumVaultState.INACTIVE;
    private ResourceLocation loot_table_loc;
    private long loot_table_seed;
    private LootTable loot_table = null;
    private ItemStack displayed_item = ItemStack.EMPTY;
    private List<ItemStack> stored_items = Lists.newArrayList();
    private List<UUID> rewarded_players = Lists.newArrayList();
    
    public TileVault() {}
    
    public TileVault(boolean ominous) {
        loot_table_loc = Constants.loc(ominous ? "ominous_vault" : "vault");
    }
    
    @Override
    public void update() {
        if (world == null) return;
        if (world.isRemote) return;
        if (world.getWorldTime() % 20 != 0) return;
        if (state == EnumVaultState.ACTIVE) {
            if (world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    4.5, false) == null)
                setState(EnumVaultState.INACTIVE);
            else {
                displayed_item = getRandomDisplayItem();
                markDirty();
            }
        } else if (state == EnumVaultState.INACTIVE &&
                world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        4, this::canReward) != null)
            setState(EnumVaultState.ACTIVE);
        if (state == EnumVaultState.UNLOCKING) setState(EnumVaultState.EJECTING);
        if (state == EnumVaultState.EJECTING) {
            if (stored_items.isEmpty()) setState(EnumVaultState.INACTIVE);
            else {
                BehaviorDefaultDispenseItem.doDispense(world, stored_items.get(0), 3, EnumFacing.UP, new BlockSourceImpl(world, pos));
                stored_items.remove(0);
            }
        }
    }
    
    public void interact(EntityPlayer player, ItemStack stack) {
        if (!canReward(player) |! isKey(stack)) return;
        if (loot_table == null) {
            if (loot_table_loc == null) return;
            loot_table = world.getLootTableManager().getLootTableFromLocation(loot_table_loc);
        }
        stored_items = loot_table.generateLootForPools(loot_table_seed == 0 ? new Random() : new Random(loot_table_seed),
                new LootContext.Builder((WorldServer) world).withPlayer(player).withLuck(player.getLuck()).build());
        if (stored_items.isEmpty()) return;
        stack.shrink(1);
        rewarded_players.add(player.getUniqueID());
        setState(EnumVaultState.UNLOCKING);
    }
    
    private boolean canReward(Entity entity) {
        return !((EntityPlayer)entity).isSpectator() &! rewarded_players.contains(entity.getUniqueID());
    }
    
    private ItemStack getRandomDisplayItem() {
        if (loot_table == null) {
            if (loot_table_loc == null) return ItemStack.EMPTY;
            loot_table = world.getLootTableManager().getLootTableFromLocation(loot_table_loc);
        }
        List<ItemStack> loot = loot_table.generateLootForPools(loot_table_seed == 0 ? new Random() : new Random(loot_table_seed),
                new LootContext.Builder((WorldServer) world).build());
        return loot.isEmpty() ? ItemStack.EMPTY : loot.get(world.rand.nextInt(loot.size()));
    }
    
    @Override
    public ResourceLocation getLootTable() {
        return loot_table_loc;
    }
    
    public boolean isKey(ItemStack stack) {
        return stack.getItem() == DeeperDepthsItems.TRIAL_KEY && ItemTrialKey.isOminous(stack) == isOminous();
    }
    
    private boolean isOminous() {
        return world.getBlockState(pos).getValue(BlockTrial.OMINOUS);
    }
    
    public void setLootTable(ResourceLocation loc) {
        loot_table_loc = loc;
        if (hasWorld()) loot_table = world.getLootTableManager().getLootTableFromLocation(loc);
    }
    
    public EnumVaultState getState() {
        return state;
    }
    
    public void setState(EnumVaultState state) {
        this.state = state;
        if (state == EnumVaultState.ACTIVE) displayed_item = getRandomDisplayItem();
        else displayed_item = ItemStack.EMPTY;
        markDirty();
    }
    
    public ItemStack getDisplayedItem() {
        return displayed_item;
    }
    
    @Override
    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
        super.markDirty();
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("state")) state = EnumVaultState.values()[nbt.getInteger("state")];
        if (nbt.hasKey("LootTable")) {
            loot_table_loc = new ResourceLocation(nbt.getString("LootTable"));
            if (nbt.hasKey("LootTableSeed")) loot_table_seed = nbt.getLong("LootTableSeed");
        }
        if (nbt.hasKey("stored_items")) {
            stored_items.clear();
            for (NBTBase tag : nbt.getTagList("stored_items", 10)) stored_items.add(new ItemStack((NBTTagCompound)tag));
        }
        if (nbt.hasKey("rewarded_players")) {
            rewarded_players.clear();
            for (NBTBase tag : nbt.getTagList("stored_items", 10)) rewarded_players.add(NBTUtil.getUUIDFromTag((NBTTagCompound) tag));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("state", state.ordinal());
        if (loot_table_loc != null) nbt.setString("LootTable", loot_table_loc.toString());
        if (!stored_items.isEmpty()) {
            NBTTagList items = new NBTTagList();
            for (ItemStack stack : stored_items) items.appendTag(stack.serializeNBT());
        }
        if (!rewarded_players.isEmpty()) {
            NBTTagList players = new NBTTagList();
            for (UUID uuid : rewarded_players) players.appendTag(NBTUtil.createUUIDTag(uuid));
        }
        return nbt;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("state", state.ordinal());
        nbt.setTag("displayed_item", displayed_item.serializeNBT());
        return nbt;
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state")) {
            int i = nbt.getInteger("state");
            state = EnumVaultState.values()[i];
        }
        if (nbt.hasKey("displayed_item")) displayed_item = new ItemStack(nbt.getCompoundTag("displayed_item"));
    }
    
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
}
