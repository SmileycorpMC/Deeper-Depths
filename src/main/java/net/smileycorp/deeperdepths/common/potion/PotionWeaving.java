package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Weaving Effect.
 * Requires Particles, and a Mixin for Weaving allowing slightly faster web movement.
 *
 * Mixin should hook to Entity.class, at about Line 569, altering the values of 569 and 571 to 0.5 if the entity has Weaving on them. Maybe goto different web movement if Weaving is detected?
 */
public class PotionWeaving extends PotionDeeperDepths
{
    /** The entity to spawn. */
    private final Block block;
    /** The min amount of blocks placed. */
    private final int minPlace;
    /** The max amount of blocks placed. */
    private final int maxPlace;
    /** How far to place blocks. */
    private final int blockRange;

    protected PotionWeaving(String nameIn, boolean isBadEffectIn, int liquidColorIn, Block blockIn, int minPlaceIn, int maxPlaceIn, int blockSpreadIn)
    {
        super(nameIn, isBadEffectIn, liquidColorIn, 5);
        block = blockIn;
        minPlace = minPlaceIn;
        maxPlace = maxPlaceIn;
        blockRange = blockSpreadIn;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        /* Particles will need to be fed through a Packet Handler. */
        //entity.world.spawnParticle(EnumParticleTypes.SLIME, entity.posX, entity.posY, entity.posZ, 0, 0,0, new int[0]);

        if (!entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.WEAVING);

        if(entity.isDead && !entity.world.isRemote)
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
}