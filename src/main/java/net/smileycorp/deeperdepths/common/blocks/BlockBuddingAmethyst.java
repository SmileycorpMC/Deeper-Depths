package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumClusterSize;

import java.util.Random;

public class BlockBuddingAmethyst extends BlockAmethyst {
    
    public BlockBuddingAmethyst() {
        super("budding_amethyst");
        needsRandomTick = true;
    }
    
    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) return;
        if (world.rand.nextInt(5) > 0) return;
        EnumFacing facing = EnumFacing.values()[random.nextInt(6)];
        BlockPos pos1 = pos.offset(facing);
        IBlockState state1 = world.getBlockState(pos1);
        if (state1.getMaterial() == Material.AIR) world.setBlockState(pos1, DeeperDepthsBlocks.AMETHYST_BUDS.get(EnumClusterSize.SMALL)
                .getDefaultState().withProperty(BlockDirectional.FACING, facing), 3);
        if (state1.getBlock() instanceof BlockAmethystBud) {
            if (!((BlockAmethystBud) state1.getBlock()).canGrow()) return;
            if (state1.getValue(BlockDirectional.FACING) != facing) return;
            ((BlockAmethystBud) state1.getBlock()).grow(world, state1, pos1);
        }
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 0;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }
    
}
