package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;

public interface ICopperBlock {
    
    PropertyEnum<EnumWeatherStage> WEATHER_STAGE = PropertyEnum.create("weather_stage", EnumWeatherStage.class);
    
    boolean isWaxed(IBlockState state);
    
    EnumWeatherStage getStage(IBlockState state);
    
    IBlockState getScraped(IBlockState state);
    
    IBlockState getWaxed(IBlockState state);
    
    IBlockState getWeathered(IBlockState state);
    
}
