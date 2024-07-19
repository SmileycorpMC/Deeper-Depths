package net.smileycorp.deeperdepths.client.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.BlockTrialSpawner;

public class TrialSpawnerStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(Constants.locStr(((BlockTrialSpawner)state.getBlock()).byState(state)),
                "state="+state.getValue(BlockTrialSpawner.STATE).getName());
    }

}
