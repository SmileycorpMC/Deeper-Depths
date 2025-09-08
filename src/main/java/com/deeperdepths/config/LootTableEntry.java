package com.deeperdepths.config;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.common.config.Configuration;

public class LootTableEntry {

    private int weight;
    private final ResourceLocation lootTable;
    private final String pool;
    private final Item item;
    private final int metadata;

    public LootTableEntry(Configuration config, String key, ResourceLocation lootTable, String pool, int weight, Item item) {
        this(config, key, lootTable, pool, weight, item, 0);
    }

    public LootTableEntry(Configuration config, String key, ResourceLocation lootTable, String pool, int weight, Item item, int metadata) {
        this.weight = config.get("general", key, weight, "Weight for " + item.getRegistryName()
                + " to generate in " + lootTable + " (Set to 0  to disable)").getInt();
        this.lootTable = lootTable;
        this.pool = pool;
        this.item = item;
        this.metadata = metadata;
        LootTablesConfig.ENTRIES.add(this);
    }

    public boolean canApply(ResourceLocation lootTable) {
        if (weight <= 0) return false;
        return this.lootTable.equals(lootTable);
    }

    public void addEntry(LootTable table) {
        table.getPool(pool).addEntry(new LootEntryItem(item, weight, 1,
                new LootFunction[]{new SetMetadata(new LootCondition[0], new RandomValueRange(metadata))}, new LootCondition[0],
                item.getRegistryName() + "_" + metadata));
    }

}
