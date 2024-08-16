package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;

public class BlockTintedGlass extends BlockDeeperDepths {
    
    public BlockTintedGlass() {
        super("Tinted_Glass", Material.GLASS, 0.3f, 0.3f, 0);
        setSoundType(SoundType.GLASS);
        lightOpacity = 255;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
}
