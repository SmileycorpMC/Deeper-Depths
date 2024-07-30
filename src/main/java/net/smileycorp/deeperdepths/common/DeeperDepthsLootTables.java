package net.smileycorp.deeperdepths.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class DeeperDepthsLootTables
{
    public static final ResourceLocation TRIAL_CHEST_LOOT = new ResourceLocation(Constants.MODID, "chamber_chest_loot");
    public static final ResourceLocation TRIAL_VAULT_LOOT = new ResourceLocation(Constants.MODID, "vault");
    public static final ResourceLocation TRIAL_VAULT_LOOT_OMINOUS = new ResourceLocation(Constants.MODID, "ominous_vault");
    public static final ResourceLocation TRIAL_SPAWNER_LOOT = new ResourceLocation(Constants.MODID, "trial_spawner");
    public static final ResourceLocation TRIAL_SPAWNER_LOOT_OMINOUS = new ResourceLocation(Constants.MODID, "ominous_trial_spawner");
    public static final ResourceLocation TRIAL_SPAWNER_KEY = new ResourceLocation(Constants.MODID, "trial_key");
    public static final ResourceLocation TRIAL_SPAWNER_KEY_OMINOUS = new ResourceLocation(Constants.MODID, "ominous_trial_key");
    public static final ResourceLocation BOGGED_DROPS = new ResourceLocation(Constants.MODID, "entities/bogged");

    public static void registerLootTables()
    {
        LootTableList.register(TRIAL_CHEST_LOOT);
        LootTableList.register(TRIAL_VAULT_LOOT);
        LootTableList.register(TRIAL_VAULT_LOOT_OMINOUS);
        LootTableList.register(TRIAL_SPAWNER_LOOT);
        LootTableList.register(TRIAL_SPAWNER_LOOT_OMINOUS);
        LootTableList.register(BOGGED_DROPS);
    }
}