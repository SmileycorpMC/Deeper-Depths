package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileDecoratedPot;

import javax.annotation.Nullable;

public class BlockDecoratedPot extends BlockDeeperDepths implements ITileEntityProvider {
    
    public static final PropertyBool CRACKED = PropertyBool.create("cracked");
    
    public BlockDecoratedPot() {
        super("Decorated_Pot", Material.ROCK, 0, 0, 0);
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH).withProperty(CRACKED, false));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING, CRACKED);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && world.getBlockState(pos) instanceof TileDecoratedPot &! stack.isEmpty()) {
            TileDecoratedPot tile = (TileDecoratedPot) world.getBlockState(pos);
            if (tile.isItemValidForSlot(0, stack)) {
                tile.getStackInSlot(0).grow(1);
                stack.shrink(1);
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItemMainhand();
            if ((stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemHoe
                    || stack.getItem() instanceof ItemSword) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) < 1)
                world.setBlockState(pos, state.withProperty(CRACKED, true), 3);
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
       if (state.getValue(CRACKED)) {
           TileEntity te = world.getTileEntity(pos);
           drops.add(new ItemStack(Items.BRICK, 4));
           if (te instanceof TileDecoratedPot &! ((TileDecoratedPot)te).isEmpty()) drops.add(((TileDecoratedPot) te).getStackInSlot(0));
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
        return getDefaultState().withProperty(BlockHorizontal.FACING, facing);
    }
    
    @Override
    public String getHarvestTool(IBlockState state) {
        return null;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileDecoratedPot();
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {}
    
    
}
