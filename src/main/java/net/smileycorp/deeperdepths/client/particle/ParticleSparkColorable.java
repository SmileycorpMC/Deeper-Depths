package net.smileycorp.deeperdepths.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.Constants;

/**
 * A generic-style spark particle, recolorable, for use with Lightning, Waxing, and Scraping
 * */
@SideOnly(Side.CLIENT)
public class ParticleSparkColorable extends ParticleDeeperDepths
{
    private static final ResourceLocation SPARK_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/spark.png");

    protected ParticleSparkColorable(TextureManager textureManager, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn)
    { this(textureManager, worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn, 30); }

    protected ParticleSparkColorable(TextureManager textureManager, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xMovementIn, double yMovementIn, double zMovementIn, int maxAge)
    { this(textureManager, worldIn, xCoordIn, yCoordIn, zCoordIn, xMovementIn, yMovementIn, zMovementIn, maxAge, 0F, 0F, 0F); }

    public ParticleSparkColorable(TextureManager textureManager, World world, double x, double y, double z, double speedX, double ySpeed, double zSpeed, int maxAge, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, speedX, ySpeed, zSpeed, SPARK_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = speedX;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.particleMaxAge = maxAge;
        this.particleGravity = 0.0F;
        this.renderYOffset = 0;
        this.texSpot = 0;
        this.particleRed = red * 0.00392156862F;
        this.particleGreen = green * 0.00392156862F;
        this.particleBlue = blue * 0.00392156862F;
        this.texSheetSeg = 2;
        this.size = 0.15F;
        this.size *= this.rand.nextFloat() * 0.6F + 0.5F;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @Override
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
                    return new ParticleSparkColorable(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ);
                case 1:
                    return new ParticleSparkColorable(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
                case 4:
                    return new ParticleSparkColorable(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], parameters[1], parameters[2], parameters[3]);
            }
            return null;
        }
    }
}