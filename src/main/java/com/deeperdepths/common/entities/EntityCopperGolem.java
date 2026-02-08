package com.deeperdepths.common.entities;

import com.deeperdepths.common.entities.ai.EntityAICopperGolemItemSorting;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class EntityCopperGolem extends EntityGolem
{
    /** Chests already checked during this run */
    public Set<BlockPos> memoryDepositContainers = new HashSet<>();

    public EntityCopperGolem(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.setSize(0.49F, 0.98F);
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

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

    }
}