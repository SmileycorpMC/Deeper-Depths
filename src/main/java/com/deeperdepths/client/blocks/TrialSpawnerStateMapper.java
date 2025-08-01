package com.deeperdepths.client.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.BlockTrialSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class TrialSpawnerStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(Constants.locStr(((BlockTrialSpawner)state.getBlock()).byState(state)),
                "state="+state.getValue(BlockTrialSpawner.STATE).getName());
    }

}
