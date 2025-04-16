package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.TextUtils;

import java.util.Random;

public class BlockCopperTrapdoor extends BlockTrapDoor implements IBlockProperties, ICopperBlock {
    
    private final EnumWeatherStage stage;
    private final boolean waxed;
    
    public BlockCopperTrapdoor(EnumWeatherStage stage, boolean waxed) {
        super(Material.IRON);
        this.stage = stage;
        this.waxed = waxed;
        String name = "Copper_Trapdoor";
        if (stage != EnumWeatherStage.NORMAL) name = TextUtils.toProperCase(stage.getName()) + "_" + name;
        if (waxed) name = "Waxed_" + name;
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.COPPER);
        setHardness(BlockConfig.copper.getHardness());
        setResistance(BlockConfig.copper.getResistance());
        setHarvestLevel("pickaxe", BlockConfig.copper.getHarvestLevel());
        useNeighborBrightness = true;
        needsRandomTick = BlockConfig.copperAges &! waxed && stage != EnumWeatherStage.OXIDIZED;
    }
    
    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        tryWeather(world, pos, state, random);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        state = state.cycleProperty(OPEN);
        world.setBlockState(pos, state, 2);
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                state.getValue(OPEN) ? DeeperDepthsSoundEvents.COPPER_TRAPDOOR_OPEN : DeeperDepthsSoundEvents.COPPER_TRAPDOOR_CLOSE,
                SoundCategory.BLOCKS, 1, 1);
        return true;
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
        return copyProperties(state, DeeperDepthsBlocks.COPPER_TRAPDOORS.get(waxed ? stage : stage.previous()).getDefaultState());
    }
    
    private IBlockState copyProperties(IBlockState oldState, IBlockState newState) {
        return newState.withProperty(FACING, oldState.getValue(FACING)).withProperty(HALF, oldState.getValue(HALF))
                .withProperty(OPEN, oldState.getValue(OPEN));
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return waxed ? state : copyProperties(state, DeeperDepthsBlocks.WAXED_COPPER_TRAPDOORS.get(stage).getDefaultState());
    }
    
    @Override
    public IBlockState getWeathered(IBlockState state) {
        return waxed || stage == EnumWeatherStage.OXIDIZED ? state : copyProperties(state, DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage.next()).getDefaultState());
    }
    
    @Override
    public boolean interactRequiresSneak() {
        return true;
    }
    
    @Override
    public boolean isEdible(ItemStack stack) {
        return stage != EnumWeatherStage.NORMAL;
    }
    
    @Override
    public ItemStack getPrevious(ItemStack stack) {
        ItemStack stack1 = new ItemStack((waxed ? DeeperDepthsBlocks.WAXED_CUT_COPPER_STAIRS : DeeperDepthsBlocks.CUT_COPPER_STAIRS)
                .get(stage.previous()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }
    
    @Override
    public ItemStack getScraped(ItemStack stack) {
        if (!canScrape(stack)) return stack;
        ItemStack stack1 = new ItemStack(waxed ? DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage)
                : DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage.previous()), stack.getCount(), stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }
    
    @Override
    public ItemStack getWaxed(ItemStack stack) {
        if (isWaxed(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_TRAPDOORS.get(stage), stack.getCount(), stack.getMetadata());
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
    public boolean isWaxed(ItemStack stack) {
        return waxed;
    }
    
}
