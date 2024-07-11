package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public interface IBlockProperties {
    
    default int getMaxMeta(){
            return 0;
        }
        
    default boolean usesCustomItemHandler(){
            return false;
        }
    
    default String byMeta(int meta) {
        return "normal";
    }
    
    default String byState(IBlockState state) {
        return byMeta(((Block)this).getMetaFromState(state));
    }
    
}
