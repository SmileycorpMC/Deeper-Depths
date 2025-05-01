package com.deeperdepths.client.particle;

import com.deeperdepths.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleShriek extends ParticleDeeperDepths
{
    private static final ResourceLocation SHRIEK_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/shriek.png");

    public ParticleShriek(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, SHRIEK_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 20;
        this.texSpot = 0;
        this.texSheetSeg = 1;
    }

    public void onUpdate()
    {
        super.onUpdate();

        float f = (float)this.particleAge / (float)this.particleMaxAge;
        this.particleScale += f/3;

        this.setAlphaF(1.0F - (float)particleAge / (float)this.particleMaxAge);
    }

    @Override
    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        /* Tilt barely adjusts? Rework this later, but it works for now. */
        double tilt = 0.0000001 * particleSize;
        return new Vec3d[]
                {
                        new Vec3d(-particleSize, particleSize + tilt, -particleSize),
                        new Vec3d(-particleSize, -particleSize + tilt,  particleSize),
                        new Vec3d( particleSize, -particleSize - tilt,  particleSize),
                        new Vec3d( particleSize, particleSize - tilt, -particleSize)
                };
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return brightnessIncreaseToFull(partialTicks); }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleShriek(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}
