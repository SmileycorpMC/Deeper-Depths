package net.smileycorp.deeperdepths.client.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.BlockDDStoneWall;
import net.smileycorp.deeperdepths.common.blocks.BlockVault;
import net.smileycorp.deeperdepths.common.blocks.IBlockProperties;

import java.util.HashMap;

public class WallStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        HashMap<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(BlockWall.VARIANT);
        map.remove(BlockDDStoneWall.TYPE);
        return new ModelResourceLocation(Constants.locStr(((BlockDDStoneWall)state.getBlock()).byState(state)), getPropertyString(map));
    }

}
