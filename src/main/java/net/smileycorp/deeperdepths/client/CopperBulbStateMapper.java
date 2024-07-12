package net.smileycorp.deeperdepths.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.BlockCopper;
import net.smileycorp.deeperdepths.common.blocks.BlockCopperBulb;
import net.smileycorp.deeperdepths.common.blocks.EnumWeatherStage;

public class CopperBulbStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        StringBuilder builder = new StringBuilder();
        if (((BlockCopperBulb)state.getBlock()).isWaxed()) builder.append("waxed_");
        EnumWeatherStage stage = state.getValue(BlockCopper.WEATHER_STAGE);
        if (stage != EnumWeatherStage.NORMAL) builder.append(stage.getName() + "_");
        builder.append("copper_bulb");
        if (state.getValue(BlockCopperBulb.LIT)) builder.append("_lit");
        if (state.getValue(BlockCopperBulb.POWERED)) builder.append("_powered");
        return new ModelResourceLocation(Constants.locStr(builder.toString()), "normal");
    }

}
