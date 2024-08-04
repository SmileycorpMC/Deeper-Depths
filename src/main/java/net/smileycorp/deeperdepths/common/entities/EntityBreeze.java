package net.smileycorp.deeperdepths.common.entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.deeperdepths.animation.EZAnimation;
import net.smileycorp.deeperdepths.animation.EZAnimationHandler;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.common.DeeperDepthsLootTables;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
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

    public EntityBreeze(World worldIn)
    {
        super(worldIn);
        //this.jumpMovementFactor = this.getAIMoveSpeed() * 0.21600003184F;
        this.experienceValue = 10;
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
        this.tasks.addTask(4, new AIWindChargeAttack(this, 0.8));
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

    @Nullable
    protected ResourceLocation getLootTable() {
        return DeeperDepthsLootTables.BREEZE_DROPS;
    }

    protected SoundEvent getAmbientSound()
    { return this.onGround ? DeeperDepthsSoundEvents.BREEZE_IDLE_GROUND : DeeperDepthsSoundEvents.BREEZE_IDLE_AIR; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return DeeperDepthsSoundEvents.BREEZE_HURT; }

    protected SoundEvent getDeathSound() {
        return DeeperDepthsSoundEvents.BREEZE_DEATH;
    }

    //used for animation system
    public int animationTick;
    //just a variable that holds what the current animation is
    private EZAnimation currentAnimation;

    int exampleTimerOnly = 40;

    @Override
    public void onUpdate() {
        super.onUpdate();
        //this.onGround = true;
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

        if(this.isShootAttack() && this.currentAnimation == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SHOOT);
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

    /** The entire Combat AI of the Breeze, very basic currently.
     *
     * Has 3 States: Attack, Reposition, and Jump
     * Base state is Reposition, trying to move behind the target. Hard interrupted by Jump or Attack state.
     * Timer counts down before it enters Attack State. Attack only temp interupted by Retreating, will resume.
     * Reposition can randomly go into Jump. Jump has no interrupt.
     *
     * Breeze will try to stack behind target, keep distance, and attack.
     * */
    static class AIWindChargeAttack extends EntityAIBase
    {
        private final EntityBreeze breeze;
        private final double entityMoveSpeed;

        /** Timer used for entering Attack State. */
        private int attackTimer = 0;
        /** Timer used to space out actions. */
        private int actionTimer = 0;
        private int pauseTimer = 0;
        /** If the Breeze needs to retreat currently. */
        private boolean isRetreating = false;
        /** If the Breeze is preforming a Jump. */
        private boolean isJumping = false;
        /** Max allowed time before a Breeze gives up on Retreating, and attacks. */
        int maxRetreatTime = 80;
        /** The distance to move behind the target. */
        double repositionDistance = 8.0;
        /** Movement has been give to the Breeze, and it is headed there. */
        private boolean hasMovement = false;
        double attackDistance = 16.0F;
        double retreatDistance = 3.0F;

        public AIWindChargeAttack(EntityBreeze breezeIn, double moveSpeedIn)
        {
            this.breeze = breezeIn;
            this.setMutexBits(3);
            entityMoveSpeed = moveSpeedIn;
        }

        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.breeze.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        public void startExecuting()
        {
            attackTimer = 10;
            actionTimer = 0;
        }


        public void resetTask()
        {
            breeze.setShootAttack(false);
            //this is used in the onUpdate
           // EZAnimationHandler.INSTANCE.updateAnimations(breeze);
        }

        public void updateTask()
        {
            if (!isJumping) attackTimer++;
            boolean seeTarget = this.breeze.getEntitySenses().canSee(this.breeze.getAttackTarget());
            EntityLivingBase entitylivingbase = this.breeze.getAttackTarget();
            double d0 = this.breeze.getDistanceSq(entitylivingbase);
            boolean dontGet2Close = d0 <= retreatDistance * retreatDistance;

            float targetYaw = entitylivingbase.rotationYawHead * ((float)Math.PI / 180F);
            Vec3d targetLook = new Vec3d(-Math.sin(targetYaw), 0, Math.cos(targetYaw));

            double randomOffsetX = (this.breeze.getRNG().nextDouble() - 0.5) * 2.0;
            double randomOffsetZ = (this.breeze.getRNG().nextDouble() - 0.5) * 2.0;

            Vec3d behindTarget = entitylivingbase.getPositionVector().subtract(targetLook.scale(repositionDistance)).addVector(randomOffsetX, 0, randomOffsetZ);

            if (isRetreating && attackTimer <= maxRetreatTime)
            {
                breeze.setShootAttack(false);
                doRetreat(entitylivingbase, dontGet2Close);
            }
            else if (isJumping)
            {
                this.breeze.getNavigator().clearPath();
                this.breeze.getLookHelper().setLookPosition(behindTarget.x, behindTarget.y, behindTarget.z, 10.0F, 10.0F);
                ++this.actionTimer;

                if (this.actionTimer == 5)
                {
                    this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_CHARGE, 1, 1);
                }
                else if (this.actionTimer == 20)
                {
                    double moveDistance = breeze.getDistanceSq(behindTarget.x, behindTarget.y, behindTarget.z);
                    this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_JUMP, 1, 1);
                    this.breeze.motionY = Math.min(1.0, 0.2D + moveDistance * 0.01D);
                    this.breeze.velocityChanged = true;
                    this.breeze.getMoveHelper().setMoveTo(behindTarget.x, behindTarget.y, behindTarget.z, entityMoveSpeed * 3);
                }

                if (actionTimer >= 25 && (this.breeze.onGround || breeze.isInWater()) )
                {
                    this.actionTimer = 0;
                    this.attackTimer = 20;
                    isJumping = false;
                }
            }
            else if (attackTimer > 20)
            {
                if (dontGet2Close && attackTimer <= maxRetreatTime)
                {
                    hasMovement = false;
                    isRetreating = true;
                }

                this.breeze.getNavigator().clearPath();
                this.breeze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);

                if (d0 <= attackDistance * attackDistance && seeTarget)
                {
                    ++this.actionTimer;
                    if (this.actionTimer == 5 && (breeze.onGround || breeze.isInWater()))
                    {
                        this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_INHALE, 1, 1);
                        breeze.setShootAttack(true);
                    }
                    else if (this.actionTimer >= 25 && seeTarget && (breeze.onGround || breeze.isInWater()))
                    {
                        attackWindCharge(entitylivingbase, d0);
                        breeze.setShootAttack(false);
                        hasMovement = false;
                        this.actionTimer = 0;
                        this.attackTimer = 0;
                        pauseTimer = 5 + breeze.getRNG().nextInt(6);
                    }
                }
                else
                {
                    breeze.setShootAttack(false);
                    this.breeze.getNavigator().clearPath();

                    this.breeze.getNavigator().tryMoveToXYZ(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entityMoveSpeed);

                    this.actionTimer = 0;
                }
            }
            else
            {
                breeze.setShootAttack(false);
                if (!hasMovement)
                {
                    if (breeze.getRNG().nextInt(4) != 0 || breeze.isInWater()) isJumping = true;
                    hasMovement = true;
                }

                this.breeze.getNavigator().clearPath();
                this.breeze.getNavigator().tryMoveToXYZ(behindTarget.x, behindTarget.y, behindTarget.z, entityMoveSpeed);
            }

            super.updateTask();
        }

        /** Shoot a Wind Charge at the target entity. */
        private void doRetreat(Entity target, boolean dontGet2Close)
        {
            ++this.actionTimer;

            if (!hasMovement)
            {
                this.breeze.getNavigator().clearPath();

                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.breeze, 4, 4, new Vec3d(target.posX, target.posY, target.posZ));

                if (vec3d != null) this.breeze.getNavigator().setPath(this.breeze.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z), entityMoveSpeed);

                this.hasMovement = true;
            }


            if (this.breeze.isInWater())
            {
                this.doAWackyBoing();
            }


            if (this.breeze.getNavigator().noPath())
            {
                this.actionTimer = 0;
                this.hasMovement = false;
                if (!dontGet2Close) isRetreating = false;
            }
        }

        /** Shoot a Wind Charge at the target entity. */
        private void attackWindCharge(Entity target, double distance)
        {
            float f = MathHelper.sqrt(MathHelper.sqrt(distance)) * 0.5F;

            this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_SHOOT, 1, 1);

            EntityWindCharge entitywindcharge = new EntityWindCharge(this.breeze.world, this.breeze, this.breeze);
            double s1 = target.posY - this.breeze.posY + 0.5;
            double s2 = target.posX - breeze.posX;
            //double s3 = s1 - entitywindcharge.posY;
            double s4 = target.posZ - breeze.posZ;
            float j = MathHelper.sqrt(s2 * s2 + s4 * s4) * 0.2F;
            entitywindcharge.shoot(s2, s1, s4, 0.7F, 1.0F);

            entitywindcharge.posY = this.breeze.posY + 0.5;
            this.breeze.world.spawnEntity(entitywindcharge);
        }

        private void doAWackyBoing()
        {
            this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_JUMP, 1, 1);
            //this.breeze.motionX = this.breeze.getRNG().nextGaussian()/1.1;
            //this.breeze.motionZ = this.breeze.getRNG().nextGaussian()/1.1;
            this.breeze.motionY = 0.8D;
            this.breeze.velocityChanged = true;
        }

        private double getFollowDistance() {
            IAttributeInstance iattributeinstance = this.breeze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 16.0 : iattributeinstance.getAttributeValue();
        }
    }

    /** Basic, reusable reflecting code, also handles the sound. */
    private static void deflectProjectile(Entity projectile)
    {
        double bounceStrength = -1.0D;
        projectile.motionX *= bounceStrength;
        projectile.motionY *= bounceStrength;
        projectile.motionZ *= bounceStrength;
        projectile.velocityChanged = true;

        projectile.world.playSound(null, projectile.getPosition(), DeeperDepthsSoundEvents.BREEZE_DEFLECT, SoundCategory.HOSTILE, 1, 1);
    }

    /** Events have to be used for Projectile Reflection, as `attackEntityFrom` is called within onImpact for most projectiles, which breaks this behavior! */
    @SubscribeEvent
    public static void reflectArrowEvent(ProjectileImpactEvent.Arrow event)
    {
        final EntityArrow projectile = event.getArrow();

        if (projectile.getEntityWorld().isRemote) return;
        Entity entity = event.getRayTraceResult().entityHit;

        if (event.getEntity() != null && entity instanceof EntityBreeze)
        {
            deflectProjectile(projectile);
            projectile.shootingEntity = entity;

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void reflectFireballEvent(ProjectileImpactEvent.Fireball event)
    {
        final EntityFireball projectile = event.getFireball();

        if (projectile.getEntityWorld().isRemote) return;
        Entity entity = event.getRayTraceResult().entityHit;

        if (event.getEntity() != null && entity instanceof EntityBreeze)
        {
            deflectProjectile(projectile);
            double bounceStrength = -1.0D;
            projectile.accelerationX *= bounceStrength;
            projectile.accelerationY *= bounceStrength;
            projectile.accelerationZ *= bounceStrength;
            projectile.shootingEntity = (EntityLivingBase) entity;

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void reflectThrowableEvent(ProjectileImpactEvent.Throwable event)
    {
        final EntityThrowable projectile = event.getThrowable();
        if (projectile instanceof EntityWindCharge) return;

        if (projectile.getEntityWorld().isRemote) return;
        Entity entity = event.getRayTraceResult().entityHit;

        if (event.getEntity() != null && entity instanceof EntityBreeze)
        {
            deflectProjectile(projectile);
            //projectile.thrower = entityBlocking;
            event.setCanceled(true);
        }
    }
}