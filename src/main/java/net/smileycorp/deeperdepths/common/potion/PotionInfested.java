package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Objects;

/**
 * The Infested Effect.
 * Requires Particles.
 */
@Mod.EventBusSubscriber
public class PotionInfested extends PotionDeeperDepths
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
        super(nameIn, isBadEffectIn, liquidColorIn, 3);
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

    @Override
    public void spawnParticles(EntityLivingBase entity)
    { ((WorldServer)entity.world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX, entity.posY + (entity.height / 2), entity.posZ, 1, entity.width/3, entity.height/2, entity.width/3, 0.0, Block.getStateId(Blocks.STONE.getDefaultState())); }
}