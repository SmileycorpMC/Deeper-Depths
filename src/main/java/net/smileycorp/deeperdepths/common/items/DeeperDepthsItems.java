package net.smileycorp.deeperdepths.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;

import java.lang.reflect.Field;

public class DeeperDepthsItems {
    
    //materials
    public static final Item AMETHYST = null;
    public static final Item COPPER_INGOT = null;
    public static final Item ECHO_SHARD = null;
    public static final Item BREEZE_ROD = null;
    public static final Item TALLOW = null;
    
    //useable items
    public static final Item WIND_CHARGE = null;
    public static final Item OMINOUS_BOTTLE = null;
    public static final Item TRIAL_KEY = null;
    public static final Item OMINOUS_KEY = null;
    
    //tools
    public static final Item SPYGLASS = null;
    public static final Item RECOVERY_COMPASS = null;
    
    //this is a terrible way of doing it, but I'm feeling lazy for this part
    public static void registerBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Field field : DeeperDepthsItems.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Item) || object == null) continue;
                registry.register((Item) object);
            } catch (Exception e) {
                DeeperDepths.error(field, e);
            }
        }
        for (Block block : DeeperDepthsBlocks.BLOCKS) registry.register(new ItemBlock(block));
        registry.register(new ItemSlab(DeeperDepthsBlocks.STONE, DeeperDepthsBlocks.STONE_SLAB, DeeperDepthsBlocks.DOUBLE_STONE_SLAB));
        registry.register(new ItemSlab(DeeperDepthsBlocks.CUT_COPPER, DeeperDepthsBlocks.CUT_COPPER_SLAB, DeeperDepthsBlocks.DOUBLE_CUT_COPPER_SLAB));
    }
    
}
