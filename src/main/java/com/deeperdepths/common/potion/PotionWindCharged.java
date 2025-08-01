package com.deeperdepths.common.potion;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.entities.EntityWindCharge;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * The Wind Charged Effect.
 * Requires Particles, and a Mixin for Weaving allowing slightly faster web movement.
 */
@Mod.EventBusSubscriber
public class PotionWindCharged extends PotionDeeperDepths
{
    protected PotionWindCharged(String nameIn, boolean isBadEffectIn, int liquidColorIn)
    {
        super(nameIn, isBadEffectIn, liquidColorIn, 6);
    }

    /** Uses Forge's `LivingDeathEvent`, as repeatedly scanning if an Entity is dead is silly. */
    @SubscribeEvent
    public static void onWindChargedDeathEvent(LivingDeathEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        if (!entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.WIND_CHARGED);

        if (!entity.isPotionActive(DeeperDepthsPotions.WIND_CHARGED)) return;

        if(!entity.world.isRemote)
        {
            EntityWindCharge entitywindcharge = new EntityWindCharge(entity.world, entity, entity);
            entitywindcharge.posY = entity.posY;
            entitywindcharge.forceSpawn = true;
            entitywindcharge.forceExplode(null);
            entity.world.spawnEntity(entitywindcharge);
        }

        /* Remove after preforming the effect once. */
        entity.removePotionEffect(DeeperDepthsPotions.WIND_CHARGED);
    }

    @Override
    public long getParticleSpawnRate(EntityLivingBase entity)
    { return 15L - entity.getRNG().nextInt(5); }

    @Override
    public void spawnParticles(EntityLivingBase entity)
    {
        double x = entity.posX + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat());
        double y = entity.posY + (entity.height / 2) + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat());
        double z = entity.posZ + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat());

        DeeperDepths.proxy.spawnParticle(7, entity.world, x, y, z, 0, 0, 0);
    }
}