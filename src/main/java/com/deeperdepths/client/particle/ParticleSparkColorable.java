package com.deeperdepths.client.particle;

import com.deeperdepths.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A generic-style spark particle, recolorable, for use with Lightning, Waxing, and Scraping
 * */
@SideOnly(Side.CLIENT)
public class ParticleSparkColorable extends ParticleDeeperDepths
{
    private static final ResourceLocation SPARK_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/spark.png");

    protected ParticleSparkColorable(TextureManager textureManager, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double movementX, double movementY, double movementZ)
    { this(textureManager, worldIn, xCoordIn, yCoordIn, zCoordIn, movementX, movementY, movementZ, 30); }

    protected ParticleSparkColorable(TextureManager textureManager, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double movementX, double movementY, double movementZ, int maxAge)
    { this(textureManager, worldIn, xCoordIn, yCoordIn, zCoordIn, movementX, movementY, movementZ, maxAge, 0F, 0F, 0F); }

    public ParticleSparkColorable(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int maxAge, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, SPARK_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.particleMaxAge = maxAge;
        setRBGColorF(red * 0.00392156862F, green * 0.00392156862F,blue * 0.00392156862F);
        this.texSpot = 0;
        this.texSheetSeg = 2;
        this.particleScale = 1.5F;
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return brightnessIncreaseToFull(partialTicks); }

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