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
 * A bare-bones copy of ParticleFlame, where we can tweak and alter variables for customizability, such as scale
 * */
@SideOnly(Side.CLIENT)
public class ParticleFlameScalable extends ParticleDeeperDepths
{
    private static final ResourceLocation FLAME_TEXTURE1 = new ResourceLocation(Constants.MODID, "textures/particles/flame_candle.png");
    private static final ResourceLocation FLAME_TEXTURE2 = new ResourceLocation(Constants.MODID, "textures/particles/flame_soul.png");
    private final float flameScale;

    protected ParticleFlameScalable(TextureManager textureManager, World worldIn, double x, double y, double z, double xMovementIn, double yMovementIn, double zMovementIn)
    { this(textureManager, worldIn, x, y, z, xMovementIn, yMovementIn, zMovementIn, 0, 15); }

    protected ParticleFlameScalable(TextureManager textureManager, World worldIn, double x, double y, double z, double xMovementIn, double yMovementIn, double zMovementIn, int textureNum)
    { this(textureManager, worldIn, x, y, z, xMovementIn, yMovementIn, zMovementIn, textureNum, 15); }

    protected ParticleFlameScalable(TextureManager textureManager, World worldIn, double x, double y, double z, double xMovementIn, double yMovementIn, double zMovementIn, int textureNum, int scale)
    {
        super(textureManager, worldIn, x, y, z, xMovementIn, yMovementIn, zMovementIn, textureNum == 0 ? FLAME_TEXTURE1 : FLAME_TEXTURE2, 0);
        this.motionX = this.motionX * 0.009999999776482582D + xMovementIn;
        this.motionY = this.motionY * 0.009999999776482582D + yMovementIn;
        this.motionZ = this.motionZ * 0.009999999776482582D + zMovementIn;
        this.posX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.posY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.posZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
        this.flameScale = (scale * 0.01F);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.texSpot = 0;
        this.texSheetSeg = 1;
        this.renderYOffset = 0;

        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
    }

    public void onUpdate()
    {
        super.onUpdate();
        float f = ((float)this.particleAge ) / (float)this.particleMaxAge;
        this.size = this.flameScale * (1.0F - f * f * 0.5F);
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
        //this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
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