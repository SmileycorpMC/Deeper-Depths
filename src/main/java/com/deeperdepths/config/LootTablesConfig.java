package com.deeperdepths.config;

import com.deeperdepths.common.items.DeeperDepthsItems;
import com.google.common.collect.Lists;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.LootTableEntry;

import java.io.File;
import java.util.List;

public class LootTablesConfig {

    public static final List<LootTableEntry> ENTRIES = Lists.newArrayList();

    public static LootTableEntry dungeonHorseArmor;
    public static LootTableEntry desertTempleHorseArmor;
    public static LootTableEntry endCityHorseArmor;
    public static LootTableEntry jungleTempleHorseArmor;
    public static LootTableEntry netherFortressHorseArmor;
    public static LootTableEntry strongholdHorseArmor;
    public static LootTableEntry villageBlacksmithHorseArmor;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deeperdepths/loot_tables.cfg"));
        try{
            config.load();
            dungeonHorseArmor = new LootTableEntry(config, ENTRIES, "dungeonHorseArmor", LootTableList.CHESTS_SIMPLE_DUNGEON, "main", 15,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
            desertTempleHorseArmor = new LootTableEntry(config, ENTRIES, "desertTempleHorseArmor", LootTableList.CHESTS_DESERT_PYRAMID, "main", 15,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
            endCityHorseArmor = new LootTableEntry(config, ENTRIES, "endCityHorseArmor", LootTableList.CHESTS_END_CITY_TREASURE, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
            jungleTempleHorseArmor = new LootTableEntry(config, ENTRIES, "jungleTempleHorseArmor", LootTableList.CHESTS_JUNGLE_TEMPLE, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
            netherFortressHorseArmor = new LootTableEntry(config, ENTRIES, "netherFortressHorseArmor", LootTableList.CHESTS_NETHER_BRIDGE, "main", 5,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
            strongholdHorseArmor = new LootTableEntry(config, ENTRIES, "strongholdHorseArmor", LootTableList.CHESTS_STRONGHOLD_CORRIDOR, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
            villageBlacksmithHorseArmor = new LootTableEntry(config, ENTRIES, "villageBlacksmithHorseArmor", LootTableList.CHESTS_VILLAGE_BLACKSMITH, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getHorseArmour());
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
