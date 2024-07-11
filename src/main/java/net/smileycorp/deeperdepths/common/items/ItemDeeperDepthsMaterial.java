package net.smileycorp.deeperdepths.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemDeeperDepthsMaterial extends ItemDeeperDepths {
    
    private final String[] variants = {"copper_ingot", "amethyst_shard", "echo_shard", "breeze_rod"};
    
    public ItemDeeperDepthsMaterial() {
        super("material");
        setHasSubtypes(true);
    }
    
    @Override
    public String byMeta(int meta) {
        return variants[meta];
    }
    
    @Override
    public int getMaxMeta() {
        return variants.length;
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < variants.length; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + byMeta(stack.getMetadata());
    }
    
}
