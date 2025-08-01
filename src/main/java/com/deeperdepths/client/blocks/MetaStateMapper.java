package com.deeperdepths.client.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class MetaStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(Constants.locStr(((IBlockProperties) state.getBlock()).byState(state)));
    }

}
