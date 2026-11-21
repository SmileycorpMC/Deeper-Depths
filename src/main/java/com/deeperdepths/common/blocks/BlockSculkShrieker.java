package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.tiles.TileSculkShrieker;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSculkShrieker extends BlockSculk  implements ITileEntityProvider, IFluidloggable {

    public static final AxisAlignedBB SLAB_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);

    public BlockSculkShrieker()
    {
        super("sculk_shrieker", BlockConfig.sculkShrieker);
        setSoundType(DeeperDepthsSoundTypes.SCULK);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WATERLOGGED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(WATERLOGGED) ? 1 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WATERLOGGED, (meta & 1) != 0);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (world.getBlockState(pos) != Blocks.AIR) return;
        if (state.getValue(WATERLOGGED)) world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
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
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 5;
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSculkShrieker) ((TileSculkShrieker) tile).tryShriek(entityIn);
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SLAB_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSculkShrieker();
    }

}