package com.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class EntityConfig {
    
    public static EntityAttributesEntry bogged;
    public static int poisonArrowDuration;
    public static double boggedAttackCooldownEasy = 3.5;
    public static double boggedAttackCooldownNormal = 3.5;
    public static double boggedAttackCooldownHard = 2.5;
    public static EntityAttributesEntry breeze;
    public static EntityAttributesEntry warden;
    //public static EntityAttributesEntry glare;
    //public static EntityAttributesEntry copper_golem;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deeperdepths/entities.cfg"));
        try{
            config.load();
            //bogged
            bogged = new EntityAttributesEntry(config, "bogged", 0.25, 32, 2, 16, 0, 0, 0);
            poisonArrowDuration = config.get("bogged", "poisonArrowDuration", 4, "How long the poison from arrows last, in seconds").getInt();
            boggedAttackCooldownEasy = config.get("bogged", "attackCooldownEasy", 3.5, "The cooldown between firing on Easy, in seconds").getDouble();
            boggedAttackCooldownNormal = config.get("bogged", "attackCooldownMedium", 3.5, "The cooldown between firing on Normal, in seconds").getDouble();
            boggedAttackCooldownHard = config.get("bogged", "attackCooldownHard", 2.5, "The cooldown between firing on Hard, in seconds").getDouble();
            //breeze
            breeze = new EntityAttributesEntry(config, "breeze", 0.63, 24, 3, 30, 0, 0, 0);
            //warden = new EntityAttributesEntry(config, "warden", 0.3, 32, 30, 500, 0, 0, 1);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
