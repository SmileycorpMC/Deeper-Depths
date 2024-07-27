package net.smileycorp.deeperdepths.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;

import java.util.List;

public class EntityWindCharge extends EntityThrowable
{
    /** Sets a certain entity to be completely immune. Used so Breeze don't get knockback from their own Wind Charges. */
    protected EntityLivingBase knockbackImmune;

    public EntityWindCharge(World worldIn, EntityLivingBase throwerIn)
    { super(worldIn, throwerIn); }

    public EntityWindCharge(World worldIn, EntityLivingBase throwerIn, EntityLivingBase knockbackImmuneIn)
    {
        super(worldIn, throwerIn);
        knockbackImmune = knockbackImmuneIn;
    }

    public EntityWindCharge(World worldIn, double x, double y, double z)
    { super(worldIn, x, y, z); }

    public EntityWindCharge(World worldIn)
    {
        super(worldIn);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        { return false; }
        else
        {
            this.markVelocityChanged();
            if (source.getTrueSource() != null)
            {
                Vec3d vec3d = source.getTrueSource().getLookVec();
                if (vec3d != null)
                {
                    this.motionX = vec3d.x;
                    this.motionY = vec3d.y;
                    this.motionZ = vec3d.z;
                }

                if (source.getTrueSource() instanceof EntityLivingBase)
                { this.thrower = (EntityLivingBase)source.getTrueSource(); }

                return true;
            }
            else
            { return false; }
        }
    }

    protected void onImpact(RayTraceResult result)
    {
        SoundEvent burstSound = knockbackImmune instanceof EntityBreeze ? DeeperDepthsSoundEvents.BREEZE_WIND_BURST : DeeperDepthsSoundEvents.WIND_CHARGE_WIND_BURST;

        this.playSound(burstSound, 1, 1);
        if (!this.world.isRemote)
        {
            if (result.entityHit != null)
            {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)1);
            }

            preformKnockbackEffects();

            this.world.setEntityState(this, (byte)3);
            //this.world.newExplosion(null, this.posX, this.posY, this.posZ, 0, false, false);
            this.setDead();
        }
    }

    /** No gravity, keep going forward!! */
    protected float getGravityVelocity() {
        return 0.0F;
    }

    /** Mostly from Minecraft's Explosion Code, as Wind Charges use the same code, but altered to remove damage. */
    public void preformKnockbackEffects()
    {
        float scale = 10;
        double knockbackStrength = 0.9;
        float k = MathHelper.floor(this.posX - (double) scale - 1.0);
        float l = MathHelper.floor(this.posX + (double) scale + 1.0);
        int i2 = MathHelper.floor(this.posY - (double) scale - 1.0);
        int i1 = MathHelper.floor(this.posY + (double) scale + 1.0);
        int j2 = MathHelper.floor(this.posZ - (double) scale - 1.0);
        int j1 = MathHelper.floor(this.posZ + (double) scale + 1.0);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB((double) k, (double) i2, (double) j2, (double) l, (double) i1, (double) j1));
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

        for (Entity entity : list)
        {
            if (!entity.isImmuneToExplosions() && entity != this.knockbackImmune)
            {
                double d12 = entity.getDistance(this.posX, this.posY, this.posZ) / (double) scale;
                if (d12 <= 1.0)
                {
                    double dx = entity.posX - this.posX;
                    double dy = entity.posY + (double) entity.getEyeHeight() - this.posY;
                    double dz = entity.posZ - this.posZ;
                    double distance = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                    if (distance != 0.0) {
                        dx /= distance;
                        dy /= distance;
                        dz /= distance;
                        double blockStoppage = (double) this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double kmult = (knockbackStrength - d12) * blockStoppage;

                        entity.motionX += dx * kmult;
                        entity.motionY += dy * kmult;
                        entity.motionZ += dz * kmult;
                        entity.velocityChanged = true;
                    }
                }
            }
        }
    }
}