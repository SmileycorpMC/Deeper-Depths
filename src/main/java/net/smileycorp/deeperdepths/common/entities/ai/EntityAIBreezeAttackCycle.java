package net.smileycorp.deeperdepths.common.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;
import net.smileycorp.deeperdepths.common.entities.EntityWindCharge;

/** The combat AI for the Breeze. Composed of multiple loops, in a continuous order, be careful when tweaking! */
public class EntityAIBreezeAttackCycle extends EntityAIBase
{
    private final EntityBreeze breeze;
    private final double entityMoveSpeed;
    /** Timer used for entering Attack State. */
    private int attackTimer = 0;
    /** Timer used to space out actions. */
    private int actionTimer = 0;
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
    /** How close the Breeze must be to preform an attack. 0 makes this range infinite. */
    double attackDistance;
    double retreatDistance = 3.0F;

    public EntityAIBreezeAttackCycle(EntityBreeze breezeIn, double moveSpeedIn, double attackDistanceIn)
    {
        this.breeze = breezeIn;
        this.setMutexBits(3);
        entityMoveSpeed = moveSpeedIn;
        attackDistance = attackDistanceIn;
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.breeze.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }

    /** First shot is half off! */
    public void startExecuting()
    {
        attackTimer = 10;
        actionTimer = 0;
    }

    public void resetTask()
    {
        breeze.setShootAttack(false);
        breeze.setJumpingAnim(false);
    }

    /** Tries to break up Breeze AI into separate loops. There is a priority, and interruptions to some loops. */
    public void updateTask()
    {
        if (!isJumping) attackTimer++;
        EntityLivingBase entitylivingbase = this.breeze.getAttackTarget();
        double d0 = this.breeze.getDistanceSq(entitylivingbase);
        boolean dontGet2Close = d0 <= retreatDistance * retreatDistance;

        float targetYaw = entitylivingbase.rotationYawHead * ((float)Math.PI / 180F);
        Vec3d targetLook = new Vec3d(-Math.sin(targetYaw), 0, Math.cos(targetYaw));

        double randomOffsetX = (this.breeze.getRNG().nextDouble() - 0.5) * 2.0;
        double randomOffsetZ = (this.breeze.getRNG().nextDouble() - 0.5) * 2.0;

        Vec3d behindTarget = entitylivingbase.getPositionVector().subtract(targetLook.scale(repositionDistance)).addVector(randomOffsetX, 0, randomOffsetZ);

        if (isRetreating && attackTimer <= maxRetreatTime) doRetreat(entitylivingbase, dontGet2Close);
        else if (isJumping) doJump(behindTarget);
        else if (attackTimer > 20) doAttack(entitylivingbase, d0, dontGet2Close);
        else
        {
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

    /** Makes the Breeze Retreat from its target. */
    private void doRetreat(Entity target, boolean dontGet2Close)
    {
        /* Retreat can cancel the Shooting, so make sure to stop the animation! */
        breeze.setShootAttack(false);
        ++this.actionTimer;

        if (!hasMovement)
        {
            this.breeze.getNavigator().clearPath();

            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.breeze, 4, 4, new Vec3d(target.posX, target.posY, target.posZ));

            if (vec3d != null) this.breeze.getNavigator().setPath(this.breeze.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z), entityMoveSpeed);

            this.hasMovement = true;
        }
        /* TODO: Decide on if the Breeze should have smarter Water Retreat Behavior. */
        //if (this.breeze.isInWater()) this.doAWackyBoing();

        if (this.breeze.getNavigator().noPath())
        {
            this.actionTimer = 0;
            this.hasMovement = false;
            if (!dontGet2Close) isRetreating = false;
        }
    }

    /** Makes the Breeze wind-up and preform a Jump. */
    private void doJump(Vec3d behindTarget)
    {
        this.breeze.getNavigator().clearPath();
        this.breeze.getLookHelper().setLookPosition(behindTarget.x, behindTarget.y, behindTarget.z, 10.0F, 10.0F);
        ++this.actionTimer;

        if (this.actionTimer == 5)
        {
            breeze.setJumpingAnim(true);
            this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_CHARGE, 1, 1);
        }
        else if (this.actionTimer == 20)
        {
            double moveDistance = breeze.getDistanceSq(behindTarget.x, behindTarget.y, behindTarget.z);
            double dX = behindTarget.x - this.breeze.posX;
            double dZ = behindTarget.z - this.breeze.posZ;
            double horizontalDistance = Math.sqrt(dX * dX + dZ * dZ);
            this.breeze.motionY = Math.min(1.0, 0.2D + moveDistance * 0.01D);
            this.breeze.motionX = (dX / horizontalDistance) * (entityMoveSpeed * 3);
            this.breeze.motionZ = (dZ / horizontalDistance) * (entityMoveSpeed * 3);

            this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_JUMP, 1, 1);
            this.breeze.velocityChanged = true;
        }

        if (actionTimer >= 25)
        {
            breeze.setJumpingAnim(false);
            if (this.breeze.onGround || breeze.isInWater())
            {
                this.actionTimer = 0;
                this.attackTimer = 20;
                isJumping = false;
            }
        }
    }

    /** Makes the Breeze charge, check, then fire a Wind Charge. Interruptible by Retreat behavior. */
    private void doAttack(Entity target, double distance, boolean retreatCheck)
    {
        /* Retreat can cancel the Shooting, so make sure to stop the animation! */
        if (retreatCheck && attackTimer <= maxRetreatTime)
        {
            hasMovement = false;
            isRetreating = true;
        }

        boolean seeTarget = this.breeze.getEntitySenses().canSee(target);

        this.breeze.getNavigator().clearPath();
        this.breeze.getLookHelper().setLookPositionWithEntity(target, 10.0F, 10.0F);

        if ((attackDistance == 0 || distance <= attackDistance * attackDistance) && seeTarget)
        {
            ++this.actionTimer;
            if (this.actionTimer == 5 && (breeze.onGround || breeze.isInWater()))
            {
                this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_INHALE, 1, 1);
                breeze.setShootAttack(true);
            }
            else if (this.actionTimer >= 25 && (breeze.onGround || breeze.isInWater()))
            {
                this.breeze.playSound(DeeperDepthsSoundEvents.BREEZE_SHOOT, 1, 1);
                shootWindCharge(target);
                breeze.setShootAttack(false);
                hasMovement = false;
                this.actionTimer = 0;
                this.attackTimer = 0;
            }
        }
        else
        {
            breeze.setShootAttack(false);
            this.breeze.getNavigator().clearPath();

            this.breeze.getNavigator().tryMoveToXYZ(target.posX, target.posY, target.posZ, entityMoveSpeed);

            this.actionTimer = 0;
        }
    }

    /** Spawns and shoots the Wind Charge. Sectioned off for readability. */
    private void shootWindCharge(Entity target)
    {
        EntityWindCharge entitywindcharge = new EntityWindCharge(this.breeze.world, this.breeze, this.breeze);
        double s1 = (target.posY - target.height*0.3) - breeze.posY;
        double s2 = target.posX - breeze.posX;
        double s4 = target.posZ - breeze.posZ;
        entitywindcharge.shoot(s2, s1, s4, 0.7F, 1.0F);

        /* This lets you set the charge's spawn height */
        //entitywindcharge.posY = this.breeze.posY + 0.5;
        entitywindcharge.posY = this.breeze.posY + (double)(this.breeze.height / 2.0F) + 0.5D;
        this.breeze.world.spawnEntity(entitywindcharge);
    }
}