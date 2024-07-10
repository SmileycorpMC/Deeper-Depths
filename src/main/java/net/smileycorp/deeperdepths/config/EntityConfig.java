package net.smileycorp.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class EntityConfig {
    
    public static EntityAttributesEntry axolotl;
    public static EntityAttributesEntry bogged;
    public static EntityAttributesEntry breeze;
    public static EntityAttributesEntry warden;
    //public static EntityAttributesEntry glare;
    //public static EntityAttributesEntry copper_golem;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deepdepths/entities.cfg"));
        try{
            config.load();
            bogged = new EntityAttributesEntry(config, "axolotl", 0.25, 32, 2, 16, 0, 0, 0);
            bogged = new EntityAttributesEntry(config, "bogged", 0.25, 32, 2, 16, 0, 0, 0);
            breeze = new EntityAttributesEntry(config, "breeze", 0.63, 24, 3, 30, 0, 0, 0);
            warden = new EntityAttributesEntry(config, "warden", 0.3, 32, 30, 500, 0, 0, 1);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
