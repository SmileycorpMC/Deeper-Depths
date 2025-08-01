package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.block.state.IBlockState;
import net.smileycorp.atlas.api.block.BlockStairsBase;

public class BlockDDStairs extends BlockStairsBase implements IBlockProperties {
    
    public BlockDDStairs(String name, IBlockState state) {
        super(name, state);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(state.getBlock().getSoundType(state, null, null, null));
        setUnlocalizedName(Constants.name(name + "_stairs"));
    }
    
}
