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
    /** Makes the particle wait in place, before preforming any logic. Also adds to the max age. */
    private int waitTime;
    /** The scale that this starts with. */
    private final float startScale;

    private static final ResourceLocation SHRIEK_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/shriek.png");

    public ParticleShriek(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
    { this(textureManager, world, x, y, z, movementX, movementY, movementZ, 0); }

    public ParticleShriek(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int waitTimeIn)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, SHRIEK_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = 30;
        this.startScale = 10.0F;
        this.waitTime = waitTimeIn;
        this.texSpot = 0;
        this.texSheetSeg = 1;
    }

    public void onUpdate()
    {
        if (--this.waitTime <= 0)
        {
            super.onUpdate();

            float ageTime = Math.min(((float)this.particleAge) / (float)this.particleMaxAge * 0.75F, 1.0F);
            this.particleScale = startScale * ageTime;
            this.setAlphaF(1.0F - ageTime);
        }
    }

    @Override
    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        /* 60 degrees tilt, since this current math makes 0 completely vertical, when we want a more horizontal tilt. */
        double tilt = Math.toRadians(60);
        double cos = Math.cos(tilt);
        double sin = Math.sin(tilt);
        return new Vec3d[]
                {
                new Vec3d(-particleSize, particleSize * cos, -particleSize * sin),
                new Vec3d(-particleSize, -particleSize * cos, particleSize * sin),
                new Vec3d(particleSize, -particleSize * cos, particleSize * sin),
                new Vec3d(particleSize, particleSize * cos, -particleSize * sin)
        };

    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 240; }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            switch (parameters.length)
            {
                case 0:
                    return new ParticleShriek(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ);
                case 1:
                    return new ParticleShriek(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]);
            }
            return null;
        }
    }
}
