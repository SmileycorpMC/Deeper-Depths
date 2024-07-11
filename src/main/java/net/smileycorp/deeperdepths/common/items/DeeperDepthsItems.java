package net.smileycorp.deeperdepths.common.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;

import java.lang.reflect.Field;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsItems {
    
    public static final Set<Item> ITEMS = Sets.newHashSet();
    
    //materials
    public static final Item MATERIALS = new ItemDeeperDepthsMaterial();
    //public static final Item TALLOW = null;
    
    //useable items
    //public static final Item WIND_CHARGE = null;
    public static final Item OMINOUS_BOTTLE = new ItemOminousBottle();
    /*public static final Item TRIAL_KEY = null;
    public static final Item OMINOUS_KEY = null;*/
    
    //tools
    /*public static final Item SPYGLASS = null;
    public static final Item RECOVERY_COMPASS = null;*/
    
    //this is a terrible way of doing it, but I'm feeling lazy for this part
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Field field : DeeperDepthsItems.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Item) || object == null) continue;
                register(registry, (Item) object);
            } catch (Exception e) {
                DeeperDepths.error(field, e);
            }
        }
        for (Block block : DeeperDepthsBlocks.BLOCKS) registry.register(new ItemDDBlock(block));
        register(registry, new ItemDDSlab(DeeperDepthsBlocks.STONE_SLAB, DeeperDepthsBlocks.DOUBLE_STONE_SLAB));
        register(registry, new ItemDDSlab(DeeperDepthsBlocks.CUT_COPPER_SLAB, DeeperDepthsBlocks.DOUBLE_CUT_COPPER_SLAB));
    }
    
    private static void register(IForgeRegistry<Item> registry, Item item) {
        registry.register(item);
        ITEMS.add(item);
    }
    
}
