package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;

import java.util.Random;

public class BlockCutCopperStairs extends BlockDDStairs implements ICopperBlock {
    
    private final EnumWeatherStage stage;
    private final boolean waxed;
    
    public BlockCutCopperStairs(EnumWeatherStage stage, boolean waxed) {
        super(getName(stage, waxed), DeeperDepthsBlocks.CUT_COPPER.getDefaultState().withProperty(WEATHER_STAGE, stage));
        setSoundType(DeeperDepthsSoundTypes.COPPER);
        this.stage = stage;
        this.waxed = waxed;
        needsRandomTick = !waxed && stage != EnumWeatherStage.OXIDIZED;
    }
    
    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        tryWeather(world, pos, state, random);
    }
    
    private static String getName(EnumWeatherStage stage, boolean waxed) {
        StringBuilder builder = new StringBuilder();
        if (waxed) builder.append("Waxed_");
        if (stage != EnumWeatherStage.NORMAL) builder.append(stage.getUnlocalizedName() + "_");
        return builder + "Cut_Copper";
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
        return copyProperties(state, DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(waxed ? stage : stage.previous()).getDefaultState());
    }
    
    private IBlockState copyProperties(IBlockState oldState, IBlockState newState) {
        return newState.withProperty(FACING, oldState.getValue(FACING)).withProperty(HALF, oldState.getValue(HALF))
                .withProperty(SHAPE, oldState.getValue(SHAPE));
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return waxed ? state : copyProperties(state, DeeperDepthsBlocks.WAXED_CUT_COPPER_STAIRS.get(stage).getDefaultState());
    }
    
    @Override
    public IBlockState getWeathered(IBlockState state) {
        return waxed || stage == EnumWeatherStage.OXIDIZED ? state : copyProperties(state,  DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage.next()).getDefaultState());
    }
    
}
