package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockReinforcedDeepslate extends BlockDeeperDepths {
    
    public BlockReinforcedDeepslate() {
        super("Reinforced_Deepslate", Material.ROCK, BlockConfig.reinforcedDeepslate.getHardness(),
                BlockConfig.reinforcedDeepslate.getHardness(), BlockConfig.reinforcedDeepslate.getHarvestLevel());
        setSoundType(DeeperDepthsSoundTypes.DEEPSLATE);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.GRAY;
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }
    
    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }
    
}
