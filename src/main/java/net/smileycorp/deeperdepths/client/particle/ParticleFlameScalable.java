package net.smileycorp.deeperdepths.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A bare-bones copy of ParticleFlame, where we can tweak and alter variables for customizability, such as scale
 * */
@SideOnly(Side.CLIENT)
public class ParticleFlameScalable extends Particle
{
    private final float flameScale;

    protected ParticleFlameScalable(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn)
    { this(worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn, 0.75F); }

    protected ParticleFlameScalable(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn, float scale)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn);
        this.motionX = this.motionX * 0.009999999776482582D + xMovementIn;
        this.motionY = this.motionY * 0.009999999776482582D + yMovementIn;
        this.motionZ = this.motionZ * 0.009999999776482582D + zMovementIn;
        this.posX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.posY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.posZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.flameScale = scale;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.setParticleTextureIndex(48);
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        float f = ((float)this.particleAge + p_189214_1_) / (float)this.particleMaxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(p_189214_1_);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            switch (parameters.length)
            {
                case 0:
                    return new ParticleFlameScalable(world, posX, posY, posZ, speedX, speedY, speedZ);
                case 1:
                    return new ParticleFlameScalable(world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return null;
        }
    }
}