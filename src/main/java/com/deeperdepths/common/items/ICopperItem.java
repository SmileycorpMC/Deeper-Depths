package com.deeperdepths.common.items;

import com.deeperdepths.common.blocks.ICopperBlock;
import net.minecraft.item.ItemStack;

public interface ICopperItem {
    
    ICopperBlock getBlock();
    
    default ItemStack getWaxed(ItemStack stack) {
        return getBlock().getWaxed(stack);
    }
    
    default ItemStack getScraped(ItemStack stack) {
        return getBlock().getScraped(stack);
    }
    
    default boolean canWax(ItemStack stack) {
        return getBlock().canWax(stack);
    }
    
    default boolean canScrape(ItemStack stack) {
        return getBlock().canScrape(stack);
    }
    
}
