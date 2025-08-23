package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockHeavyCore extends BlockDeeperDepths
{
    protected static final AxisAlignedBB C_FLOOR_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);

    public BlockHeavyCore()
    {
        super("heavy_core", Material.IRON, BlockConfig.heavyCore.getHardness(), BlockConfig.heavyCore.getResistance(), BlockConfig.heavyCore.getHarvestLevel());
        setSoundType(DeeperDepthsSoundTypes.HEAVY_CORE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return C_FLOOR_AABB; }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing)
    { return BlockFaceShape.UNDEFINED; }

    @Override
    public boolean isFullCube(IBlockState state)
    { return false; }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    { return false; }
}