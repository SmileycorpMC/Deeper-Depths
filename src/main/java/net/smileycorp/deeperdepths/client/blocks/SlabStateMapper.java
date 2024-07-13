package net.smileycorp.deeperdepths.client.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.IBlockProperties;

public class SlabStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(Constants.locStr(((IBlockProperties) state.getBlock()).byState(state)),
                ((BlockSlab)state.getBlock()).isDouble() ? "double" : "half=" + state.getValue(BlockSlab.HALF).getName());
    }

}
