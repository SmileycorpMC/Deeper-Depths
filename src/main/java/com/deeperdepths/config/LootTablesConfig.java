package com.deeperdepths.config;

import com.deeperdepths.common.items.DeeperDepthsItems;
import com.google.common.collect.Lists;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.item.ArmourSet;

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
            dungeonHorseArmor = new LootTableEntry(config, "dungeonHorseArmor", LootTableList.CHESTS_SIMPLE_DUNGEON, "main", 15,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
            desertTempleHorseArmor = new LootTableEntry(config, "desertTempleHorseArmor", LootTableList.CHESTS_DESERT_PYRAMID, "main", 15,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
            endCityHorseArmor = new LootTableEntry(config, "endCityHorseArmor", LootTableList.CHESTS_END_CITY_TREASURE, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
            jungleTempleHorseArmor = new LootTableEntry(config, "jungleTempleHorseArmor", LootTableList.CHESTS_JUNGLE_TEMPLE, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
            netherFortressHorseArmor = new LootTableEntry(config, "netherFortressHorseArmor", LootTableList.CHESTS_NETHER_BRIDGE, "main", 5,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
            strongholdHorseArmor = new LootTableEntry(config, "strongholdHorseArmor", LootTableList.CHESTS_STRONGHOLD_CORRIDOR, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
            villageBlacksmithHorseArmor = new LootTableEntry(config, "villageBlacksmithHorseArmor", LootTableList.CHESTS_VILLAGE_BLACKSMITH, "main", 1,
                    DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE));
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
