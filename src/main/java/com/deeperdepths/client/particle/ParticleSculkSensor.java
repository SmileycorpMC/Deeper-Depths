package com.deeperdepths.client.particle;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.awt.*;

public class ParticleSculkSensor extends ParticleRedstone {

    private static final Color SCULK = new Color(0x39D6E0);
    private static final Color REDSTONE = new Color(0xFF0000);

    public ParticleSculkSensor(int id, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
        super(world, x, y, z, (float) xSpeed, (float) ySpeed, (float) zSpeed);
        setRBGColorF((float) SCULK.getRed() / 255f, (float) SCULK.getGreen() / 255f, (float) SCULK.getBlue() / 255f);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float t = MathHelper.clamp (((float)this.particleAge + partialTicks) / ((float)this.particleMaxAge + 1), 0, 1);
        particleRed = ((float)(REDSTONE.getRed() - SCULK.getRed()) * t + SCULK.getRed()) / 255f;
        particleGreen = ((float)(REDSTONE.getGreen() - SCULK.getGreen()) * t + SCULK.getGreen()) / 255f;
        particleBlue = ((float)(REDSTONE.getBlue() - SCULK.getBlue()) * t + SCULK.getBlue()) / 255f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

}
