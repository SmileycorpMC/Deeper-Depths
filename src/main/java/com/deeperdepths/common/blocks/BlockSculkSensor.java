package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.tiles.TileSculkSensor;
import com.deeperdepths.config.BlockConfig;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.DirectionUtils;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSculkSensor extends BlockSculk implements ITileEntityProvider, IFluidloggable {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);

    public BlockSculkSensor(String name) {
        super(name, BlockConfig.sculkSensor);
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
        return BlockConfig.sculkSensorXpDrop;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSculkSensor(BlockConfig.sculkSensorCooldown);
    }
    
    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        super.onEntityWalk(world, pos, entity);
        if (world.isRemote) return;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSculkSensor) ((TileSculkSensor) tile).reactToSound(entity, DeeperDepthsSoundEvents.SCULK_SENSOR_STEP, pos.getX(), pos.getY(), pos.getZ());
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!state.getValue(ACTIVE)) return;
        EnumFacing facing = DirectionUtils.getRandomDirectionXZ(rand);
        DeeperDepths.proxy.spawnParticle(10, pos.getX() + 0.5 + (facing.getFrontOffsetX() == 0 ? rand.nextFloat() - 0.5 : 0.6 * facing.getFrontOffsetX()),
                pos.getY() + 0.25, pos.getZ() + 0.5 + (facing.getFrontOffsetZ() == 0 ? rand.nextFloat() - 0.5 : 0.6 * facing.getFrontOffsetZ()),
        0d, rand.nextFloat() * 0.04, 0d);
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

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}
