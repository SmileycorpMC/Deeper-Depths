package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.item.Item;
import net.smileycorp.atlas.api.item.IMetaItem;

public class ItemDeeperDepths extends Item implements IMetaItem {
    
    public ItemDeeperDepths(String name) {
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }
    
}
