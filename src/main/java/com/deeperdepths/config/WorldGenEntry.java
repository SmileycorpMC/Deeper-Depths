package com.deeperdepths.config;

import net.minecraftforge.common.config.Configuration;

public class WorldGenEntry {
    
    private int size;
    private int spawnChances;
    private int minHeight;
    private int maxHeight;

    private int[] dimensions;
    
    public WorldGenEntry(Configuration config, String name, int size, int spawnChances, int minHeight, int maxHeight) {
        this.size = config.get(name, "size", size, "Size of block generation to generate").getInt();
        this.spawnChances = config.get(name, "spawnChances", spawnChances, "Number of chances for the block to generate (Set to 0 to disable generation)").getInt();
        this.minHeight = config.get(name, "minHeight", minHeight, "Minimum Y level the block can generate. (Setting below 0 or above 255 may cause issues if a world extension mod is not installed)").getInt();
        this.maxHeight = config.get(name, "maxHeight", maxHeight, "Maximum Y level the block can generate. (Setting below 0 or above 255 may cause issues if a world extension mod is not installed)").getInt();
    }

    public WorldGenEntry(Configuration config, String name, int spawnChances, int[] dimensionsTooSpawnIn, int yHeight) {
        this.spawnChances = config.get(name, "spawnChances", spawnChances, "Change the distance between each Trial Chambers, lower means more frequent, higher means less. Set to 0 to disable").getInt();
        this.minHeight = config.get(name, "Y-Height", yHeight, "Change the Y height the Trial Chambers spawn at, take note this y height is for the bottom corner of the entry room").getInt();
        this.dimensions = config.get(name, "ValidDimensions", dimensionsTooSpawnIn, "add valid dimensions to spawn for the Trial Chambers to spawn in.").getIntList();
    }


    public int getMaxHeight() {
        return maxHeight;
    }
    
    public int getMinHeight() {
        return minHeight;
    }
    
    public int getSpawnChances() {
        return spawnChances;
    }
    
    public int getSize() {
        return size;
    }

    public int[] getDimensions() {return dimensions;}
    
}
