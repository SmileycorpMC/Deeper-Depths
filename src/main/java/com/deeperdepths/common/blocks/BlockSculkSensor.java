package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.tiles.TileSculkSensor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
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

public class BlockSculkSensor extends BlockSculk implements ITileEntityProvider, IFluidloggable {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);

    public BlockSculkSensor(String name) {
        super(name, 1.5f, 1.5f);
        setSoundType(DeeperDepthsSoundTypes.SCULK_SENSOR);
        setLightLevel(0.125f);
        setDefaultState(getBlockState().getBaseState().withProperty(WATERLOGGED, false).withProperty(ACTIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WATERLOGGED, ACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(WATERLOGGED) ? 1 : 0) + (state.getValue(ACTIVE) ? 2 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WATERLOGGED, (meta & 1) != 0).withProperty(ACTIVE, (meta & 2) != 0);
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSculkSensor();
    }
    
    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        super.onEntityWalk(world, pos, entity);
        if (world.isRemote) return;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSculkSensor) {
            TileSculkSensor sensor = (TileSculkSensor) tile;
            if (sensor.canHear(DeeperDepthsSoundEvents.SCULK_SENSOR_STEP, pos.getX(), pos.getY(), pos.getZ()))
                sensor.reactToSound(DeeperDepthsSoundEvents.SCULK_SENSOR_STEP, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (!state.getValue(ACTIVE)) return 0;
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileSculkSensor ? ((TileSculkSensor) tile).getPower() : 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? getWeakPower(state, world, pos, side) : 0;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        if (!state.getValue(ACTIVE)) return 0;
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileSculkSensor ? ((TileSculkSensor) tile).getFrequency() : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
