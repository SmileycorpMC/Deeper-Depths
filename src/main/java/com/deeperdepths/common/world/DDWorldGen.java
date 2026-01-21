package com.deeperdepths.common.world;

import com.deeperdepths.common.world.base.ModRand;
import com.deeperdepths.common.world.base.WorldGenCustomStructure;
import com.deeperdepths.common.world.chambers.WorldGenTrialChambers;
import com.deeperdepths.common.world.geodes.WorldGenGeodes;
import com.deeperdepths.config.WorldConfig;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class DDWorldGen implements IWorldGenerator {

    public static final WorldGenTrialChambers chambers = new WorldGenTrialChambers();
    protected static final WorldGenGeodes[] geodes = {new WorldGenGeodes("geode_1"), new WorldGenGeodes("geode_2"), new WorldGenGeodes("geode_3"), new WorldGenGeodes("geode_4")};

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x + 8, 0, z + 8);

        //Dimension iterator List
        if(isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {
            //What starts the process for seeing the chambers can generate
            //Trial Chambers
            chambers.generate(world, random, pos);
            if(world.getBiomeForCoordsBody(pos) != Biomes.OCEAN && world.getBiomeForCoordsBody(pos) != Biomes.DEEP_OCEAN) {
                //Geodes
                //Awaiting Structures and Textures
                WorldGenCustomStructure genGeode = ModRand.choice(geodes);
                genGeode.generate(world, random, pos.add(0, ModRand.range(15, 35), 0));

            }
        }
    }



    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.trial_chambers.getDimensions()) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

}
