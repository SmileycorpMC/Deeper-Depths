package net.smileycorp.deeperdepths.client.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.BlockVault;

import java.util.HashMap;

public class VaultStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        HashMap<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap();
        map.put(BlockVault.STATE, state.getValue(BlockVault.STATE));
        map.put(BlockHorizontal.FACING, state.getValue(BlockHorizontal.FACING));
        return new ModelResourceLocation(Constants.locStr(((BlockVault)state.getBlock()).byState(state)), getPropertyString(map));
    }

}
