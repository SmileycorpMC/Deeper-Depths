package com.deeperdepths.client.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.BlockCopperChain;
import com.deeperdepths.common.blocks.ICopperBlock;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class CopperChainStateMapper extends StateMapperBase
{
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        StringBuilder builder = new StringBuilder();
        if (((BlockCopperChain)state.getBlock()).isWaxed()) builder.append("waxed_");
        EnumWeatherStage stage = state.getValue(ICopperBlock.WEATHER_STAGE);
        if (stage != EnumWeatherStage.NORMAL) builder.append(stage.getName() + "_");
        builder.append("copper_chain");
        return new ModelResourceLocation(Constants.locStr(builder.toString()), "axis=" + state.getValue(BlockCopperChain.AXIS).getName());
    }
}