package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.deeperdepths.common.capabilities.LightingRods;

import java.util.Random;

public class BlockLightningRod extends BlockDeeperDepths {
    
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    
    protected static final AxisAlignedBB[] AABBS = {new AxisAlignedBB(0.0, 0.375, 0.375, 1.0, 0.625, 0.625),
        new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625), new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 1.0)};
    
    public BlockLightningRod() {
        super("lightning_rod", Material.IRON, 3, 6, 0);
        setDefaultState(getBlockState().getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.UP).withProperty(POWERED, false));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockDirectional.FACING, POWERED);
    }
    
    //ACDC reference
    public void struck(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(POWERED, true), 3);
        world.notifyNeighborsOfStateChange(pos.offset(state.getValue(BlockDirectional.FACING)), this, false);
        world.scheduleUpdate(pos, this, 8);
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        world.setBlockState(pos, state.withProperty(POWERED, false), 3);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (world instanceof WorldServer) LightingRods.get((WorldServer) world).add(pos);
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world instanceof WorldServer) LightingRods.get((WorldServer) world).remove(pos);
    }
    
    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return state.getWeakPower(world, pos, side);
    }
    
    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
       return state.getValue(POWERED) ? 15 : 0;
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWERED, meta % 2 == 1)
                .withProperty(BlockDirectional.FACING, EnumFacing.values()[(meta/2) % 6]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDirectional.FACING).ordinal() * 2 + (state.getValue(POWERED) ? 1 : 0);
    }
    
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABBS[state.getValue(BlockDirectional.FACING).getAxis().ordinal()];
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float lookX, float lookY, float lookZ, int meta, EntityLivingBase player) {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }
    
}
