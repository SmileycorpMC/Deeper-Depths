package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCopperBulb extends BlockDeeperDepths implements ICopperBlock  {
    
    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private final boolean waxed;
    
    public BlockCopperBulb(boolean waxed) {
        super((waxed ? "Waxed_" : "") + "Copper_Bulb", Material.IRON, BlockConfig.copper.getHardness(), BlockConfig.copper.getResistance(), BlockConfig.copper.getHarvestLevel());
        setDefaultState(getBlockState().getBaseState().withProperty(WEATHER_STAGE, EnumWeatherStage.NORMAL)
                .withProperty(LIT, false).withProperty(POWERED, false));
        setSoundType(DeeperDepthsSoundTypes.COPPER_BULB);
        this.waxed = waxed;
        needsRandomTick = BlockConfig.copperAges &! waxed;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WEATHER_STAGE, LIT, POWERED);
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(placer.getHeldItem(hand).getMetadata());
    }
    
    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        tryWeather(world, pos, state, random);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        checkPower(state, world, pos);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return;
        checkPower(state, world, pos);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(WEATHER_STAGE).getMapColor();
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WEATHER_STAGE).ordinal() + (state.getValue(LIT) ? 4 : 0) + (state.getValue(POWERED) ? 8 : 0);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 4;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WEATHER_STAGE, EnumWeatherStage.values()[meta % 4])
                .withProperty(LIT, meta % 8 >= 4).withProperty(POWERED, meta >= 8);
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 4; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return 4;
    }
    
    @Override
    public String byMeta(int meta) {
        StringBuilder builder = new StringBuilder();
        if (waxed) builder.append("waxed_");
        if (meta % 4 > 0) builder.append(EnumWeatherStage.values()[meta % 4].getName() + "_");
        if (meta > 8) builder.append("lit_");
        return builder + "copper_bulb";
    }
    
    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (!state.getValue(LIT)) return 0;
        switch (state.getValue(WEATHER_STAGE)) {
            case EXPOSED:
                return 12;
            case WEATHERED:
                return 8;
            case OXIDIZED:
                return 4;
            default:
                return 15;
        }
    }
    
    public void checkPower(IBlockState state, World world, BlockPos pos) {
        if (world.isRemote) return;
        if (world.isBlockPowered(pos) == state.getValue(POWERED)) return;
        if (state.getValue(POWERED)) world.setBlockState(pos, state.withProperty(POWERED, false), 3);
        else {
            world.setBlockState(pos, state.withProperty(LIT, !state.getValue(LIT)).withProperty(POWERED, true), 3);
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    state.getValue(LIT) ? DeeperDepthsSoundEvents.COPPER_BULB_TURN_ON: DeeperDepthsSoundEvents.COPPER_BULB_TURN_OFF,
                    SoundCategory.BLOCKS, 1, 1);
        }
    }
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }
    
    public boolean isWaxed() {
        return waxed;
    }
    
    @Override
    public boolean isWaxed(IBlockState state) {
        return waxed;
    }
    
    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return state.getValue(WEATHER_STAGE);
    }
    
    @Override
    public IBlockState getScraped(IBlockState state) {
        return waxed ? DeeperDepthsBlocks.COPPER_BULB.getDefaultState().withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE))
                .withProperty(LIT, state.getValue(LIT)).withProperty(POWERED, state.getValue(POWERED))
                : state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).previous());
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return DeeperDepthsBlocks.WAXED_COPPER_BULB.getDefaultState().withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE))
                .withProperty(LIT, state.getValue(LIT)).withProperty(POWERED, state.getValue(POWERED));
    }

    @Override
    public IBlockState getWeathered(IBlockState state) {
        return state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).next());
    }
    
    @Override
    public boolean canWax(ItemStack stack) {
        return !waxed;
    }
    
    @Override
    public boolean canScrape(ItemStack stack) {
        return waxed || stack.getMetadata() > 0;
    }
    
    @Override
    public ItemStack getWaxed(ItemStack stack) {
        if (waxed) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_BULB, 1, stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }
    
    @Override
    public ItemStack getScraped(ItemStack stack) {
        if (!canScrape(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.COPPER_BULB, 1, (waxed ? stack.getMetadata()
                : stack.getMetadata() - 1));
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }
    
    @Override
    public boolean isWaxed(ItemStack stack) {
        return waxed;
    }
    
}
