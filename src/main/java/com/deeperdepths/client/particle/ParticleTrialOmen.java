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

@SideOnly(Side.CLIENT)
public class ParticleTrialOmen extends ParticleDeeperDepths
{
    private static final ResourceLocation OMEN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/trial_omen.png");

    public ParticleTrialOmen(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, OMEN_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += movementX;
        this.motionY += movementY;
        this.motionZ += movementZ;
        this.particleMaxAge = (int)(Math.random() * 10.0D) + 20;
        this.texSpot = 0;
        this.texSheetSeg = 1;
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return brightnessIncreaseToFull(partialTicks); }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleTrialOmen(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}