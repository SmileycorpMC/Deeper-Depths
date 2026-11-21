package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockSculkVein;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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
    /** The maximum charge 1... charge can hold. Duh. */
    public static final int maxCharge = 1000;
    /** The distance the charge must be from its start position in order to place Sensors and Shriekers. */
    public static final int noSculkElementsRange = 16;

    private final BlockPos startingPos;
    private BlockPos currentPos;
    private int charge;

    public SculkCharge(BlockPos startPosIn, BlockPos currentPosIn, int startEnergy)
    {
        this.startingPos = startPosIn;
        this.currentPos = currentPosIn;
        this.charge = startEnergy;
    }

    public boolean update(World world)
    {
        if (charge <= 0) return false;
        /* Do NOT force-load chunks.*/
        if (!world.isBlockLoaded(currentPos)) return true;
        Block block = world.getBlockState(currentPos).getBlock();
        /* If the charge is within a non-replacable position that isn't sculk, end it. */
        if (block != DeeperDepthsBlocks.SCULK && !isReplaceable(block)) return false;

        /* Some effects... */
        spawnSurfaceParticles(world, currentPos, 2, 107, 186, 130);
        world.playSound(null, currentPos.getX() + 0.5f, currentPos.getY() + 0.5f, currentPos.getZ() + 0.5f, DeeperDepthsSoundEvents.SCULK_CHARGE, SoundCategory.BLOCKS, 0.15F, 1.0F);

        /* Replace this block if it isn't sculk! */
        if (block != DeeperDepthsBlocks.SCULK)
        {
            world.playSound(null, currentPos.getX() + 0.5f, currentPos.getY() + 0.5f, currentPos.getZ() + 0.5f, DeeperDepthsSoundEvents.SCULK_SPREAD, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(currentPos, DeeperDepthsBlocks.SCULK.getDefaultState());
            tryPlacingVeins(world, currentPos, world.rand);
            charge--;
        }
        else
        {
            boolean farEnoughFromStart = currentPos.distanceSq(startingPos) > noSculkElementsRange;

            if (farEnoughFromStart && world.rand.nextInt(10) == 0 && world.isAirBlock(currentPos.up()) && charge >= 10 && !hasMultipleNearbySculkElements(world, currentPos))
            {
                if (world.rand.nextInt(10) == 0) world.setBlockState(currentPos.up(), DeeperDepthsBlocks.SCULK_SHRIEKER.getDefaultState());
                else world.setBlockState(currentPos.up(), DeeperDepthsBlocks.SCULK_SENSOR.getDefaultState());

                charge -= 10;
            }
            else if (world.rand.nextInt(10) == 0) charge--;
        }

        /* Move to a new position, to repeat the loop. */
        this.currentPos = getValidNeighborPos(world, currentPos, world.rand);

        return charge > 0;
    }

    public BlockPos getValidNeighborPos(World world, BlockPos pos, Random rand)
    {
        List<EnumFacing> list = Lists.newArrayList(EnumFacing.values());
        Collections.shuffle(list, rand);

        for (EnumFacing facing : list)
        {
            BlockPos neighbor = pos.offset(facing);
            if (!world.isBlockLoaded(neighbor)) continue;

            Block block = world.getBlockState(neighbor).getBlock();
            if (block == DeeperDepthsBlocks.SCULK || isReplaceable(block))
            { return pos.offset(facing); }
        }

        return pos;
    }

    private static boolean isReplaceable(Block block)
    {
        for (String id : blocksToReplace)
        {
            /* The registry hands back air for unknown ids, which covers blocks from mods that aren't installed. */
            Block found = Block.REGISTRY.getObject(new ResourceLocation(id));
            if (found == block) return true;
        }
        return false;
    }

    /** Pretty straight forward, detects nearby Sculk Shriekers and Sensors. Should we call these elements? IDK what other name to use. s*/
    public boolean hasMultipleNearbySculkElements(World world, BlockPos centerPos)
    {
        int nearbySculkElements = 0;
        BlockPos minPos = centerPos.add(-4, -1, -4);
        BlockPos maxPos = centerPos.add(4, 1, 4);
        for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(minPos, maxPos))
        {
            Block block = world.getBlockState(pos).getBlock();
            if (block == DeeperDepthsBlocks.SCULK_SENSOR || block == DeeperDepthsBlocks.SCULK_SHRIEKER)
            {
                nearbySculkElements++;
                if (nearbySculkElements >= 2) return true;
            }
        }
        return false;
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

    public void mergeCharges(SculkCharge consumedCharge)
    {
        this.charge = Math.min(this.charge + consumedCharge.charge, maxCharge);
        consumedCharge.charge = 0;
    }

    public BlockPos getPos() { return currentPos; }

    /** No need to save the Catalyst pos, since charges get saved and loaded by said block*/
    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.merge(NBTUtil.createPosTag(currentPos));
        tag.setInteger("Charge", charge);
        return tag;
    }

    public static SculkCharge fromNBT(BlockPos catalystPos, NBTTagCompound tag)
    { return new SculkCharge(catalystPos, NBTUtil.getPosFromTag(tag), Math.min(tag.getInteger("Charge"), maxCharge)); }
}