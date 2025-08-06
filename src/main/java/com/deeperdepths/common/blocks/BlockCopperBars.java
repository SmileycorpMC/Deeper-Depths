package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.TextUtils;

import java.util.Random;

public class BlockCopperBars extends BlockPane implements ICopperBlock, IBlockProperties {

    private final EnumWeatherStage stage;
    private final boolean waxed;

    protected BlockCopperBars(EnumWeatherStage stage, boolean waxed) {
        super(Material.IRON, true);
        String name = "Copper_Bars";
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
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        tryWeather(world, pos, state, random);
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
    public IBlockState getScraped(IBlockState state) {
        return copyProperties(state, DeeperDepthsBlocks.LIGHTNING_RODS.get(waxed ? stage : stage.previous()).getDefaultState());
    }

    private IBlockState copyProperties(IBlockState oldState, IBlockState newState) {
        return newState.withProperty(BlockDirectional.FACING, oldState.getValue(BlockDirectional.FACING));
    }

    @Override
    public IBlockState getWaxed(IBlockState state) {
        return waxed ? state : copyProperties(state, DeeperDepthsBlocks.WAXED_LIGHTNING_RODS.get(stage).getDefaultState());
    }

    @Override
    public IBlockState getWeathered(IBlockState state) {
        return waxed || stage == EnumWeatherStage.OXIDIZED ? state : copyProperties(state, DeeperDepthsBlocks.LIGHTNING_RODS.get(stage.next()).getDefaultState());
    }

    @Override
    public boolean isEdible(ItemStack stack) {
        //💀
        if (!Constants.FUNNY &! BlockConfig.tastyCopper) return false;
        return stage != EnumWeatherStage.NORMAL;
    }

    @Override
    public ItemStack getPrevious(ItemStack stack) {
        ItemStack stack1 = new ItemStack((waxed ? DeeperDepthsBlocks.WAXED_LIGHTNING_RODS : DeeperDepthsBlocks.LIGHTNING_RODS)
                .get(stage.previous()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getScraped(ItemStack stack) {
        if (!canScrape(stack)) return stack;
        ItemStack stack1 = new ItemStack(waxed ? DeeperDepthsBlocks.LIGHTNING_RODS.get(stage)
                : DeeperDepthsBlocks.LIGHTNING_RODS.get(stage.previous()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getWaxed(ItemStack stack) {
        if (isWaxed(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.WAXED_LIGHTNING_RODS.get(stage), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getWeathered(ItemStack stack) {
        if (!canWeather(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.LIGHTNING_RODS.get(stage.next()), stack.getCount(), stack.getMetadata());
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

    @Override
    public boolean usesCustomItemHandler(){
        return true;
    }

}
