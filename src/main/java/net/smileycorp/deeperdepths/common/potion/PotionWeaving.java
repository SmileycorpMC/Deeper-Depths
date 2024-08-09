package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Weaving Effect.
 * Requires Particles, and a Mixin for Weaving allowing slightly faster web movement.
 *
 * Mixin should hook to Entity.class, at about Line 569, altering the values of 569 and 571 to 0.5 if the entity has Weaving on them. Maybe goto different web movement if Weaving is detected?
 */
@Mod.EventBusSubscriber
public class PotionWeaving extends PotionDeeperDepths
{
    /** The entity to spawn. */
    private static Block block;
    /** The min amount of blocks placed. */
    private static int minPlace;
    /** The max amount of blocks placed. */
    private static int maxPlace;
    /** How far to place blocks. */
    private static int blockRange;

    protected PotionWeaving(String nameIn, boolean isBadEffectIn, int liquidColorIn, Block blockIn, int minPlaceIn, int maxPlaceIn, int blockSpreadIn)
    {
        super(nameIn, isBadEffectIn, liquidColorIn, 5);
        block = blockIn;
        minPlace = minPlaceIn;
        maxPlace = maxPlaceIn;
        blockRange = blockSpreadIn;
    }

    /** Uses Forge's `LivingDeathEvent`, as repeatedly scanning if an Entity is dead is silly. */
    @SubscribeEvent
    public static void onWeavingDeathEvent(LivingDeathEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        /* Currently hard-coded against Silverfish, make configurable later. */
        if (!entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.WEAVING);

        if (!entity.isPotionActive(DeeperDepthsPotions.WEAVING)) return;

        if(!entity.world.isRemote)
        {
            List<BlockPos> validPositions = new ArrayList<>();

            BlockPos pos = entity.getPosition();

            /* First gets all acceptable blocks in the defined range around the entity. */
            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-blockRange, -blockRange, -blockRange), pos.add(blockRange, blockRange, blockRange)))
            {
                /* Needs Air, and a solid surface below. */
                if (entity.world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.AIR && entity.world.getBlockState(blockpos$mutableblockpos.down()).isSideSolid(entity.world, blockpos$mutableblockpos.down(), EnumFacing.UP))
                {
                    validPositions.add(blockpos$mutableblockpos.toImmutable());
                }
            }
            /* Shuffles the list of valid blocks about, to and fro. */
            Collections.shuffle(validPositions);

            /* Places the webs. */
            for (int i = 0; i < Math.min(entity.getRNG().nextInt(maxPlace - minPlace + 1) + minPlace, validPositions.size()); i++)
            {
                BlockPos webPos = validPositions.get(i);
                entity.world.setBlockState(webPos, block.getDefaultState());
            }
        }
    }

    @Override
    public void spawnParticles(EntityLivingBase entity)
    { ((WorldServer)entity.world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX, entity.posY + (entity.height / 2), entity.posZ, 1, entity.width/3, entity.height/2, entity.width/3, 0.0, Block.getStateId(block.getDefaultState())); }
}