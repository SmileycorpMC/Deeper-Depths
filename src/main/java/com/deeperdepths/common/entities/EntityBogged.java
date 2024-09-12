package com.deeperdepths.common.entities;

import com.deeperdepths.common.DeeperDepthsLootTables;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.config.EntityConfig;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.Iterator;

public class EntityBogged extends AbstractSkeleton
{
    private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntityBogged.class, DataSerializers.BOOLEAN);
    /** A seperate shearing loot table that can be defined via NBT. Otherwise the default is used. */
    private ResourceLocation shearLootTable;
    private long shearLootTableSeed;
    private final EntityAIAttackRangedBow<AbstractSkeleton> aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2, false)
    {
        public void resetTask()
        {
            super.resetTask();
            EntityBogged.this.setSwingingArms(false);
        }

        public void startExecuting()
        {
            super.startExecuting();
            EntityBogged.this.setSwingingArms(true);
        }
    };

    public EntityBogged(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(SHEARED, false);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        EntityConfig.bogged.applyAttributes(this);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() instanceof ItemShears &&  !this.getSheared())
        {
            this.setSheared(true);
            player.swingArm(hand);
            this.world.playSound(player, this.posX, this.posY, this.posZ, DeeperDepthsSoundEvents.BOGGED_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);

            if (!this.world.isRemote)
            {
                ResourceLocation lootTableLocation = this.shearLootTable;
                if (lootTableLocation == null) lootTableLocation = this.getShearLootTable();

                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
                LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(lootTableLocation);

                for (ItemStack itemSheared : loottable.generateLootForPools(this.getRNG(), lootcontext$builder.build()))
                {
                    this.entityDropItem(itemSheared, 1.0F);
                }
            }
            return true;
        }

        return super.processInteract(player, hand);
    }

    /** Overrides Combat Task, so we may customize the shooting speed. */
    @Override
    public void setCombatTask()
    {
        super.setCombatTask();

        /* After the Skeleton's Combat AI Tasks are set, we rip out. */
        if (this.world != null && !this.world.isRemote)
        {
            /* Currently uses an iterator to find and remove the `EntityAIAttackRangedBow`, as a simple `removeTask` wasn't working. */
            Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.tasks.taskEntries.iterator();
            while (iterator.hasNext())
            {
                EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
                EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;

                if (entityaibase instanceof EntityAIAttackRangedBow)
                { iterator.remove(); }
            }

            ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() instanceof ItemBow)
            {
                int i = 20;
                switch (world.getDifficulty()) {
                    case EASY:
                        i += EntityConfig.boggedAttackCooldownEasy;
                        break;
                    case NORMAL:
                        i += EntityConfig.boggedAttackCooldownNormal;
                        break;
                    case HARD:
                        i += EntityConfig.boggedAttackCooldownHard;
                        break;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(4, this.aiArrowAttack);
            }
        }
    }

    @Nullable
    protected ResourceLocation getShearLootTable()
    { return DeeperDepthsLootTables.BOGGED_SHEAR; }

    @Nullable
    protected ResourceLocation getLootTable()
    { return DeeperDepthsLootTables.BOGGED_DROPS; }

    protected SoundEvent getAmbientSound()
    { return DeeperDepthsSoundEvents.BOGGED_AMBIENT; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return DeeperDepthsSoundEvents.BOGGED_HURT; }

    protected SoundEvent getDeathSound()
    { return DeeperDepthsSoundEvents.BOGGED_DEATH; }

    protected SoundEvent getStepSound()
    { return DeeperDepthsSoundEvents.BOGGED_STEP; }

    protected EntityArrow getArrow(float p_190726_1_)
    {
        EntityArrow entityarrow = super.getArrow(p_190726_1_);
        if (entityarrow instanceof EntityTippedArrow)
        {
            ((EntityTippedArrow)entityarrow).addEffect(new PotionEffect(MobEffects.POISON, EntityConfig.poisonArrowDuration * 20));
        }
        return entityarrow;
    }

    public boolean getSheared()
    { return ((Boolean)this.dataManager.get(SHEARED)).booleanValue(); }

    public void setSheared(boolean sheared)
    { this.dataManager.set(SHEARED, Boolean.valueOf(sheared)); }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("sheared", this.getSheared());

        if (this.shearLootTable != null)
        {
            compound.setString("ShearLootTable", this.shearLootTable.toString());
            if (this.shearLootTableSeed != 0L) compound.setLong("ShearLootTableSeed", this.shearLootTableSeed);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("sheared"));
        if (compound.hasKey("ShearLootTable", 8))
        {
            this.shearLootTable = new ResourceLocation(compound.getString("ShearLootTable"));
            this.shearLootTableSeed = compound.getLong("ShearLootTableSeed");
        }
    }
}