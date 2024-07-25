package net.smileycorp.deeperdepths.common.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;

import javax.annotation.Nullable;
import java.util.Iterator;

public class EntityBogged extends AbstractSkeleton
{
    private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntityBogged.class, DataSerializers.BOOLEAN);
    /** How many mushrooms are dropped when sheared. */
    int mushroomsDropped = 2;
    /** How long the poison from arrows last, in seconds. */
    int poisonArrowDuration = 4;
    /** The cooldown between firing on Easy+Normal, in seconds. */
    double attackCooldown = 3.5;
    /** The cooldown between firing on Hard, in seconds. */
    double attackCooldownHard = 2.5;
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
                for (int i = 0; i < mushroomsDropped; i++)
                {
                    Block mushroom = Blocks.BROWN_MUSHROOM;
                    if (this.world.rand.nextBoolean()) mushroom = Blocks.RED_MUSHROOM;

                    this.entityDropItem(new ItemStack(mushroom, 1), 1.0F);
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
                int i = (int) (attackCooldownHard * 20);
                if (this.world.getDifficulty() != EnumDifficulty.HARD)
                {
                    i = (int) (attackCooldown * 20);
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(4, this.aiArrowAttack);
            }
        }
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return Constants.BOGGED_DROPS;
    }

    protected SoundEvent getAmbientSound() {
        return DeeperDepthsSoundEvents.BOGGED_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return DeeperDepthsSoundEvents.BOGGED_HURT;
    }

    protected SoundEvent getDeathSound() {
        return DeeperDepthsSoundEvents.BOGGED_DEATH;
    }

    protected SoundEvent getStepSound() {
        return DeeperDepthsSoundEvents.BOGGED_STEP;
    }

    protected EntityArrow getArrow(float p_190726_1_)
    {
        EntityArrow entityarrow = super.getArrow(p_190726_1_);
        if (entityarrow instanceof EntityTippedArrow)
        {
            ((EntityTippedArrow)entityarrow).addEffect(new PotionEffect(MobEffects.POISON, poisonArrowDuration * 20));
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
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("sheared"));
    }
}