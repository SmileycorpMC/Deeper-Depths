package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSculkCatalyst extends BlockSculk {
    
    public BlockSculkCatalyst() {
        super("sculk_catalyst", 3, 3);
        setSoundType(DeeperDepthsSoundTypes.SCULK);
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 5;
    }
    
}
