package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.smileycorp.deeperdepths.common.entities.EntityWindCharge;

/**
 * The Wind Charged Effect.
 * Requires Particles, and a Mixin for Weaving allowing slightly faster web movement.
 */
public class PotionWindCharged extends PotionDeeperDepths
{
    protected PotionWindCharged(String nameIn, boolean isBadEffectIn, int liquidColorIn)
    {
        super(nameIn, isBadEffectIn, liquidColorIn, 6);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        /* Particles will need to be fed through a Packet Handler. */
        //entity.world.spawnParticle(EnumParticleTypes.SLIME, entity.posX, entity.posY, entity.posZ, 0, 0,0, new int[0]);

        /* Currently hard-coded against Slimes, make configurable later. */
        if (!entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.WIND_CHARGED);

        if(entity.isDead && !entity.world.isRemote)
        {
            EntityWindCharge entitywindcharge = new EntityWindCharge(entity.world, entity, entity);
            entitywindcharge.posY = entity.posY;
            entitywindcharge.forceSpawn = true;
            entitywindcharge.forceExplode(null);
            entity.world.spawnEntity(entitywindcharge);
        }
    }
}