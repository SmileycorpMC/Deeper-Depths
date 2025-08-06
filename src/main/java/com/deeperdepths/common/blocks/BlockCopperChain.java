package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.TextUtils;

public class BlockCopperChain extends BlockRotatedPillar implements ICopperBlock, IBlockProperties
{
    private final EnumWeatherStage stage;
    private final boolean waxed;

    protected static final AxisAlignedBB CHAIN_AABB_Y = new AxisAlignedBB(0.6D, 0.0D, 0.6D, 0.4D, 1.0D, 0.4D);
    protected static final AxisAlignedBB CHAIN_AABB_X = new AxisAlignedBB(0.0D, 0.4D, 0.6D, 1.0D, 0.6D, 0.4D);
    protected static final AxisAlignedBB CHAIN_AABB_Z = new AxisAlignedBB(0.6D, 0.4D, 0.0D, 0.4D, 0.6D, 1.0D);

    public BlockCopperChain(EnumWeatherStage stage, boolean waxed)
    {
        super(Material.ROCK);
        String name = "Copper_Chain";
        if (stage != EnumWeatherStage.NORMAL) name = TextUtils.toProperCase(stage.getName()) + "_" + name;
        if (waxed) name = "Waxed_" + name;
        this.stage = stage;
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
    public boolean isTopSolid(IBlockState state) {
        return false;
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
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();
        switch (meta)
        {
            case 1:
                state = state.withProperty(AXIS, EnumFacing.Axis.X);
                break;
            case 2:
                state = state.withProperty(AXIS, EnumFacing.Axis.Z);
                break;
            default:
                state = state.withProperty(AXIS, EnumFacing.Axis.Y);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(AXIS)) {
            case X:
                return 1;
            case Y:
                return 0;
            case Z:
                return 2;
            default:
                return 3;
        }
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /** Same shape as a fence if it is oriented straight up */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    { return state.getValue(AXIS) == EnumFacing.Axis.Y ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED; }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    { return false; }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isWaxed(IBlockState state) {
        return waxed;
    }

    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return stage;
    }

    @Override
    public IBlockState getScraped(IBlockState state)
    { return copyProperties(state, DeeperDepthsBlocks.COPPER_CHAINS.get(waxed ? stage : stage.previous()).getDefaultState()); }

    private IBlockState copyProperties(IBlockState oldState, IBlockState newState)
    { return newState.withProperty(AXIS, oldState.getValue(AXIS)); }

    @Override
    public IBlockState getWaxed(IBlockState state)
    { return waxed ? state : copyProperties(state, DeeperDepthsBlocks.WAXED_COPPER_CHAINS.get(stage).getDefaultState()); }

    @Override
    public IBlockState getWeathered(IBlockState state)
    { return waxed || stage == EnumWeatherStage.OXIDIZED ? state : copyProperties(state, DeeperDepthsBlocks.COPPER_CHAINS.get(stage.next()).getDefaultState()); }

    @Override
    public boolean isEdible(ItemStack stack)
    {
        if (!Constants.FUNNY &! BlockConfig.tastyCopper) return false;
        return stage != EnumWeatherStage.NORMAL;
    }

    @Override
    public ItemStack getPrevious(ItemStack stack)
    {
        ItemStack stack1 = new ItemStack((waxed ? DeeperDepthsBlocks.WAXED_COPPER_CHAINS : DeeperDepthsBlocks.COPPER_CHAINS).get(stage.previous()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getScraped(ItemStack stack)
    {
        if (!canScrape(stack)) return stack;
        ItemStack stack1 = new ItemStack(waxed ? DeeperDepthsBlocks.COPPER_CHAINS.get(stage) : DeeperDepthsBlocks.COPPER_CHAINS.get(stage.previous()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getWaxed(ItemStack stack) {
        if (isWaxed(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_CHAINS.get(stage), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getWeathered(ItemStack stack)
    {
        if (!canWeather(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.COPPER_CHAINS.get(stage.next()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public boolean canWax(ItemStack stack) {
        return !waxed;
    }

    @Override
    public boolean canScrape(ItemStack stack) {
        return waxed || stage != EnumWeatherStage.NORMAL;
    }

    @Override
    public boolean canWeather(ItemStack stack) {
        return !isWaxed(stack) && stage != EnumWeatherStage.OXIDIZED;
    }

    @Override
    public boolean isWaxed(ItemStack stack) {
        return waxed;
    }
}
