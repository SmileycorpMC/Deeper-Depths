package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.smileycorp.deeperdepths.common.Constants;

import java.util.Objects;

/**
 * The Infested Effect.
 * Requires Particles.
 */
@Mod.EventBusSubscriber
public class PotionInfested extends Potion
{
    /** The entity to spawn. */
    private static Class spawnEntity;
    /** The chance to spawn the entity(s). */
    private static int chance;
    /** The min amount of entities to spawn. */
    private static int spawnQuantityMin;
    /** The max amount of entities to spawn. */
    private static int spawnQuantityMax;
    /** If the entities should be initialized via `onInitialSpawn`. */
    private static boolean doInitialSpawn;

    protected PotionInfested(String nameIn, boolean isBadEffectIn, int liquidColorIn, Class spawnEntityIn, int chanceIn, int spawnQuantityMinIn, int spawnQuantityMaxIn, boolean doInitialSpawnIn)
    {
        super(isBadEffectIn, liquidColorIn);
        this.setPotionName("effect." + Constants.MODID + "." + nameIn);
        this.setRegistryName(nameIn);
        spawnEntity = spawnEntityIn;
        chance = chanceIn;
        spawnQuantityMin = spawnQuantityMinIn;
        spawnQuantityMax = spawnQuantityMaxIn;
        doInitialSpawn = doInitialSpawnIn;
    }

    /** Uses Forge's `LivingHurtEvent`, as repeatedly scanning an entity's HurtTime hurts our time... it's slow. */
    @SubscribeEvent
    public static void onHurtEvent(LivingHurtEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        /* Particles will need to be fed through a Packet Handler. */
        //entity.world.spawnParticle(EnumParticleTypes.SLIME, entity.posX, entity.posY, entity.posZ, 0, 0,0, new int[0]);

        /* Currently hard-coded against Silverfish, make configurable later. */
        if (entity instanceof EntitySilverfish || !entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.INFESTED);

        if (!entity.isPotionActive(DeeperDepthsPotions.INFESTED) || entity.getRNG().nextFloat() > chance * 0.01) return;

        if(!entity.world.isRemote)
        {
            for (int i = 0; i < entity.getRNG().nextInt(spawnQuantityMax - spawnQuantityMin + 1) + spawnQuantityMin; i++)
            {
                Entity infestor = Objects.requireNonNull(EntityRegistry.getEntry(spawnEntity)).newInstance(entity.world);

                if (infestor instanceof EntityLiving && doInitialSpawn) ((EntityLiving)infestor).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);

                //if (infestor instanceof EntitySlime) setSlimeStats(oozed, 1);

                /* Spawns halfway up the effected entity, facing the same direction. */
                infestor.setLocationAndAngles(entity.posX, entity.posY + (entity.height / 2), entity.posZ, entity.rotationYaw, 0.0F);

                entity.world.spawnEntity(infestor);
            }
        }
    }

    /** We are using Forge's LivingHurtEvent, so this isn't required at all. */
    @Override
    public boolean isReady(int duration, int amplifier)
    { return false; }
}