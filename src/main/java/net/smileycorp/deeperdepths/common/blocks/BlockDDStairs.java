package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.smileycorp.atlas.api.block.BlockStairsBase;
import net.smileycorp.deeperdepths.common.DeeperDepths;

public class BlockDDStairs extends BlockStairsBase implements IBlockProperties {
    
    public BlockDDStairs(String name, IBlockState state) {
        super(name, state);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(state.getBlock().getSoundType(state, null, null, null));
    }
    
}
