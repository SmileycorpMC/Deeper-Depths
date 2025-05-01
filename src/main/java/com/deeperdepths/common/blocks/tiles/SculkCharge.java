package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockSculkVein;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Handles all the logic of Sculk Charges, which gets added, stored, ticked, and removed by Sculk Catalyst tiele entities.
 *
 * Needs lots of work still.
 * */
public class SculkCharge
{
    /** Later put this into config! */
    public static String[] blocksToReplace = new String[]{
            "minecraft:clay",
            "minecraft:dirt",
            "minecraft:end_stone",
            "minecraft:grass",
            "minecraft:gravel",
            "minecraft:hardened_clay",
            "minecraft:mycelium",
            "minecraft:netherrack",
            "minecraft:sand",
            "minecraft:sandstone",
            "minecraft:stone",
            "minecraft:soul_sand",
            "deeperdepths:deepslate",
            "deeperdepths:stone"
    };


    private final World world;
    private BlockPos currentPos;
    private int charge;

    public SculkCharge(World world, BlockPos startPos, int startEnergy)
    {
        this.world = world;
        this.currentPos = startPos;
        this.charge = startEnergy;
    }

    public boolean update()
    {
        if (charge <= 0) return false;

        /* Some effects... */
        spawnSurfaceParticles(world, currentPos, 2, 107, 186, 130);
        world.playSound(null, currentPos.getX() + 0.5f, currentPos.getY() + 0.5f, currentPos.getZ() + 0.5f, DeeperDepthsSoundEvents.SCULK_CHARGE, SoundCategory.BLOCKS, 0.15F, 1.0F);

        Block block = world.getBlockState(currentPos).getBlock();

        /* Replace this block if it isn't sculk! */
        if (block != DeeperDepthsBlocks.SCULK)
        {
            if (block == Blocks.AIR)
            {
                charge = 0;
                return false;
            }
            else
            {
                world.playSound(null, currentPos.getX() + 0.5f, currentPos.getY() + 0.5f, currentPos.getZ() + 0.5f, DeeperDepthsSoundEvents.SCULK_SPREAD, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(currentPos, DeeperDepthsBlocks.SCULK.getDefaultState());
                tryPlacingVeins(world, currentPos, world.rand);
                charge--;
            }
        }
        else if (world.rand.nextInt(10) == 0)
        { charge--; }

        /* Move to a new position, to repeat the loop. */
        BlockPos pos = getValidNeighborPos(world, currentPos, world.rand);
        if (pos != currentPos)
        { currentPos = pos; }

        return charge > 0;
    }


    public BlockPos getValidNeighborPos(World world, BlockPos pos, Random rand)
    {
        List<EnumFacing> list = Lists.newArrayList(EnumFacing.values());
        Collections.shuffle(list, rand);

        for (EnumFacing facing : list)
        {
            Block block = world.getBlockState(pos.offset(facing)).getBlock();

            if (block == DeeperDepthsBlocks.SCULK || ArrayUtils.contains(blocksToReplace, block.getRegistryName().toString()))
            { return pos.offset(facing); }
        }

        return pos;
    }

    /** Places Veins atop all open block faces of blocks neighboring the given position. */
    public void tryPlacingVeins(World world, BlockPos pos, Random rand)
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            for (EnumFacing outerFacing : EnumFacing.values())
            {
                BlockPos outerPos = pos.offset(facing).offset(outerFacing);

                if (world.getBlockState(pos.offset(facing)).isSideSolid(world, pos.offset(facing), outerFacing))
                {
                    IBlockState state = world.getBlockState(outerPos);

                    if (state.getBlock().isAir(state, world, outerPos))
                    { world.setBlockState(outerPos, BlockSculkVein.getBlockState(outerFacing.getOpposite())); }
                    else if (state.getBlock() instanceof BlockSculkVein && !ArrayUtils.contains(BlockSculkVein.getFacings(state), outerFacing.getOpposite()))
                    { world.setBlockState(outerPos, BlockSculkVein.getBlockState(ArrayUtils.add(BlockSculkVein.getFacings(state), outerFacing.getOpposite()))); }
                }
            }
        }
    }

    /** Spawns particles to slide on the surfaces of the Copper block. */
    private void spawnSurfaceParticles(World worldIn, BlockPos pos, int particleCount, int red, int green, int blue)
    {
        for (int i = 0; i < particleCount; i++)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                double x = pos.getX() + 0.5 + (facing.getAxis() == EnumFacing.Axis.X ? 0.55 * facing.getFrontOffsetX() : (worldIn.rand.nextDouble() - 0.5));
                double y = pos.getY() + 0.5 + (facing.getAxis() == EnumFacing.Axis.Y ? 0.55 * facing.getFrontOffsetY() : (worldIn.rand.nextDouble() - 0.5));
                double z = pos.getZ() + 0.5 + (facing.getAxis() == EnumFacing.Axis.Z ? 0.55 * facing.getFrontOffsetZ() : (worldIn.rand.nextDouble() - 0.5));

                double velocityX = (facing.getAxis() != EnumFacing.Axis.X) ? (worldIn.rand.nextDouble() - 0.5) * 0.1 : 0;
                double velocityY = (facing.getAxis() != EnumFacing.Axis.Y) ? (worldIn.rand.nextDouble() - 0.5) * 0.1 : 0;
                double velocityZ = (facing.getAxis() != EnumFacing.Axis.Z) ? (worldIn.rand.nextDouble() - 0.5) * 0.1 : 0;

                //DeeperDepths.proxy.spawnParticle(4, worldIn, x, y, z, velocityX, velocityY, velocityZ, 30, red, green, blue);
                DeeperDepths.proxy.spawnParticle(7, worldIn, x, y, z, velocityX, velocityY, velocityZ);
            }
        }
    }
}