package com.deeperdepths.common.items;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.*;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.item.ArmourSet;
import net.smileycorp.atlas.api.item.ToolSet;

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
    public static final Item EXPLORER_MAP = new ItemExplorerMap();

    public static final ToolSet COPPER_TOOLS = new ToolSet(Constants.MODID, "copper",
            EnumHelper.addToolMaterial("COPPER", 1, 190, 5, 1, 13), DeeperDepths.CREATIVE_TAB);
    public static final ArmourSet COPPER_ARMOR = new ArmourSet(Constants.MODID, "copper",
            EnumHelper.addArmorMaterial("COPPER", "deeperdepths:copper", 190, new int[] {1, 3, 4, 2}, 8,
                    DeeperDepthsSoundEvents.ITEM_ARMOR_EQUIP_COPPER, 0), DeeperDepths.CREATIVE_TAB, 4);

    public static final Item SPYGLASS = new ItemSpyglass();
    public static final Item TRIAL_KEY = new ItemTrialKey();
    public static final Item TRIAL_CHAMBERS_PAINTING = new ItemCustomPainting();
    public static final Item OMINOUS_BOTTLE = new ItemOminousBottle();
    public static final Item MACE = new ItemMace();
    public static final Item RECOVERY_COMPASS = new ItemRecoveryCompass();
    //skull emoji
    public static final Item SCULK_VEIN = new ItemSculkVein();
    
    //this is a terrible way of doing it, but I'm feeling lazy for this part
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Block block : DeeperDepthsBlocks.BLOCKS) {
            if (block instanceof BlockCandle) register(registry, new ItemBlockCandle((BlockCandle) block));
            //jank
            else if (block instanceof BlockSculkVein) {if (((BlockSculkVein)block).getOrdinal() == 0) register(registry, SCULK_VEIN);}
            else if (block instanceof BlockCopperDoor) register(registry, ((BlockCopperDoor) block).getItem());
            else if (block instanceof BlockCopperChest) register(registry, new ItemCopperChest((BlockCopperChest) block));
            else if (!(block instanceof BlockDDSlab)) register(registry, (block instanceof ICopperBlock) ?
                    new ItemBlockCopper(block) : new ItemDDBlock(block));
            else if (!((BlockDDSlab) block).isDouble()) register(registry, block instanceof BlockCutCopperSlab ?
                    new ItemCopperSlab((BlockCutCopperSlab) block) : new ItemDDSlab((BlockDDSlab)block));
        }
        for (Field field : DeeperDepthsItems.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (object instanceof ToolSet) ((ToolSet)object).getItems().forEach(item -> register(registry, item));
                if (object instanceof ArmourSet) ((ArmourSet)object).getItems().forEach(item -> register(registry, item));
                //bad
                if (!(object instanceof Item) || object == null || object == SCULK_VEIN) continue;
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
