package net.smileycorp.deeperdepths.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
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
public class ParticleSpawnerDetect extends ParticleDeeperDepths
{
    private static final ResourceLocation DETECT_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/trial_spawner_detect.png");
    private static final ResourceLocation DETECT_OMINOUS_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/trial_spawner_detect_ominous.png");

    protected ParticleSpawnerDetect(TextureManager textureManager, World worldIn, double x, double y, double z, double xMovementIn, double yMovementIn, double zMovementIn)
    { this(textureManager, worldIn, x, y, z, xMovementIn, yMovementIn, zMovementIn, 0); }

    protected ParticleSpawnerDetect(TextureManager textureManager, World worldIn, double x, double y, double z, double xMovementIn, double yMovementIn, double zMovementIn, int ominous)
    {
        super(textureManager, worldIn, x, y, z, xMovementIn, yMovementIn, zMovementIn, ominous == 1 ? DETECT_OMINOUS_TEXTURE : DETECT_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += xMovementIn;
        this.motionY += yMovementIn;
        this.motionZ += zMovementIn;
        this.particleMaxAge = 10;
        this.texSheetSeg = 3;
        this.size = 0.1F;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 5 / this.particleMaxAge;
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
                    return new ParticleSpawnerDetect(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ);
                case 1:
                    return new ParticleSpawnerDetect(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return null;
        }
    }
}