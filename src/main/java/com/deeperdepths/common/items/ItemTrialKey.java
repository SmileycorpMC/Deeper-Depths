package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.IRarity;

public class ItemTrialKey extends ItemDeeperDepths {
    
    public ItemTrialKey() {
        super("trial_key");
        setHasSubtypes(true);
    }
    
    @Override
    public String byMeta(int meta) {
        return (isOminous(meta) ? "ominous" : "trial") + "_key";
    }
    
    @Override
    public int getMaxMeta() {
        return 2;
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < 2; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Constants.name(byMeta(stack.getMetadata()));
    }
    
    public static boolean isOminous(ItemStack stack) {
        return isOminous(stack.getMetadata());
    }
    
    public static boolean isOminous(int meta) {
        return meta == 1;
    }
    
    //vanilla doesn't do this, but I'm kinda confused why it doesn't, all other ominous items are uncommon in vanilla
    //why are the ominous keys common, it just doesn't make sense
    //ngl I fully expect this to get changed in another snapshot after 24w33a
    //they didn't lmao, I'm not changing it though because I'm pretty sure it's still a bug or oversight
    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return isOminous(stack) ? EnumRarity.UNCOMMON : super.getForgeRarity(stack);
    }
    
}
