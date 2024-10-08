package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.capabilities.LightingRods;
import com.deeperdepths.config.BlockConfig;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockLightningRod extends BlockDeeperDepths {
    
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    
    protected static final AxisAlignedBB[] AABBS = {new AxisAlignedBB(0.0, 0.375, 0.375, 1.0, 0.625, 0.625),
        new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625), new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 1.0)};
    
    public BlockLightningRod() {
        super("lightning_rod", Material.IRON, BlockConfig.copper.getHardness(), BlockConfig.copper.getResistance(), BlockConfig.copper.getHarvestLevel());
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
        if (!world.canBlockSeeSky(pos) || !world.isThundering()) return;
        EnumFacing enumfacing = state.getValue(BlockDirectional.FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.2F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.2F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.2F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

        double velocityX = (double)enumfacing.getFrontOffsetX() * rand.nextGaussian();
        double velocityY = (double)enumfacing.getFrontOffsetY() * rand.nextGaussian();
        double velocityZ = (double)enumfacing.getFrontOffsetZ() * rand.nextGaussian();

        if (rand.nextInt(4)==0)  DeeperDepths.proxy.spawnParticle(4, world, d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, velocityX, velocityY, velocityZ, 3, 201, 227, 255);
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
