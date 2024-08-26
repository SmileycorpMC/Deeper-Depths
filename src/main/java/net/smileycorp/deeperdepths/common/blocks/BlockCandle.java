package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;
import net.smileycorp.deeperdepths.config.BlockConfig;

import java.util.Random;

public class BlockCandle extends BlockDeeperDepths {
    
    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyInteger CANDLES = PropertyInteger.create("candles", 1, 4);
    private static final AxisAlignedBB[] AABBS = {new AxisAlignedBB(0.4375, 0, 0.4375, 0.5625, 0.375, 0.5625),
            new AxisAlignedBB(0.3125, 0, 0.375, 0.6875, 0.375, 0.5625),
            new AxisAlignedBB(0.3125, 0, 0.375, 0.625, 0.375, 0.6875),
            new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.375, 0.625)};

    /** MULTIDIMENSIONAL WICK ARRAY. */
    private Vec3d[][] wickSpot = new Vec3d[][]
            {
                    new Vec3d[] {new Vec3d(0.5, 0.5, 0.5)},
                    new Vec3d[] {new Vec3d(0.375, 0.4375, 0.5), new Vec3d(0.625, 0.5, 0.4375)},
                    new Vec3d[] {new Vec3d(0.375, 0.4375, 0.5), new Vec3d(0.5625, 0.5, 0.4375), new Vec3d(0.5, 0.3125, 0.625)},
                    new Vec3d[] {new Vec3d(0.375, 0.4375, 0.375), new Vec3d(0.5625, 0.5, 0.375), new Vec3d(0.4375, 0.3125, 0.5625), new Vec3d(0.625, 0.4375, 0.5625)}
            };
    //no way wick, like john wick fortnite?
    
    private final EnumDyeColor color;
    
    public BlockCandle(EnumDyeColor color) {
        //why is it called silver in the enum when it's called light gray everywhere else
        super(color == null ? "Candle" : (color == EnumDyeColor.SILVER ? "light_gray" : color.getName()) + "_Candle", Material.CARPET, BlockConfig.candle.getHardness(), BlockConfig.candle.getResistance(), BlockConfig.candle.getHarvestLevel());
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

        IBlockState state = world.getBlockState(pos.down());
        return checkSpecialPlacementBlock(state, world, pos);
    }

    /** Bedrock feature, Burning Mobs ignite Candles when touching them. */
    public boolean checkSpecialPlacementBlock(IBlockState state, World world, BlockPos pos)
    {
        /* Deal with Shulker boxes immediately, `getBlockFaceShape` causes a crash if it checks the Shulker */
        if (state.getBlock() instanceof BlockShulkerBox) return true;
        if (state.getBlock() instanceof BlockAnvil) return true;
        if (state.getBlock() instanceof BlockHopper) return false;
        if (state.getBlock() instanceof BlockLeaves) return false;
        if (state.getBlock().canPlaceTorchOnTop(state, world, pos)) return true;
        if (state.getBlock() instanceof BlockEndRod && state.getValue(BlockEndRod.FACING) == EnumFacing.UP) return true;
        BlockFaceShape shape = state.getBlockFaceShape(world, pos, EnumFacing.UP);
        if (shape != BlockFaceShape.BOWL && shape != BlockFaceShape.UNDEFINED) return true;

        return false;
    }

    /** Bedrock feature, Burning Mobs ignite Candles when touching them. */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn.isBurning()) light(worldIn, worldIn.getBlockState(pos), pos);
        super.onEntityWalk(worldIn, pos, entityIn);
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
        /* Skip if not lit. */
        if (!state.getValue(LIT)) return;
        int candles = state.getValue(CANDLES);

        for (int c = 0; c <= candles - 1; c++)
        {
            if (rand.nextInt(4)==0) world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + wickSpot[candles - 1][c].x, pos.getY() + wickSpot[candles - 1][c].y, pos.getZ() + wickSpot[candles - 1][c].z, 0.0D, 0.0D, 0.0D);
            //world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + wickSpot[candles - 1][c].x, pos.getY() + wickSpot[candles - 1][c].y, pos.getZ() + wickSpot[candles - 1][c].z, 0.0D, 0.0D, 0.0D);
            DeeperDepths.proxy.spawnParticle(3, world, pos.getX() + wickSpot[candles - 1][c].x, pos.getY() + wickSpot[candles - 1][c].y, pos.getZ() + wickSpot[candles - 1][c].z, 0.0D, 0.0D, 0.0D, 0, 8);
        }
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
