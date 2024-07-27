package net.smileycorp.deeperdepths.common.entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.animation.EZAnimation;
import net.smileycorp.deeperdepths.animation.EZAnimationHandler;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;

public class EntityBreeze extends EntityMob implements IAnimatedEntity {
    //the number in the animation only states the duration of the animation
    public static final EZAnimation ANIMATION_SHOOT = EZAnimation.create(30);
    public static final DataParameter<Boolean> SLIDE = EntityDataManager.createKey(EntityBreeze.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> JUMP = EntityDataManager.createKey(EntityBreeze.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SHOOT_ATTACK = EntityDataManager.createKey(EntityBreeze.class, DataSerializers.BOOLEAN);

    /** use DataParamater booleans for animations for best results */
    public boolean isShootAttack() {return this.dataManager.get(SHOOT_ATTACK);}
    public void setShootAttack(boolean value) {this.dataManager.set(SHOOT_ATTACK, Boolean.valueOf(value));}
    public boolean isJumpingAnim() {return this.dataManager.get(JUMP);}
    public void setJumpingAnim(boolean value) {this.dataManager.set(JUMP, Boolean.valueOf(value));}
    public boolean isSlidingAnim() {return this.dataManager.get(SLIDE);}
    public void setSlidingAnim(boolean value) {this.dataManager.set(SLIDE, Boolean.valueOf(value));}

    public EntityBreeze(World worldIn) {
        super(worldIn);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHOOT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(JUMP, Boolean.valueOf(false));
        this.dataManager.register(SLIDE, Boolean.valueOf(false));
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new AIWindChargeAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0);
    }

    protected SoundEvent getAmbientSound()
    { return this.onGround ? DeeperDepthsSoundEvents.BREEZE_IDLE_GROUND : DeeperDepthsSoundEvents.BREEZE_IDLE_AIR; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return DeeperDepthsSoundEvents.BREEZE_HURT; }

    protected SoundEvent getDeathSound() {
        return DeeperDepthsSoundEvents.BREEZE_DEATH;
    }

    //used for animation system
    private int animationTick;
    //just a variable that holds what the current animation is
    private EZAnimation currentAnimation;



    int exampleTimerOnly = 40;

    @Override
    public void onUpdate() {
        super.onUpdate();
        IBlockState iblockstate = this.world.getBlockState(this.getPosition().down());

        if (this.world.isRemote)
        {
            if (iblockstate.getMaterial() != Material.AIR)
            {
                for(int i = 0; i < 1; ++i)
                {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Block.getStateId(iblockstate));
                }
            }
        }

        //just testing to see if the animation system is working
        if(exampleTimerOnly < 0) {
            // I wouldn't reccomend using timers as calling animations, I'd just use dataparamater booleans
            //this.setAnimation(ANIMATION_SHOOT);
            exampleTimerOnly = 40;
        } else {
            exampleTimerOnly--;
        }


        //sends the Animation Handler constant updates on the animations
        EZAnimationHandler.INSTANCE.updateAnimations(this);
    }

    /** Immune to Fall Damage, instead plays a sound effect. */
    public void fall(float distance, float damageMultiplier)
    { if (distance > 3) this.playSound(DeeperDepthsSoundEvents.BREEZE_LAND, 1.0F, 1.0F); }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public EZAnimation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(EZAnimation animation) {
        currentAnimation = animation;
    }

    //This is where you store a collective list of all the animations this entity is capable of ONLY USING THIS SYSTEM, there is no walk or idle animations
    @Override
    public EZAnimation[] getAnimations() {
        return new EZAnimation[]{ANIMATION_SHOOT};
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Shoot_Attack", this.isShootAttack());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.setShootAttack(nbt.getBoolean("Shoot_Attack"));
    }


    /** The entire Combat AI of the Breeze, very basic currently. */
    static class AIWindChargeAttack extends EntityAIBase
    {
        private final EntityBreeze breeze;
        private int attackStep;
        private int attackTime;
        private boolean canJump = false;
        private boolean didJump;

        public AIWindChargeAttack(EntityBreeze breezeIn)
        {
            this.breeze = breezeIn;
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.breeze.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        public void startExecuting() {
            this.attackStep = 0;
        }

        public void resetTask()
        {
            breeze.setShootAttack(false);
            EZAnimationHandler.INSTANCE.updateAnimations(breeze);
        }

        public void updateTask()
        {
            if (breeze.onGround)
            {
                didJump = false;
                --this.attackTime;
                EntityLivingBase entitylivingbase = this.breeze.getAttackTarget();
                double d0 = this.breeze.getDistanceSq(entitylivingbase);
                if (d0 < this.getFollowDistance() * this.getFollowDistance())
                {
                    if (this.attackStep == 1)
                    {
                        /** Animations aren't playing, what am I doing wrong here?? */
                        this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_INHALE, 1, 1);
                        breeze.setAnimation(ANIMATION_SHOOT);
                        breeze.setShootAttack(true);
                        EZAnimationHandler.INSTANCE.updateAnimations(breeze);
                    }


                    double d1 = entitylivingbase.posX - this.breeze.posX;
                    double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.breeze.posY + (double)(this.breeze.height / 2.0F));
                    double d3 = entitylivingbase.posZ - this.breeze.posZ;
                    if (this.attackTime <= 0)
                    {
                        ++this.attackStep;
                        if (this.attackStep == 20)
                        {
                            float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;

                            this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_SHOOT, 1, 1);

                            EntityWindCharge entitywindcharge = new EntityWindCharge(this.breeze.world, this.breeze, this.breeze);
                            double s1 = entitylivingbase.posY + entitylivingbase.height/3;
                            double s2 = entitylivingbase.posX - breeze.posX;
                            double s3 = s1 - entitywindcharge.posY;
                            double s4 = entitylivingbase.posZ - breeze.posZ;
                            float j = MathHelper.sqrt(s2 * s2 + s4 * s4) * 0.2F;
                            entitywindcharge.shoot(s2, s3 + (double)j, s4, 0.8F, 1.0F);

                            entitywindcharge.posY = this.breeze.posY + 0.5;
                            this.breeze.world.spawnEntity(entitywindcharge);
                            canJump = true;
                        }
                        else if(this.attackStep == 40)
                        {
                            if (!this.breeze.world.getBlockState(breeze.getPosition().up(2)).isSideSolid(this.breeze.world, breeze.getPosition().up(2), EnumFacing.DOWN))
                            {
                                this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_JUMP, 1, 1);
                                this.breeze.motionX = this.breeze.getRNG().nextGaussian()/1.1;
                                this.breeze.motionZ = this.breeze.getRNG().nextGaussian()/1.1;
                                this.breeze.motionY = 0.8D;
                                this.breeze.velocityChanged = true;
                            }

                            this.attackStep = 0;
                            this.breeze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                        }
                    }

                    this.breeze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                } else {
                    this.breeze.getNavigator().clearPath();
                    this.breeze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
                }
            }
            else if (canJump && !didJump)
            {
                canJump = true;
                didJump = true;
            }


            super.updateTask();
        }

        private double getFollowDistance() {
            IAttributeInstance iattributeinstance = this.breeze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 16.0 : iattributeinstance.getAttributeValue();
        }
    }
}