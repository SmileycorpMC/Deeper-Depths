package com.deeperdepths.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class DeeperDepthsLootTables
{
    public static final ResourceLocation TRIAL_CHEST_LOOT = Constants.loc("chamber_chest_loot");
    public static final ResourceLocation TRIAL_VAULT_LOOT = Constants.loc("vault");
    public static final ResourceLocation TRIAL_VAULT_LOOT_OMINOUS = Constants.loc("ominous_vault");
    public static final ResourceLocation TRIAL_SPAWNER_LOOT = Constants.loc("trial_spawner");
    public static final ResourceLocation TRIAL_SPAWNER_LOOT_OMINOUS = Constants.loc("ominous_trial_spawner");
    public static final ResourceLocation TRIAL_SPAWNER_KEY = Constants.loc("trial_key");
    public static final ResourceLocation TRIAL_SPAWNER_KEY_OMINOUS = Constants.loc("ominous_trial_key");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_PROJECTILES = Constants.loc("ominous_trial_projectiles");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_HELMETS = Constants.loc("equipment/ominous_trial_helmets");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_CHESTPLATES = Constants.loc("equipment/ominous_trial_chestplates");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_LEGGINGS = Constants.loc("equipment/ominous_trial_leggings");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_BOOTS = Constants.loc("equipment/ominous_trial_boots");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_MELEE_WEAPONS = Constants.loc("equipment/ominous_trial_melee_weapons");
    public static final ResourceLocation TRIAL_SPAWNER_OMINOUS_RANGED_WEAPONS = Constants.loc("equipment/ominous_trial_ranged_weapons");
    public static final ResourceLocation BOGGED_DROPS = Constants.loc("entities/bogged");
    public static final ResourceLocation BOGGED_SHEAR = Constants.loc("entities/bogged_shear");
    public static final ResourceLocation BREEZE_DROPS = Constants.loc("entities/breeze");

    public static void registerLootTables()
    {
        LootTableList.register(TRIAL_CHEST_LOOT);
        LootTableList.register(TRIAL_VAULT_LOOT);
        LootTableList.register(TRIAL_VAULT_LOOT_OMINOUS);
        LootTableList.register(TRIAL_SPAWNER_LOOT);
        LootTableList.register(TRIAL_SPAWNER_LOOT_OMINOUS);
        LootTableList.register(TRIAL_SPAWNER_KEY);
        LootTableList.register(TRIAL_SPAWNER_KEY_OMINOUS);
        LootTableList.register(TRIAL_SPAWNER_OMINOUS_PROJECTILES);
        LootTableList.register(BOGGED_DROPS);
        LootTableList.register(BOGGED_SHEAR);
        LootTableList.register(BREEZE_DROPS);
    }
}