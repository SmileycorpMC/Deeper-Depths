package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;

public class BlockCopperGrate extends BlockCopper {
    
    public BlockCopperGrate() {
        super("Copper_Grate");
        setSoundType(DeeperDepthsSoundTypes.COPPER_GRATE);
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
