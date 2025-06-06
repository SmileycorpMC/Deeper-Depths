package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.blocks.*;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsItems {
    
    public static final Set<Item> ITEMS = Sets.newHashSet();
    
    //materials
    public static final Item MATERIALS = new ItemDeeperDepthsMaterial();
    //public static final Item TALLOW = null;
    
    //useable items
    public static final Item WIND_CHARGE = new ItemWindCharge();
    public static final Item OMINOUS_BOTTLE = new ItemOminousBottle();
    public static final Item TRIAL_KEY = new ItemTrialKey();
    public static final Item MACE = new ItemMace();
    public static final Item TRIAL_CHAMBERS_PAINTING = new ItemCustomPainting();

    //tools
    /*public static final Item SPYGLASS = null;
    public static final Item RECOVERY_COMPASS = null;*/
    
    //this is a terrible way of doing it, but I'm feeling lazy for this part
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Block block : DeeperDepthsBlocks.BLOCKS) {
            if (block instanceof BlockCandle) register(registry, new ItemBlockCandle((BlockCandle) block));
            else if (block instanceof BlockCopperDoor) register(registry, ((BlockCopperDoor) block).getItem());
            else if (!(block instanceof BlockDDSlab)) register(registry, (block instanceof ICopperBlock) ?
                    new ItemBlockCopper(block) : new ItemDDBlock(block));
            else if (!((BlockDDSlab) block).isDouble()) register(registry, block instanceof BlockCutCopperSlab ?
                    new ItemCopperSlab((BlockCutCopperSlab) block) : new ItemDDSlab((BlockDDSlab)block));
        }
        for (Field field : DeeperDepthsItems.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Item) || object == null) continue;
                register(registry, (Item) object);
            } catch (Exception e) {
                DeeperDepths.error(field, e);
            }
        }
    }
    
    private static void register(IForgeRegistry<Item> registry, Item item) {
        registry.register(item);
        ITEMS.add(item);
    }
    
}
