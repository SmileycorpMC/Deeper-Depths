package net.smileycorp.deeperdepths.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class DeeperDepthsLootTables
{
    public static final ResourceLocation TRIAL_CHEST_LOOT = new ResourceLocation(Constants.MODID, "chamber_chest_loot");
    public static final ResourceLocation TRIAL_VAULT_LOOT = new ResourceLocation(Constants.MODID, "vault");
    public static final ResourceLocation TRIAL_VAULT_LOOT_OMINOUS = new ResourceLocation(Constants.MODID, "omionous_vault");

    public static final ResourceLocation BOGGED_DROPS = new ResourceLocation(Constants.MODID, "entities/bogged");

    public static void registerLootTables()
    {
        LootTableList.register(TRIAL_CHEST_LOOT);
        LootTableList.register(TRIAL_VAULT_LOOT);
        LootTableList.register(TRIAL_VAULT_LOOT_OMINOUS);
        LootTableList.register(BOGGED_DROPS);
    }
}