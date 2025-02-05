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

public class BlockSculk extends BlockDeeperDepths implements IHoeEfficient {
    
    public BlockSculk(String name, float h, float r) {
        super(name, Material.PLANTS, 0.2f, 0.2f);
        setSoundType(DeeperDepthsSoundTypes.SCULK);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.BLACK;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
    
    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }
    
    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return false;
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 1;
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }
    
}
