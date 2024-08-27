package net.smileycorp.deeperdepths.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.Constants;

@SideOnly(Side.CLIENT)
public class ParticleGust extends ParticleDeeperDepths
{
    private static final ResourceLocation OMEN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/particles/gust.png");

    public ParticleGust(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, OMEN_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.particleMaxAge = 8 + this.rand.nextInt(11);
        this.particleScale = 10.0F - this.rand.nextFloat() * 0.5F;
        this.texSheetSeg = 4;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 15 / this.particleMaxAge;
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleGust(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ); }
    }
}