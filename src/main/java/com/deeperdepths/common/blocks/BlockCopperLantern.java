package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockCopperLantern extends BlockDeeperDepths implements ICopperBlock
{
    public static final PropertyBool HANGING = PropertyBool.create("hanging");
    public static final PropertyBool WAXED = PropertyBool.create("waxed");
    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.6D, 0.7D);

    public BlockCopperLantern()
    {
        super("copper_lantern", Material.ROCK, BlockConfig.copper.getHardness(), BlockConfig.copper.getResistance(), BlockConfig.copper.getHarvestLevel());
        setSoundType(DeeperDepthsSoundTypes.LANTERN);
        needsRandomTick = BlockConfig.copperAges;
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        tryWeather(world, pos, state, random);
    }

    @Override
    public String byMeta(int meta)
    {
        StringBuilder builder = new StringBuilder();
        if ((meta & (1 << 2)) != 0) builder.append("waxed_");
        if ((meta & 3) != 0) builder.append(EnumWeatherStage.values()[meta & 3].getName() + "_");
        return builder + "copper_lantern";
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    { return isValidBlock(worldIn, pos, worldIn.getBlockState(pos.up()), true) || isValidBlock(worldIn, pos, worldIn.getBlockState(pos.down()), false); }

    @Deprecated
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return BlockFaceShape.UNDEFINED; }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_AABB;
    }

    @Deprecated
    public boolean isTopSolid(IBlockState state)
    {
        return false;
    }

    /**
     * If a Lantern can be placed against this block.
     *
     * `hanging` bool defines which faces to check when it comes to shape
     * */
    public boolean isValidBlock(World worldIn, BlockPos pos, IBlockState state, boolean hanging)
    {
        if (state.getBlock() instanceof BlockShulkerBox) return true;
        if (state.getBlock() instanceof BlockAnvil) return true;
        if (state.getBlock().canPlaceTorchOnTop(state, worldIn, pos)) return true;
        BlockFaceShape shape = state.getBlockFaceShape(worldIn, pos, hanging ? EnumFacing.DOWN : EnumFacing.UP);
        if (shape != BlockFaceShape.BOWL && shape != BlockFaceShape.UNDEFINED) return true;
        return false;
    }

    @Deprecated
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    { return getStateFromMeta(placer.getHeldItem(hand).getMetadata()).withProperty(HANGING, facing == EnumFacing.DOWN); }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    { return state.getValue(HANGING) ? isValidBlock(worldIn, pos, worldIn.getBlockState(pos.up()), true) : isValidBlock(worldIn, pos, worldIn.getBlockState(pos.down()), false); }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 4;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items)
    { for (int i = 0; i < 8; i++) items.add(new ItemStack(this, 1, i)); }

    @Override
    public int getMaxMeta()
    { return 8; }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, HANGING, WAXED, WEATHER_STAGE); }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT;  }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta |= state.getValue(WEATHER_STAGE).ordinal();
        if (state.getValue(WAXED)) meta |= (1 << 2);
        if (state.getValue(HANGING))  meta |= (1 << 3);
        return meta;
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumWeatherStage stage = EnumWeatherStage.values()[meta & 3];

        boolean waxed = (meta & (1 << 2)) != 0;
        boolean hanging = (meta & (1 << 3)) != 0;

        return this.getDefaultState().withProperty(WEATHER_STAGE, stage).withProperty(WAXED, waxed).withProperty(HANGING, hanging);
    }


    @Override
    public boolean isWaxed(IBlockState state) { return state.getValue(WAXED); }

    @Override
    public EnumWeatherStage getStage(IBlockState state) { return state.getValue(WEATHER_STAGE); }

    @Override
    public IBlockState getScraped(IBlockState state)
    { return isWaxed(state) ? state.withProperty(WAXED, false) : state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).previous()); }

    @Override
    public IBlockState getWaxed(IBlockState state)
    { return state.withProperty(WAXED, true); }

    @Override
    public IBlockState getWeathered(IBlockState state)
    { return state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).next()); }
}