package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;

public class BlockCandle extends BlockDeeperDepths {
    
    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyInteger CANDLES = PropertyInteger.create("candles", 1, 4);
    private static final AxisAlignedBB[] AABBS = {new AxisAlignedBB(0.4375, 0, 0.4375, 0.5625, 0.375, 0.5625),
            new AxisAlignedBB(0.3125, 0, 0.375, 0.6875, 0.375, 0.5625),
            new AxisAlignedBB(0.3125, 0, 0.375, 0.625, 0.375, 0.6875),
            new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.375, 0.625)};
    
    private final EnumDyeColor color;
    
    public BlockCandle(EnumDyeColor color) {
        super(color == null ? "Candle" : color.getName() + "_Candle", Material.CARPET, 0.1f, 0.1f, 0);
        this.color = color;
        setSoundType(DeeperDepthsSoundTypes.CANDLE);
        setDefaultState(getBlockState().getBaseState().withProperty(LIT, false).withProperty(CANDLES, 1));
    }
    
    public static void light(World world, IBlockState state, BlockPos pos) {
        world.setBlockState(pos, state.withProperty(LIT, true), 3);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CANDLES, LIT);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isSneaking() && stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() == state.getBlock())
        {
            int candles = state.getValue(CANDLES);
            if (candles >= 4) return false;
            player.swingArm(hand);
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, blockSoundType.getPlaceSound(),
                    SoundCategory.BLOCKS, (blockSoundType.getVolume() + 1f) / 2f, blockSoundType.getPitch() * 0.8F);
            world.setBlockState(pos, state.withProperty(CANDLES, candles + 1), 3);
            if (!player.isCreative()) stack.shrink(1);
            return true;
        }
        if (!player.isSneaking() && state.getValue(LIT))
        {
            player.swingArm(hand);
            if (!world.isRemote)
            {
                world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, DeeperDepthsSoundEvents.CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
                world.setBlockState(pos, state.withProperty(LIT, false), 3);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolid(world, pos, EnumFacing.UP);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!world.getBlockState(pos.down()).isSideSolid(world, pos, EnumFacing.UP)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
        super.neighborChanged(state, world, pos, block, other);
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(state.getBlock(), state.getValue(CANDLES)));
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(state.getBlock());
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return color == null ? MapColor.SAND : MapColor.getBlockColor(color);
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? state.getValue(CANDLES) * 3 : 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LIT, meta > 3).withProperty(CANDLES,(meta % 4) + 1);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    { return (state.getValue(LIT) ? 4 : 0) + state.getValue(CANDLES) - 1; }
    
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABBS[state.getValue(CANDLES) - 1];
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
    public String byMeta(int meta) {
        return "inventory";
    }
    
}