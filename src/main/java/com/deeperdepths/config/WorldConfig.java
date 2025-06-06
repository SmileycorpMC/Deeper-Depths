package com.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class WorldConfig {
    
    public static WorldGenEntry copperOre;
    public static WorldGenEntry tuff;
    public static WorldGenEntry deepslate;
    public static WorldGenEntry trial_chambers;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deeperdepths/world.cfg"));
        try{
            config.load();
            copperOre = new WorldGenEntry(config, "copper ore", 10, 16, 0, 112, 0);
            tuff = new WorldGenEntry(config, "tuff", 33, 2, 0, 16, 0);
            deepslate = new WorldGenEntry(config, "deepslate", 33, 0, 0, 16, 0);
            trial_chambers = new WorldGenEntry(config, "trial_chambers", 150, new int[]{0}, 22);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
}
