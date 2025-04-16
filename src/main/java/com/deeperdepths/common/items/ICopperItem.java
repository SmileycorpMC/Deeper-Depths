package com.deeperdepths.common.items;

import net.minecraft.item.ItemStack;

public interface ICopperItem {
    
    ItemStack getWaxed();
    
    ItemStack getScraped();
    
    boolean canWax();
    
    boolean canScrape();
    
}
