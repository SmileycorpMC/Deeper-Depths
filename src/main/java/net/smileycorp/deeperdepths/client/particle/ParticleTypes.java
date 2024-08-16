package net.smileycorp.deeperdepths.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This exists to grab the Factory for each Particle
 * */
public enum ParticleTypes
{
    TRIAL_OMEN;

    public int getId()
    {
        return this.ordinal();
    }

    @SideOnly(Side.CLIENT)
    public static IParticleFactory getFactory(int particleId)
    {
        switch(particleId)
        {
            default:
            case 0:
                return new ParticleTrialOmen.Factory();
            case 1:
                return new ParticleOmenRelease.Factory();
            case 2:
                return new ParticleOozingDrip.Factory();
        }
    }
}