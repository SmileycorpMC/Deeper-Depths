package net.smileycorp.deeperdepths.common.blocks;

public interface IBlockProperties {
    
        default int getMaxMeta(){
            return 0;
        }
        
        default boolean usesCustomItemHandler(){
            return false;
        }
    
        default String byMeta(int meta) {
            return "inventory";
        }
    
}
