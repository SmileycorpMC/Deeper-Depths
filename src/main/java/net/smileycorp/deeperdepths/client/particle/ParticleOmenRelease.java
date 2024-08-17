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

@SideOnly(Side.CLIENT)
public class ParticleOmenRelease extends ParticleDeeperDepths
{
    private static final ResourceLocation OMEN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/ominous_cloud.png");

    public ParticleOmenRelease(TextureManager textureManager, World world, double x, double y, double z, double speedX, double ySpeed, double zSpeed)
    {
        super(textureManager, world, x, y, z, speedX, ySpeed, zSpeed, OMEN_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = speedX;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.particleMaxAge = 40;
        this.texSheetSeg = 5;
        this.canCollide = true;
        this.size = 1.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 22 / this.particleMaxAge;
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
        { return new ParticleOmenRelease(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}