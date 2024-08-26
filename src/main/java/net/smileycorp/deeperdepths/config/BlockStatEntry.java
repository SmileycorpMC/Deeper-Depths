package net.smileycorp.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;

public class BlockStatEntry {
    
    private float hardness, resistance;
    private int harvestLevel;
    
    public BlockStatEntry(Configuration config, String name, double hardness, double resistance, int harvestLevel) {
        this.hardness = (float) config.get(name, "hardness", hardness, "What hardness value does " + name + " have").getDouble();
        this.resistance = (float) config.get(name, "resistance", resistance, "What explosion resistance value does " + name + " have").getDouble();
        this.harvestLevel = config.get(name, "harvestLevel", harvestLevel, "What harvest level does " + name + " have (0 is wood, 1 is stone, 2 is iron, 3 is diamond ect.)").getInt();
    }
    
    public float getHardness() {
        return hardness;
    }
    
    public float getResistance() {
        return resistance;
    }
    
    public int getHarvestLevel() {
        return harvestLevel;
    }

}
