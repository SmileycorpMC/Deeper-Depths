package com.deeperdepths.common.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenSimpleOre extends WorldGenOre {
    
    private final IBlockState state;
    
    public WorldGenSimpleOre(int num, IBlockState state) {
        super(num);
        this.state = state;
    }
    
    @Override
    protected IBlockState getState(World world, Random rand, BlockPos pos) {
        return state;
    }
    
}
