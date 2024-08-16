package net.smileycorp.deeperdepths.common.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public abstract class WorldGenOre extends WorldGenerator {
	
    private final int num;
    
    public WorldGenOre(int num) {
        this.num = num;
    }

    public boolean generate(World world, Random rand, BlockPos pos) {
        float radius = rand.nextFloat() * (float)Math.PI;
        double d0 = (float)(pos.getX() + 8) + MathHelper.sin(radius) * (float) num / 8.0F;
        double d1 = (float)(pos.getX() + 8) - MathHelper.sin(radius) * (float) num / 8.0F;
        double d2 = (float)(pos.getZ() + 8) + MathHelper.cos(radius) * (float) num / 8.0F;
        double d3 = (float)(pos.getZ() + 8) - MathHelper.cos(radius) * (float) num / 8.0F;
        double d4 = pos.getY() + rand.nextInt(3) - 2;
        double d5 = pos.getY() + rand.nextInt(3) - 2;
        for (int i = 0; i < num; ++i) {
            float f1 = (float)i / (float) num;
            double d6 = d0 + (d1 - d0) * (double)f1;
            double d7 = d4 + (d5 - d4) * (double)f1;
            double d8 = d2 + (d3 - d2) * (double)f1;
            double d9 = rand.nextDouble() * (double) num / 16.0D;
            double d10 = (double)(MathHelper.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double)(MathHelper.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);
            for (int l1 = j; l1 <= i1; ++l1) {
                double d12 = ((double)l1 + 0.5D - d6) / (d10 / 2.0D);
                if (d12 * d12 < 1.0D) for (int i2 = k; i2 <= j1; ++i2) {
                    double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);
                    if (d12 * d12 + d13 * d13 < 1.0D) for (int j2 = l; j2 <= k1; ++j2) {
                        double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);
                        if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                            BlockPos pos1 = new BlockPos(l1, i2, j2);
                            if (world.getBlockState(pos1).getBlock() != Blocks.STONE) continue;
                            setBlockAndNotifyAdequately(world, pos1, getState(world, rand, pos1));
                        }
                    }
                }
            }
        }
        return true;
    }
    
    protected abstract IBlockState getState(World world, Random rand, BlockPos pos);
    
}
