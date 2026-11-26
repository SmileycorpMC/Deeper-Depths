package com.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.EntityAttributesEntry;

import java.io.File;

public class EntityConfig {

    public static int copperArmourChance;
    public static EntityAttributesEntry bogged;
    public static int poisonArrowDuration;
    public static double boggedAttackCooldownEasy = 3.5;
    public static double boggedAttackCooldownNormal = 3.5;
    public static double boggedAttackCooldownHard = 2.5;
    public static EntityAttributesEntry breeze;
    public static EntityAttributesEntry warden;
    public static int wardenAngerAgitatedMin;
    public static int wardenAngerAngryMin;
    public static int wardenAngerDecayTicks;
    public static int wardenAngerDecayAmount;
    public static int wardenAngerGainCollision;
    public static int wardenAngerGainAttacked;
    public static int wardenAngerGainRoar;
    public static int wardenAngerGainSniff;
    public static int wardenAngerGainVibration;
    public static int wardenAngerMax;
    public static int wardenBurrowingCooldown;
    public static int wardenDarknessFrequency;
    public static int wardenDarknessLength;
    public static double wardenDarknessRange;
    public static int wardenSniffCooldownMin;
    public static int wardenSniffCooldownMax;
    public static int wardenSonicBoomCooldown;
    public static double wardenSniffHorizontalRange;
    public static double wardenSniffVerticalRange;
    public static double wardenSonicBoomDamage;
    public static double wardenSonicBoomKnockback;
    public static double wardenSonicBoomKnockbackYRatio;
    public static double wardenSonicBoomHorizontalRange;
    public static double wardenSonicBoomVerticalRange;
    public static int wardenVibrationCooldown;
    //public static EntityAttributesEntry glare;
    public static EntityAttributesEntry copper_golem;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deeperdepths/entities.cfg"));
        try{
            config.load();
            //general
            copperArmourChance = config.get("copperArmourChance", "General", 3, "Chance for copper armour to replace gold or leather armour on spawned mobs. (Set to 0 to disable, default is 3, meaning 1/3 are replaced)").getInt();
            //bogged
            bogged = new EntityAttributesEntry(config, "bogged", 0.25, 32, 2, 16, 0, 0, 0);
            poisonArrowDuration = config.get("bogged", "poisonArrowDuration", 4, "How long the poison from arrows last, in seconds").getInt();
            boggedAttackCooldownEasy = config.get("bogged", "attackCooldownEasy", 3.5, "The cooldown between firing on Easy, in seconds").getDouble();
            boggedAttackCooldownNormal = config.get("bogged", "attackCooldownMedium", 3.5, "The cooldown between firing on Normal, in seconds").getDouble();
            boggedAttackCooldownHard = config.get("bogged", "attackCooldownHard", 2.5, "The cooldown between firing on Hard, in seconds").getDouble();
            //breeze
            breeze = new EntityAttributesEntry(config, "breeze", 0.63, 24, 3, 30, 0, 0, 0);
            //warden
            warden = new EntityAttributesEntry(config, "warden", 0.3, 32, 30, 500, 0, 0, 1);
            wardenAngerAgitatedMin = config.get("warden", "angerAgitatedMin", 40, "Minimum amount of anger at a target for the warden to be considered agitated?", 1, Integer.MAX_VALUE).getInt();
            wardenAngerAngryMin = config.get("warden", "angerAngryMin", 80, "Minimum amount of anger at a target for the warden to be considered angry?", 1, Integer.MAX_VALUE).getInt();
            wardenAngerDecayTicks = config.get("warden", "angerDecayTicks", 20, "Amount of ticks until a wardens anger at each entity decays", 0, Integer.MAX_VALUE).getInt();
            wardenAngerDecayAmount = config.get("warden", "angerDecayAmount", 1, "Amount of anger a warden loses per entity when anger decays").getInt();
            wardenAngerGainAttacked = config.get("warden", "angerGainAttacked", 100, "Amount of anger a warden gains towards a target when it takes damage from them?").getInt();
            wardenAngerGainCollision = config.get("warden", "angerGainCollision", 35, "Amount of anger a warden gains towards a target when it makes direct contact with them?").getInt();
            wardenAngerGainRoar = config.get("warden", "angerGainRoar", 20, "Amount of anger a warden gains towards a target when it roars at them?").getInt();
            wardenAngerGainSniff = config.get("warden", "angerGainSniff", 35, "Amount of anger a warden gains towards a target if they are the closest entity when it finishes sniffing?").getInt();
            wardenAngerGainVibration = config.get("warden", "angerGainVibration", 35, "Amount of anger a warden gains towards a target when it receives a vibration from them?").getInt();
            wardenAngerMax = config.get("warden", "angerMax", 150, "Maximum amount of anger a warden can have?", 1, Integer.MAX_VALUE).getInt();
            wardenBurrowingCooldown = config.get("warden", "burrowingCooldown", 1200, "How many ticks after a warden receives a vibration until it can burrow and despawn").getInt();
            wardenDarknessFrequency = config.get("warden", "darknessFrequency", 120, "How many ticks before the warden applies darkness to nearby players?").getInt();
            wardenDarknessLength = config.get("warden", "darknessLength", 260, "How many ticks does a warden give darkness for?").getInt();
            wardenDarknessRange = config.get("warden", "darknessRange", 20, "How close to a warden does a player need to get the darkness effect?").getDouble();
            wardenSniffCooldownMin = config.get("warden", "sniffCooldownMin", 100, "Minimum number of random ticks after a warden ticks until it can try to sniff again", 0, Integer.MAX_VALUE).getInt();
            wardenSniffCooldownMax = config.get("warden", "sniffCooldownMax", 200, "Maximum number of random ticks after a warden ticks until it can try to sniff again", 0, Integer.MAX_VALUE).getInt();
            wardenSniffHorizontalRange = config.get("warden", "sniffHorizontalRange", 6, "How far can the warden smell entities from, while sniffing, on the x and z axis?").getDouble();
            wardenSniffVerticalRange = config.get("warden", "sniffVerticalRange", 20, "How far can the warden smell entities from, while sniffing, on the y axis?").getDouble();
            wardenSonicBoomCooldown = config.get("warden", "sonicBoomCooldown", 40, "How many ticks of not being able to reach its target until a warden can use it's sonic boom attack").getInt();
            wardenSonicBoomDamage = config.get("warden", "sonicBoomDamage", 10, "How much damage does the warden's sonic boom attack do? (Scaled by difficulty against players," +
                    " on easy 0.5x damage + 1, on hard 1.5x damage)").getDouble();
            wardenSonicBoomKnockback = config.get("warden", "sonicBoomKnockback", 2.5, "How much knockback does the warden's sonic boom attack do?").getDouble();
            wardenSonicBoomKnockbackYRatio = config.get("warden", "sonicBoomKnockbackYRatio", 2.5, "How much does the warden's sonic boom knockback get scaled by on the y axis?").getDouble();
            wardenSonicBoomHorizontalRange = config.get("warden", "sonicBoomHorizontalRange", 14, "How far can the warden shoot a sonic boom on the x and z axis?").getDouble();
            wardenSonicBoomVerticalRange = config.get("warden", "sonicBoomVerticalRange", 20, "How far can the warden shoot a sonic boom on the y axis?").getDouble();
            wardenVibrationCooldown = config.get("warden", "vibrationCooldown", 40, "How many ticks after a warden receives a vibration until it can receive another one").getInt();
            //copper golem
            copper_golem = new EntityAttributesEntry(config, "copper_golem", 0.3, 32, 0, 12, 0, 0, 0);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
