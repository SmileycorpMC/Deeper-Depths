package net.smileycorp.deeperdepths.common.blocks.tiles;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
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
import net.minecraft.util.*;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.smileycorp.atlas.api.util.RecipeUtils;
import net.smileycorp.deeperdepths.client.ClientProxy;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepthsLootTables;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.blocks.BlockTrial;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumVaultState;
import net.smileycorp.deeperdepths.common.items.DeeperDepthsItems;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileVault extends TileEntity implements ITickable, ILootContainer {
    
    private EnumVaultState state = EnumVaultState.INACTIVE;
    private Config config = new Config();
    private ItemStack displayed_item = ItemStack.EMPTY;
    private List<ItemStack> stored_items = Lists.newArrayList();
    private List<UUID> rewarded_players = Lists.newArrayList();
    private int ejected_items = 0;
    
    public TileVault() {}
    
    public TileVault(boolean ominous) {
        config.loot_table = Constants.loc(ominous ? "ominous_vault" : "vault");
        config.key = new ItemStack(DeeperDepthsItems.TRIAL_KEY, 1, ominous ? 1 : 0);
    }
    
    @Override
    public void update() {
        if (world == null) return;
        if (world.isRemote) {
            if (world.rand.nextFloat() < 0.5f) {
                spawnParticle(EnumParticleTypes.SMOKE_NORMAL, Color.WHITE);
                if (state != EnumVaultState.INACTIVE) spawnParticle(EnumParticleTypes.FLAME, isOminous()
                        ? Color.WHITE : new Color(0x65E7F0));
            }
            if (state != EnumVaultState.INACTIVE && world.rand.nextFloat() <= 0.02f)
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        DeeperDepthsSoundEvents.VAULT_AMBIENT, SoundCategory.BLOCKS, world.rand.nextFloat() * 0.25f + 0.75f,
                        world.rand.nextFloat() + 0.5f, false);
            return;
        }
        if (world.getWorldTime() % 20 != 0) return;
        if (state == EnumVaultState.INACTIVE) {
            if (world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    config.activation_range, this::canReward) != null) {
                setState(EnumVaultState.ACTIVE);
                playSound(DeeperDepthsSoundEvents.VAULT_ACTIVATE, 1f);
            }
        } else if (state == EnumVaultState.ACTIVE) {
            if (world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    config.deactivation_range, false) == null) {
                setState(EnumVaultState.INACTIVE);
                playSound(DeeperDepthsSoundEvents.VAULT_DEACTIVATE, 1f);
            } else {
                displayed_item = getRandomDisplayItem();
                markDirty();
            }
        } else if (state == EnumVaultState.UNLOCKING) {
            setState(EnumVaultState.EJECTING);
            playSound(DeeperDepthsSoundEvents.VAULT_OPEN_SHUTTER, 1f);
        } else if (state == EnumVaultState.EJECTING) {
            if (stored_items.isEmpty()) {
                setState(EnumVaultState.INACTIVE);
                ejected_items = 0;
                playSound( DeeperDepthsSoundEvents.VAULT_CLOSE_SHUTTER, 1f);
            }
            else {
                BehaviorDefaultDispenseItem.doDispense(world, stored_items.get(0),2, EnumFacing.UP,
                        new PositionImpl(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5));
                playSound(DeeperDepthsSoundEvents.VAULT_EJECT_ITEM, 0.8f + ejected_items * 0.4f);
                ejected_items++;
                stored_items.remove(0);
            }
        }
    }
    
    public void interact(EntityPlayer player, ItemStack stack) {
        if (!isKey(stack)) {
            playSound(DeeperDepthsSoundEvents.VAULT_INSERT_ITEM_FAIL, 1f);
            return;
        }
        if (!canReward(player)) {
            playSound(DeeperDepthsSoundEvents.VAULT_REJECT_REWARDED_PLAYER, 1f);
            return;
        }
        if (config.loot_table == null) return;
        stored_items = world.getLootTableManager().getLootTableFromLocation(config.loot_table)
                .generateLootForPools(config.loot_table_seed == 0 ? new Random() : new Random(config.loot_table_seed),
                new LootContext.Builder((WorldServer) world).withPlayer(player).withLuck(player.getLuck()).build());
        if (stored_items.isEmpty()) {
            playSound(DeeperDepthsSoundEvents.VAULT_INSERT_ITEM_FAIL, 1f);
            return;
        }
        playSound(DeeperDepthsSoundEvents.VAULT_INSERT_ITEM, 1f);
        stack.shrink(1);
        rewarded_players.add(player.getUniqueID());
        setState(EnumVaultState.UNLOCKING);
    }
    
    private void playSound(SoundEvent event, float pitch) {
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                event, SoundCategory.BLOCKS, 1, pitch);
    }
    
    private void spawnParticle(EnumParticleTypes particle, Color color) {
        ClientProxy.addParticle(particle, pos.getX() + world.rand.nextFloat() * 0.8 + 0.1,
                pos.getY() + world.rand.nextFloat() * 0.5 + 0.25, pos.getZ() + world.rand.nextFloat() * 0.8 + 0.1, color);
    }
    
    private boolean canReward(Entity entity) {
        return !((EntityPlayer)entity).isSpectator() &! rewarded_players.contains(entity.getUniqueID());
    }
    
    private ItemStack getRandomDisplayItem() {
        if (config.loot_table == null) return ItemStack.EMPTY;
        List<ItemStack> loot = world.getLootTableManager().getLootTableFromLocation(config.loot_table)
                .generateLootForPools(config.loot_table_seed == 0 ? new Random() : new Random(config.loot_table_seed),
                new LootContext.Builder((WorldServer) world).build());
        return loot.isEmpty() ? ItemStack.EMPTY : loot.get(world.rand.nextInt(loot.size()));
    }
    
    public Config getConfig() {
        return config;
    }
    
    @Override
    public ResourceLocation getLootTable() {
        return config.loot_table;
    }
    
    public boolean isKey(ItemStack stack) {
        return RecipeUtils.compareItemStacks(stack, config.key, config.key.hasTagCompound());
    }
    
    public boolean isOminous() {
        return world.getBlockState(pos).getValue(BlockTrial.OMINOUS);
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
        if (nbt.hasKey("config")) config.readFromNBT(nbt.getCompoundTag("config"));
        if (nbt.hasKey("stored_items")) {
            stored_items.clear();
            for (NBTBase tag : nbt.getTagList("stored_items", 10)) stored_items.add(new ItemStack((NBTTagCompound)tag));
        }
        if (nbt.hasKey("rewarded_players")) {
            rewarded_players.clear();
            for (NBTBase tag : nbt.getTagList("stored_items", 10)) rewarded_players.add(NBTUtil.getUUIDFromTag((NBTTagCompound) tag));
        }
        if (nbt.hasKey("ejected_items")) ejected_items = nbt.getInteger("ejected_items");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("state", state.ordinal());
        if (config != null) nbt.setTag("config", config.writeToNBT());
        if (!stored_items.isEmpty()) {
            NBTTagList items = new NBTTagList();
            for (ItemStack stack : stored_items) items.appendTag(stack.serializeNBT());
        }
        if (!rewarded_players.isEmpty()) {
            NBTTagList players = new NBTTagList();
            for (UUID uuid : rewarded_players) players.appendTag(NBTUtil.createUUIDTag(uuid));
        }
        nbt.setInteger("ejected_items", ejected_items);
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
    
    public static class Config {
        
        private double activation_range = 4, deactivation_range = 4.5;
        private ItemStack key = new ItemStack(DeeperDepthsItems.TRIAL_KEY);
        private ResourceLocation loot_table = DeeperDepthsLootTables.TRIAL_VAULT_LOOT;
        private long loot_table_seed = 0;
        
        public double getActivationRange() {
            return activation_range;
        }
        
        public Config setActivationRange(double activation_range) {
            this.activation_range = activation_range;
            return this;
        }
        
        public double getDeactivationRange() {
            return deactivation_range;
        }
        
        public Config setDeactivationRange(double deactivation_range) {
            this.deactivation_range = deactivation_range;
            return this;
        }
        
        public ItemStack getKey() {
            return key;
        }
        
        public Config setKey(ItemStack key) {
            this.key = key;
            return this;
        }
        
        public ResourceLocation getLootTable() {
            return loot_table;
        }
        
        public Config setLootTable(ResourceLocation loot_table) {
            this.loot_table = loot_table;
            return this;
        }
        
        public long getLootTableSeed() {
            return loot_table_seed;
        }
        
        public Config setLootTableSeed(long loot_table_seed) {
            this.loot_table_seed = loot_table_seed;
            return this;
        }
        
        public void readFromNBT(NBTTagCompound nbt) {
            activation_range = nbt.getDouble("activation_range");
            deactivation_range = nbt.getDouble("deactivation_range");
            key = new ItemStack(nbt.getCompoundTag("key"));
            loot_table = new ResourceLocation(nbt.getString("loot_table"));
            loot_table_seed = nbt.getLong("loot_table_seed");
        }
        
        public NBTTagCompound writeToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setDouble("activation_range", activation_range);
            nbt.setDouble("deactivation_range", deactivation_range);
            nbt.setTag("key", key.writeToNBT(new NBTTagCompound()));
            nbt.setString("loot_table", loot_table.toString());
            nbt.setLong("loot_table_seed", loot_table_seed);
            return nbt;
        }
        
    }
    
}
