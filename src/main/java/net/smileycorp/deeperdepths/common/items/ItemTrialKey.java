package net.smileycorp.deeperdepths.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.deeperdepths.common.Constants;

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
        return "item." + Constants.name(TextUtils.toProperCase(byMeta(stack.getMetadata())));
    }
    
    public static boolean isOminous(ItemStack stack) {
        return isOminous(stack.getMetadata());
    }
    
    public static boolean isOminous(int meta) {
        return meta == 1;
    }
    
}
