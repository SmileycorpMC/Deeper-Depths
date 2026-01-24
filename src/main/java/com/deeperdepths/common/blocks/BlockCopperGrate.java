package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCopperGrate extends BlockCopper implements IFluidloggable {

    public BlockCopperGrate() {
        super("Copper_Grate");
        setSoundType(DeeperDepthsSoundTypes.COPPER_GRATE);
        setDefaultState(getBlockState().getBaseState().withProperty(WATERLOGGED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WEATHER_STAGE, WAXED, WATERLOGGED);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        IBlockState replaced = world.getBlockState(pos);
        if (replaced.getBlock() != Blocks.WATER) return state;
        if (replaced.getValue(BlockLiquid.LEVEL) != 0) return state;
        return state.withProperty(WATERLOGGED, world.getBlockState(pos).getBlock() == Blocks.WATER);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return super.getMetaFromState(state) + (state.getValue(WATERLOGGED) ? 8 : 0);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state) % 8;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WEATHER_STAGE, EnumWeatherStage.values()[meta % 4])
                .withProperty(WAXED, meta % 8 >= 4).withProperty(WATERLOGGED, meta >= 8);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (world.getBlockState(pos) != Blocks.AIR) return;
        if (state.getValue(WATERLOGGED)) world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
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
    public String byState(IBlockState state) {
        return byMeta(getMetaFromState(state) % getMaxMeta());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return world.getBlockState(pos.offset(facing)).getBlock() != this && super.shouldSideBeRendered(state, world, pos, facing);
    }

}
