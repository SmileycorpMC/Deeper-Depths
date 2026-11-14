package com.deeperdepths.client.particle;

import com.deeperdepths.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleVibration extends ParticleDeeperDepths
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/vibration.png");

    private static final float spinnWEEEEEEE = 2.0F;
    private final Vec3d targetPos;
    private final Vec3d direction;

    public ParticleVibration(TextureManager textureManager, World world, double startPosX, double startPosY, double startPosZ, double endPosX, double endPosY, double endPosZ, int travelTimeTicks)
    {
        super(textureManager, world, startPosX, startPosY, startPosZ, endPosX, endPosY, endPosZ, TEXTURE, 0);
        this.targetPos = new Vec3d(endPosX, endPosY, endPosZ);

        Vec3d current = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d motionVec = this.targetPos.subtract(current).scale(1.0 / (20 + (20 * this.rand.nextFloat())));

        this.motionX = motionVec.x * travelTimeTicks;
        this.motionY = motionVec.y * travelTimeTicks;
        this.motionZ = motionVec.z * travelTimeTicks;

        Vec3d away = new Vec3d(-this.motionX, -this.motionY, -this.motionZ);
        this.direction = away.lengthSquared() > 1.0E-8D ? away.normalize() : new Vec3d(0, 1, 0);

        this.particleMaxAge = Math.max(travelTimeTicks, 1);
        this.particleScale = 3.0F;
        this.texSheetSeg = 3;
        this.canCollide = false;
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
            return;
        }

        this.texSpot = (this.texSpot + 1) % 7;

        /* This handles the motion toward the targetPos. */
        double t = 1.0D / Math.max(this.particleMaxAge - this.particleAge, 1);
        this.setPosition(this.posX + (this.targetPos.x - this.posX) * t, this.posY + (this.targetPos.y - this.posY) * t, this.posZ + (this.targetPos.z - this.posZ) * t);
    }

    @Override
    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        Vec3d[] quad = quadRotateToFacing(generateBasicQuad(particleSize, (float) Math.toRadians(-90), 0, (float) Math.toRadians(-90)), this.direction);

        /* Where we do the fancy twists Vanilla does */
        float angle = MathHelper.sin((this.particleAge + partialTicks - (float) (Math.PI * 2)) * 0.05F) * spinnWEEEEEEE;
        double cos = MathHelper.cos(angle);
        double sin = MathHelper.sin(angle);

        for (int i = 0; i < quad.length; i++)
        {
            Vec3d v = quad[i];
            quad[i] = v.scale(cos).add(this.direction.crossProduct(v).scale(sin)).add(this.direction.scale(this.direction.dotProduct(v) * (1.0D - cos)));
        }

        return quad;
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
            int speed = parameters.length > 0 ? parameters[0] : 20;
            return new ParticleVibration(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, speed);
        }
    }
}