package com.deeperdepths.common.world.geodes;

import com.deeperdepths.common.world.base.WorldGenCustomStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenGeodes extends WorldGenCustomStructure {

    private int spacing = 0;

    public WorldGenGeodes(String structureName) {

        super("geode/" + structureName);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if(spacing/4 > 40) {
            spacing = 0;
            return super.generate(worldIn, rand, position);
        }
        spacing++;
        return false;
    }
}
