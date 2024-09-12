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
public class ParticleSpawnerDetect extends ParticleDeeperDepths
{
    private static final ResourceLocation DETECT_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/trial_spawner_detect.png");
    private static final ResourceLocation DETECT_OMINOUS_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/trial_spawner_detect_ominous.png");

    protected ParticleSpawnerDetect(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ)
    { this(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, 0); }

    protected ParticleSpawnerDetect(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ, int ominous)
    {
        super(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, ominous == 1 ? DETECT_OMINOUS_TEXTURE : DETECT_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += movementX;
        this.motionY += movementY;
        this.motionZ += movementZ;
        this.particleMaxAge = 10;
        this.texSheetSeg = 3;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 5 / this.particleMaxAge;
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
                    return new ParticleSpawnerDetect(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ);
                case 1:
                    return new ParticleSpawnerDetect(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return null;
        }
    }
}