package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockUtils;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumClusterSize;
import net.smileycorp.deeperdepths.common.items.DeeperDepthsItems;

import java.util.Random;

public class BlockAmethystBud extends BlockAmethyst {
    
    private final EnumClusterSize size;
    
    public BlockAmethystBud(EnumClusterSize size) {
        super(size == EnumClusterSize.CLUSTER ? "amethyst_cluster" : size.getName() + "_amethyst_bud");
        this.size = size;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockDirectional.FACING);
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (size == EnumClusterSize.CLUSTER) drops.add(new ItemStack(DeeperDepthsItems.MATERIALS, harvesters.get() == null ? 2 :
                BlockUtils.getFortune(fortune, 4, new Random()), 1));
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
       return true;
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return size.getLight();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.values()[meta % 6]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDirectional.FACING).ordinal();
    }
    
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return size.getAABB(state.getValue(BlockDirectional.FACING));
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
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
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos.offset(side.getOpposite()));
        return state.isSideSolid(world, pos, side);
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float lookX, float lookY, float lookZ, int meta, EntityLivingBase player) {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!canPlaceBlockOnSide(world, pos, state.getValue(BlockDirectional.FACING))) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
        super.neighborChanged(state, world, pos, block, other);
    }
    
    @Override
    public String byMeta(int meta) {
        return "inventory";
    }
    
    public boolean canGrow() {
        return size != EnumClusterSize.CLUSTER;
    }
    
    public void grow(World world, IBlockState state, BlockPos pos) {
        world.setBlockState(pos, DeeperDepthsBlocks.AMETHYST_BUDS.get(size.next()).getDefaultState()
                .withProperty(BlockDirectional.FACING, state.getValue(BlockDirectional.FACING)));
    }
    
}
