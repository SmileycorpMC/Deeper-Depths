package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.IBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.IMetaItem;

public class ItemDDBlock<T extends Block & IBlockProperties> extends ItemBlock implements IMetaItem {
    
    public ItemDDBlock(T block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName().substring(0, 5));
        if (block.getMaxMeta() > 1) setHasSubtypes(true);
    }
    
    @Override
    public int getMaxMeta() {
        return ((IBlockProperties)block).getMaxMeta();
    }
    
    @Override
    public String byMeta(int meta) {
        return ((IBlockProperties)block).byMeta(meta);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getMaxMeta() > 0 ? "tile." + Constants.name(byMeta(stack.getMetadata())) : super.getUnlocalizedName();
    }
    
    
}
