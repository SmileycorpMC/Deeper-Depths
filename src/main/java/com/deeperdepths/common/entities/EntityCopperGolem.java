package com.deeperdepths.common.entities;

import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.entities.ai.EntityAICopperGolemItemSorting;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.util.control.Exception;

import java.util.HashSet;
import java.util.Set;

public class EntityCopperGolem extends EntityGolem
{

    public static final DataParameter<Byte> WEATHER_STAGE = EntityDataManager.createKey(EntityCopperGolem.class, DataSerializers.BYTE);

    /** Chests already checked during this run */
    public Set<BlockPos> memoryDepositContainers = new HashSet<>();
    private boolean waxed = false;

    public EntityCopperGolem(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.setSize(0.49F, 0.98F);
        dataManager.register(WEATHER_STAGE, (byte) 0);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAICopperGolemItemSorting(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }


    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        /* A vanilla feature the AI CANNOT handle RN. */
        if (!world.isRemote && !this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && false)
        {
            this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND).copy(), 1.0F);
            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }

        return super.processInteract(player, hand);
    }

    public void resetTaskMemory()
    {
        memoryDepositContainers.clear();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("weather_stage", (byte) getWeatherStage().ordinal());
        compound.setBoolean("waxed", isWaxed());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("weather_stage")) setWeatherStage(EnumWeatherStage.values()[compound.getByte("weather_stage") % 4]);
        if (compound.hasKey("waxed")) setWaxed(compound.getBoolean("waxed"));
    }

    public void setWeatherStage(EnumWeatherStage stage) {
        dataManager.set(WEATHER_STAGE, (byte) stage.ordinal());
    }

    public EnumWeatherStage getWeatherStage() {
        return EnumWeatherStage.values()[dataManager.get(WEATHER_STAGE) % 4];
    }

    public boolean isWaxed() {
        return waxed;
    }

    public void setWaxed(boolean waxed) {
        this.waxed = waxed;
    }

}