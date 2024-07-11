package net.smileycorp.deeperdepths.common.items;

import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.deeperdepths.common.blocks.IBlockProperties;

public class ItemDDSlab<T extends BlockSlab & IBlockProperties> extends ItemSlab implements IMetaItem {
    
    public ItemDDSlab(T half, T full) {
        super(half, half, full);
        setRegistryName(half.getRegistryName());
        setUnlocalizedName(half.getUnlocalizedName().substring(0, 5));
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
        return ((BlockSlab) block).getUnlocalizedName(stack.getMetadata());
    }
    
    
}
