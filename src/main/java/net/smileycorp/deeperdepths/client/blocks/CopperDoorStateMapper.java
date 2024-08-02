package net.smileycorp.deeperdepths.client.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

import java.util.LinkedHashMap;
import java.util.Map;

public class CopperDoorStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap();
        put(map, state, BlockDoor.FACING);
        put(map, state, BlockDoor.HALF);
        put(map, state, BlockDoor.HINGE);
        put(map, state, BlockDoor.OPEN);
        return new ModelResourceLocation(state.getBlock().getRegistryName(), getPropertyString(map));
    }
    
    private <T extends Comparable<T>> void  put(Map<IProperty<?>, Comparable<?>> map, IBlockState state, IProperty<T> prop) {
        map.put(prop, state.getValue(prop));
    }

}
