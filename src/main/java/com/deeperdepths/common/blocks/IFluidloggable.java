package com.deeperdepths.common.blocks;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Optional;

public interface IFluidloggable {

    PropertyBool WATERLOGGED = PropertyBool.create("waterlogged");

    default boolean isFluidLogged(IBlockAccess world, BlockPos pos, IBlockState state) {
        return state.getValue(WATERLOGGED);
    }

    default Optional<Fluid> getContainedFluid(IBlockAccess world, BlockPos pos, IBlockState state) {
        return state.getValue(WATERLOGGED) ? Optional.of(FluidRegistry.WATER) : Optional.empty();
    }

    default boolean canContainFluid(IBlockAccess world, BlockPos pos, IBlockState state, Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    default void fillWithFluid(World world, BlockPos pos, IBlockState state, Fluid fluid) {
        world.setBlockState(pos, state.withProperty(WATERLOGGED, true), 3);
    }

    default void empty(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(WATERLOGGED, false), 3);
    }

}
