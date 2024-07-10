package net.smileycorp.deeperdepths.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.IBlockProperties;

public class ItemDDBlock<T extends Block & IBlockProperties> extends ItemBlock implements IMetaItem {
    
    public ItemDDBlock(T block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName().substring(0, 5));
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
