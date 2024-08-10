package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.deeperdepths.common.entities.EntityWindCharge;

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
}