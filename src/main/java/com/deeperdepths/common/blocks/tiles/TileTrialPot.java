package com.deeperdepths.common.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.smileycorp.atlas.api.util.RecipeUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileTrialPot extends TileEntity implements ILootContainer, IInventory {
    
    protected final IItemHandler inventory = new InvWrapper(this);
    
    private ItemStack stack = ItemStack.EMPTY;
    
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }
    
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ?
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory)
                : super.getCapability(capability, facing);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stack = compound.hasKey("items") ? new ItemStack(compound.getCompoundTag("items")) : ItemStack.EMPTY;
        if (compound.hasKey("LootTable")) {
            lootTable = new ResourceLocation(compound.getString("LootTable"));
            lootTableSeed = compound.getLong("LootTableSeed");
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!isEmpty()) {
            NBTTagCompound items = stack.serializeNBT();
            compound.setTag("items", items);
        }
        if (lootTable != null) {
            compound.setString("LootTable", lootTable.toString());
            if (lootTableSeed != 0) compound.setLong("LootTableSeed", lootTableSeed);
        }
        return compound;
    }
    
    @Override
    public ResourceLocation getLootTable() {
        return lootTable;
    }


    public void setLootTable(ResourceLocation location, Long lootTableSeed) {
        this.lootTable = location;
        this.lootTableSeed = lootTableSeed;
    }
    
    protected ItemStack getStack() {
        if (stack.isEmpty() && lootTable != null &! world.isRemote) {
            Random rand = lootTableSeed == 0 ? new Random() : new Random(lootTableSeed);
            List<ItemStack> items = world.getLootTableManager().getLootTableFromLocation(lootTable).generateLootForPools(rand,
                    new LootContext.Builder((WorldServer) world).build());
            if (!items.isEmpty()) {
                stack = items.get(rand.nextInt(items.size()));
                lootTable = null;
                lootTableSeed = 0;
            }
        }
        return stack;
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot != 0) return false;
        if (lootTable != null) return false;
        if (getStack().isEmpty()) return true;
        if (!RecipeUtils.compareItemStacks(stack, this.stack, true)) return false;
        return this.stack.getCount() < this.stack.getMaxStackSize();
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (slot != 0) return ItemStack.EMPTY;
        int amount = Math.min(count, getStack().getCount());
        ItemStack result = stack.copy();
        result.setCount(amount);
        stack.shrink(amount);
        return result;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack result = ItemStack.EMPTY;
        if (slot == 0) {
            result = getStack();
            stack = ItemStack.EMPTY;
        }
        return result;
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot == 0) {
            this.stack = stack;
            lootTable = null;
            lootTableSeed = 0;
        }
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? getStack() : ItemStack.EMPTY;
    }
    
    @Override
    public void clear() {
        stack = ItemStack.EMPTY;
        lootTable = null;
        lootTableSeed = 0;
    }
    
    @Override
    public boolean isEmpty() {
        return lootTable == null && stack.isEmpty();
    }
    
    @Override
    public int getSizeInventory() {
        return 1;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    
    @Override
    public String getName() {
        return "container.deeperdepths.Pot";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory(EntityPlayer player) {}
    
    @Override
    public void closeInventory(EntityPlayer player) {}
    
    @Override
    public int getField(int i) {
        return 0;
    }
    
    @Override
    public void setField(int i, int i1) {}
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }
    
}
