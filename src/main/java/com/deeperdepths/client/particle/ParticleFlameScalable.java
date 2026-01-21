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
 * A bare-bones copy of ParticleFlame, where we can tweak and alter variables for customizability, such as scale
 * */
@SideOnly(Side.CLIENT)
public class ParticleFlameScalable extends ParticleDeeperDepths
{
    private static final ResourceLocation FLAME_TEXTURE1 = new ResourceLocation(Constants.MODID, "textures/particles/flame_candle.png");
    private static final ResourceLocation FLAME_TEXTURE2 = new ResourceLocation(Constants.MODID, "textures/particles/flame_soul.png");
    private static final ResourceLocation FLAME_TEXTURE3 = new ResourceLocation(Constants.MODID, "textures/particles/flame_copper.png");
    private final float flameScale;

    protected ParticleFlameScalable(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ)
    { this(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, 0, 15); }

    protected ParticleFlameScalable(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ, int textureNum)
    { this(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, textureNum, 15); }

    protected ParticleFlameScalable(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ, int textureNum, int scale)
    {
        super(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, textureNum == 0 ? FLAME_TEXTURE1 : textureNum == 1 ? FLAME_TEXTURE2: FLAME_TEXTURE3, 0);
        this.motionX = this.motionX * 0.009999999776482582D + movementX;
        this.motionY = this.motionY * 0.009999999776482582D + movementY;
        this.motionZ = this.motionZ * 0.009999999776482582D + movementZ;
        this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.flameScale = (scale * 0.1F);

        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
    }

    public void onUpdate()
    {
        super.onUpdate();
        float f = ((float)this.particleAge ) / (float)this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
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
                    return new ParticleFlameScalable(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ);
                case 1:
                    return new ParticleFlameScalable(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
                case 2:
                    return new ParticleFlameScalable(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0], parameters[1]);
            }
            return null;
        }
    }
}