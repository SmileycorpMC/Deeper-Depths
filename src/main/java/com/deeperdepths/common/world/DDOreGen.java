package com.deeperdepths.common.world;

import com.deeperdepths.common.blocks.BlockDDStone;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.config.WorldConfig;
import com.deeperdepths.config.WorldGenEntry;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class DDOreGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.getDimension() == 0) {
            //Copper ore
            genOre(DeeperDepthsBlocks.COPPER_ORE.getDefaultState(), WorldConfig.copperOre, world, random, chunkX, chunkZ);
            genOre(DeeperDepthsBlocks.STONE.getDefaultState().withProperty(BlockDDStone.VARIANT, EnumStoneType.TUFF),
                    WorldConfig.tuff, world, random, chunkX, chunkZ);
            genOre(DeeperDepthsBlocks.DEEPSLATE.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y),
                    WorldConfig.deepslate, world, random, chunkX, chunkZ);
        }
        
    }
    
    private void genOre(IBlockState block, WorldGenEntry entry, World world, Random rand, int chunkX, int chunkZ){
        WorldGenSimpleOre generator = new WorldGenSimpleOre(entry.getSize(), block);
        int dy = entry.getMaxHeight() - entry.getMinHeight() +1;
        if (dy < 1) return;
        for (int i = 0; i < entry.getSpawnChances(); i++){
            int x = chunkX * 16 + rand.nextInt(16);
            int y = entry.getMinHeight() + rand.nextInt(dy);
            int z = chunkZ * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }
    
}
