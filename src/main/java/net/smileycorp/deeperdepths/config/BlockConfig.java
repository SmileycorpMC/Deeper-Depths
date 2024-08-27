package net.smileycorp.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class BlockConfig {
    
    public static BlockStatEntry amethyst;
    public static double amethystGrowChance;
    public static BlockStatEntry calcite;
    public static BlockStatEntry candle;
    public static BlockStatEntry cobbledDeepslate;
    public static BlockStatEntry copper;
    public static boolean bulkCopper;
    //why do they call it oven when you of in the cold food out of hot eat the food
    public static boolean tastyCopper;
    public static boolean copperAges;
    public static double copperAgeChance;
    public static BlockStatEntry copperOre;
    public static BlockStatEntry deepslate;
    public static BlockStatEntry deepslateBricks;
    public static BlockStatEntry deepslateTiles;
    public static BlockStatEntry heavyCore;
    public static BlockStatEntry reinforcedDeepslate;
    public static BlockStatEntry tintedGlass;
    public static BlockStatEntry trialPot;
    public static BlockStatEntry trialSpawner;
    public static BlockStatEntry tuff;
    public static BlockStatEntry tuffBricks;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deeperdepths/blocks.cfg"));
        try{
            config.load();
            amethyst = new BlockStatEntry(config, "amethyst", 1.5, 1.5, 0);
            amethystGrowChance = config.get("amethyst", "growChance", 0.2, "Decimal chance for budding amethyst to attempt to grow clusters?").getDouble();
            calcite = new BlockStatEntry(config, "calcite", 0.75, 0.75, 0);
            candle = new BlockStatEntry(config, "candle", 0.5, 0.5, 0);
            cobbledDeepslate = new BlockStatEntry(config, "cobbled deepslate", 3.5, 6, 0);
            copper = new BlockStatEntry(config, "copper", 3, 6, 1);
            bulkCopper = config.get("copper", "bulkCopper", true, "Can copper items be crafted in bulk using blocks?").getBoolean();
            tastyCopper = config.get("copper", "tastyCopper", false, "Is copper tasty?").getBoolean();
            copperAges = config.get("copper", "copperAges", true, "Does copper age?").getBoolean();
            copperAgeChance = config.get("copper", "ageChance", 0.05688889, "Decimal chance for copper to attempt to age?").getDouble();
            copperOre = new BlockStatEntry(config, "copper ore", 3, 3, 1);
            deepslate = new BlockStatEntry(config, "deepslate", 3, 6, 0);
            deepslateBricks = new BlockStatEntry(config, "deepslate bricks", 3.5, 6, 0);
            deepslateTiles = new BlockStatEntry(config, "deepslate tiles", 3, 6, 0);
            heavyCore = new BlockStatEntry(config, "heavy core", 10, 50, 0);
            reinforcedDeepslate = new BlockStatEntry(config, "reinforced deepslate", 55, 1200, 4);
            tintedGlass = new BlockStatEntry(config, "tinted glass", 0.3, 0.3, 0);
            trialPot = new BlockStatEntry(config, "trial pot", 0, 0, 0);
            trialSpawner = new BlockStatEntry(config, "trial spawner/vault", 50, 50, 0);
            tuff = new BlockStatEntry(config, "tuff", 1.5, 6, 0);
            tuffBricks = new BlockStatEntry(config, "tuff bricks", 1.5, 6, 0);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
