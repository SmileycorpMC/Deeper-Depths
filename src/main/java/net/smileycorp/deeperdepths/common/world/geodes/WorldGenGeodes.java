package net.smileycorp.deeperdepths.common.world.geodes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.world.base.ModRand;
import net.smileycorp.deeperdepths.common.world.base.WorldGenCustomStructure;

import java.util.Random;

public class WorldGenGeodes extends WorldGenCustomStructure {

    private int spacing = 0;

    public WorldGenGeodes(String structureName) {

        super("geode/" + structureName);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if(spacing/4 > 100) {
            spacing = 0;
            System.out.println("Generated Geode at POS" + position);
            return generate(worldIn, rand, position.add(0, ModRand.range(20, 40), 0));
        }
        spacing++;
        return false;
    }
}
