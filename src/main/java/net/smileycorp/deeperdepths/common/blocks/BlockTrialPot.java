package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialPot;

import javax.annotation.Nullable;

public class BlockTrialPot extends BlockDeeperDepths implements ITileEntityProvider {
    
    public static final AxisAlignedBB AABB = new AxisAlignedBB(0.125, 0.0D, 0.125, 0.875, 1, 0.875);
    
    public static final PropertyBool CRACKED = PropertyBool.create("cracked");
    
    public BlockTrialPot() {
        super("Trial_Pot", Material.ROCK, 0, 0, 0);
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH).withProperty(CRACKED, false));
        useNeighborBrightness = true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING, CRACKED);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() |! (world.getTileEntity(pos) instanceof TileTrialPot || stack.isEmpty()))
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        TileTrialPot tile = (TileTrialPot) world.getTileEntity(pos);
        if (!tile.isItemValidForSlot(0, stack)) {
            if (!world.isRemote) world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    DeeperDepthsSoundEvents.TRIAL_POT_INSERT_FAIL, SoundCategory.BLOCKS, 1, 1);
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        }
        if (!world.isRemote) {
            if (tile.isEmpty()) tile.setInventorySlotContents(0, stack.splitStack(1));
            else {
                tile.getStackInSlot(0).grow(1);
                stack.shrink(1);
            }
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    DeeperDepthsSoundEvents.TRIAL_POT_INSERT, SoundCategory.BLOCKS, 1, 1);
        }
        return true;
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!world.isRemote) {
            if ((stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemHoe
                    || stack.getItem() instanceof ItemSword) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) < 1)
                state = state.withProperty(CRACKED, true);
        }
        super.harvestBlock(world, player, pos, state, te, stack);
    }
    
    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(CRACKED) ? DeeperDepthsSoundTypes.CRACKED_TRIAL_POT : DeeperDepthsSoundTypes.TRIAL_POT;
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
       if (state.getValue(CRACKED)) {
           TileEntity te = world.getTileEntity(pos);
           drops.add(new ItemStack(Items.BRICK, 4));
           if (te instanceof TileTrialPot &! ((TileTrialPot)te).isEmpty()) drops.add(((TileTrialPot) te).getStackInSlot(0));
           return;
       }
       super.getDrops(drops, world, pos, state, fortune);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof IProjectile) {
            world.setBlockState(pos, state.withProperty(CRACKED, true), 3);
            world.destroyBlock(pos, true);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta % 4))
                .withProperty(CRACKED, meta >= 4);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockHorizontal.FACING).getHorizontalIndex() + (state.getValue(CRACKED) ? 4 : 0);
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTrialPot();
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }
    
    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
        world.removeTileEntity(pos);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {}
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(BlockHorizontal.FACING)));
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
