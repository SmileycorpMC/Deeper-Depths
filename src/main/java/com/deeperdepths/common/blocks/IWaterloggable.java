package com.deeperdepths.common.blocks;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IWaterloggable {

    PropertyBool WATERLOGGED = PropertyBool.create("waterlogged");

    default boolean isWaterLogged(IBlockAccess world, BlockPos pos, IBlockState state) {
        return state.getValue(WATERLOGGED);
    }

    default void setWaterLogged(World world, BlockPos pos, IBlockState state, boolean waterlogged) {
        world.setBlockState(pos, state.withProperty(WATERLOGGED, waterlogged), 3);
    }

}
