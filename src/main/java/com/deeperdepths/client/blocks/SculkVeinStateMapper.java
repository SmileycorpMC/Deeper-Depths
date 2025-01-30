package com.deeperdepths.client.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.BlockSculkVein;
import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

import java.util.Map;

public class SculkVeinStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(BlockSculkVein.META);
        return new ModelResourceLocation(Constants.locStr("sculk_vein"), getPropertyString(map));
    }

}
