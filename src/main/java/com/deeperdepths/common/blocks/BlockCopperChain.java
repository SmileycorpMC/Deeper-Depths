package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.TextUtils;

public class BlockCopperChain extends BlockRotatedPillar implements ICopperBlock, IBlockProperties
{
    private final boolean waxed;

    protected static final AxisAlignedBB CHAIN_AABB_Y = new AxisAlignedBB(0.6D, 0.0D, 0.6D, 0.4D, 1.0D, 0.4D);
    protected static final AxisAlignedBB CHAIN_AABB_X = new AxisAlignedBB(0.0D, 0.4D, 0.6D, 1.0D, 0.6D, 0.4D);
    protected static final AxisAlignedBB CHAIN_AABB_Z = new AxisAlignedBB(0.6D, 0.4D, 0.0D, 0.4D, 0.6D, 1.0D);

    public BlockCopperChain(boolean waxed)
    {
        super(Material.ROCK);
        String name = (waxed ? "Waxed_" : "") + "Copper_Chain";
        this.waxed = waxed;
        setResistance(BlockConfig.copper.getResistance());
        setHardness(BlockConfig.copper.getHardness());
        setHarvestLevel("pickaxe", BlockConfig.copper.getHarvestLevel());
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.COPPER);
        needsRandomTick = BlockConfig.copperAges;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(AXIS))
        {
            case X:
                return CHAIN_AABB_X;
            case Y:
                return CHAIN_AABB_Y;
            case Z:
                return CHAIN_AABB_Z;
            default:
                return CHAIN_AABB_Y;
        }
    }

    @Override
    public String byMeta(int meta)
    {
        StringBuilder builder = new StringBuilder();
        if (waxed) builder.append("waxed_");
        if ((meta & 3) != 0) builder.append(EnumWeatherStage.values()[meta & 3].getName() + "_");
        return builder + "copper_chain";
    }

    @Override
    public boolean isTopSolid(IBlockState state) { return false; }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    { return getStateFromMeta(placer.getHeldItem(hand).getMetadata()).withProperty(AXIS, facing.getAxis()); }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 4;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items)
    { for (int i = 0; i < 4; i++) items.add(new ItemStack(this, 1, i)); }

    @Override
    public int getMaxMeta()
    { return 4; }

    /** Same shape as a fence if it is oriented straight up */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return state.getValue(AXIS) == EnumFacing.Axis.Y ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT;  }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

    public IBlockState getStateFromMeta(int meta)
    {
        EnumWeatherStage stage = EnumWeatherStage.values()[meta & 3];

        int axisBits = (meta >> 2) & 3;
        EnumFacing.Axis axis;
        switch (axisBits) {
            case 0: axis = EnumFacing.Axis.Y; break;
            case 1: axis = EnumFacing.Axis.X; break;
            case 2: axis = EnumFacing.Axis.Z; break;
            default: axis = EnumFacing.Axis.Y; break;
        }

        return this.getDefaultState().withProperty(WEATHER_STAGE, stage).withProperty(AXIS, axis);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta |= state.getValue(WEATHER_STAGE).ordinal();

        switch (state.getValue(AXIS))
        {
            case X: meta |= (1 << 2); break;
            case Z: meta |= (2 << 2); break;
        }
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, WEATHER_STAGE);
    }

    public boolean isWaxed() { return waxed; }

    @Override
    public boolean isWaxed(IBlockState state) { return waxed; }

    @Override
    public EnumWeatherStage getStage(IBlockState state) { return state.getValue(WEATHER_STAGE); }

    @Override
    public IBlockState getScraped(IBlockState state)
    {
        return waxed ? DeeperDepthsBlocks.COPPER_CHAINS.getDefaultState().withProperty(AXIS, state.getValue(AXIS)).withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE)) :
                state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).previous());
    }

    private IBlockState copyProperties(IBlockState oldState, IBlockState newState)
    { return newState.withProperty(AXIS, oldState.getValue(AXIS)).withProperty(WEATHER_STAGE, oldState.getValue(WEATHER_STAGE)); }

    @Override
    public IBlockState getWaxed(IBlockState state)
    { return waxed ? state : DeeperDepthsBlocks.WAXED_COPPER_CHAINS.getDefaultState().withProperty(AXIS, state.getValue(AXIS)).withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE)); }

    @Override
    public IBlockState getWeathered(IBlockState state)
    { return state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).next()); }
}
