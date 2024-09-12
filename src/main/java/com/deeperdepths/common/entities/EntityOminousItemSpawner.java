package com.deeperdepths.common.entities;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityOminousItemSpawner extends Entity {
    
    private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityOminousItemSpawner.class, DataSerializers.ITEM_STACK);
    private long spawn_time;
    
    public EntityOminousItemSpawner(World world) {
        super(world);
        setSize(0.25f, 0.25f);
    }
    
    public EntityOminousItemSpawner(World world, BlockPos pos, ItemStack stack) {
        this(world);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        setItem(stack);
        spawn_time = 60 + rand.nextInt(60);
    }
    
    public void setItem(ItemStack stack) {
        dataManager.set(STACK, stack);
    }
    
    public ItemStack getItem() {
        return dataManager.get(STACK);
    }
    
    @Override
    protected void entityInit() {
        dataManager.register(STACK, ItemStack.EMPTY);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            return;
        }
        if (ticksExisted == spawn_time - 26) playSound(DeeperDepthsSoundEvents.TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, 1, 1);
        if (ticksExisted >= spawn_time) {
            ItemStack stack = getItem();
            BlockPos pos = getPosition();
            IBehaviorDispenseItem behaviour = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
            behaviour.dispense(new BlockSourceImpl(world, pos), stack);
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    DeeperDepthsSoundEvents.TRIAL_SPAWNER_SPAWN_ITEM, SoundCategory.BLOCKS, 1, 1);
            setDead();
        }
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("spawn_item_after_ticks")) spawn_time = nbt.getLong("spawn_item_after_ticks");
        if (nbt.hasKey("item")) dataManager.set(STACK, new ItemStack(nbt.getCompoundTag("item")));
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setLong("spawn_item_after_ticks", spawn_time);
        if (nbt.hasKey("item")) dataManager.set(STACK, new ItemStack(nbt.getCompoundTag("item").copy()));
    }
    
}
