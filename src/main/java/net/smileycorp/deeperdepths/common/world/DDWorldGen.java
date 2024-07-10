package net.smileycorp.deeperdepths.common.world;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.smileycorp.deeperdepths.common.world.chambers.WorldGenTrialChambers;

import java.util.Random;

public class DDWorldGen implements IWorldGenerator {

    public static final WorldGenTrialChambers chambers = new WorldGenTrialChambers();


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x + 8, 0, z + 8);

        if(world.provider.getDimension() == 0) {
            //What starts the process for seeing the chambers can generate
            if(world.getBiomeForCoordsBody(pos) != Biomes.OCEAN) {
                chambers.generate(world, random, pos);
            }
        }
    }



}
