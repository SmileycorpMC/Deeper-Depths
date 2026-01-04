package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCopperGrate extends BlockCopper implements IFluidloggable {

    public BlockCopperGrate() {
        super("Copper_Grate");
        setSoundType(DeeperDepthsSoundTypes.COPPER_GRATE);
        //setDefaultState(getBlockState().getBaseState().withProperty(BlockFluidBase.LEVEL, 15));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WEATHER_STAGE, WAXED);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return world.getBlockState(pos.offset(facing)).getBlock() != this && super.shouldSideBeRendered(state, world, pos, facing);
    }

}
