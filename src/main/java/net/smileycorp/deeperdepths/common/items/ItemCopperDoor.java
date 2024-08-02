package net.smileycorp.deeperdepths.common.items;

import net.minecraft.item.ItemDoor;
import net.smileycorp.deeperdepths.common.blocks.BlockCopperDoor;

public class ItemCopperDoor<T extends BlockCopperDoor> extends ItemDoor {
    
    public ItemCopperDoor(T block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName().replace("tile.", ""));
        setCreativeTab(block.getCreativeTabToDisplayOn());
    }
    
}
