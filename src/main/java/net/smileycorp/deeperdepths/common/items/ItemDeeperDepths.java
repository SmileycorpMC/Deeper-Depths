package net.smileycorp.deeperdepths.common.items;

import net.minecraft.item.Item;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

public class ItemDeeperDepths extends Item implements IMetaItem {
    
    public ItemDeeperDepths(String name) {
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }
    
}
