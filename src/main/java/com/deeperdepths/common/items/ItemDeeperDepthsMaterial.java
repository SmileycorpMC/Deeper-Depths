package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.IRarity;

public class ItemDeeperDepthsMaterial extends ItemDeeperDepths
{
    private final String[] variants = {"copper_ingot", "amethyst_shard", "echo_shard", "breeze_rod"};
    
    public ItemDeeperDepthsMaterial()
    {
        super("material");
        setHasSubtypes(true);
    }
    
    @Override
    public String byMeta(int meta)
    { return variants[Math.min(meta, variants.length - 1)]; }
    
    @Override
    public int getMaxMeta() {
        return variants.length;
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < variants.length; i++) if (i != 2) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Constants.name(byMeta(stack.getMetadata()));
    }
    
    @Override
    public IRarity getForgeRarity(ItemStack stack)
    { return stack.getMetadata() == 2 ? EnumRarity.UNCOMMON : super.getForgeRarity(stack); }
}