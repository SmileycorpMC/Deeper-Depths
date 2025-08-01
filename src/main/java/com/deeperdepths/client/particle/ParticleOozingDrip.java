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
public class ParticleOozingDrip extends ParticleDeeperDepths
{
    /** Time before the Drip falls */
    private int hangTime;
    /** Time after the drip has landed */
    private int landedTime;

    private static final ResourceLocation OOZING_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/oozing_drip.png");

    public ParticleOozingDrip(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, OOZING_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
        this.texSheetSeg = 3;
        this.renderYOffset = this.height / 2;
        this.canCollide = true;
        this.particleScale = 1.5F;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!this.onGround)
        {
            ++hangTime;
            this.texSpot = Math.min(3, (this.hangTime / 4) * 3);
            this.motionY -= (double)0.1F;
        }
        else
        {
            ++landedTime;

            this.texSpot = Math.min(8, 3 + (this.landedTime / 2));
            this.setAlphaF(1.0F - ((float)landedTime / (float)this.particleMaxAge));
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleOozingDrip(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}