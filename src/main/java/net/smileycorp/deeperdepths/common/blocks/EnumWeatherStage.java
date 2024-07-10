package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public enum EnumWeatherStage implements IStringSerializable {
   
    NORMAL("normal", MapColor.ADOBE),
    EXPOSED("exposed", MapColor.SILVER_STAINED_HARDENED_CLAY),
    WEATHERED("weathered", MapColor.CYAN),
    OXIDIZED("oxidized", MapColor.DIAMOND);
    
    private final String name;
    private final MapColor color;
    
    EnumWeatherStage(String name, MapColor color) {
        this.name = name;
        this.color = color;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public MapColor getMapColor() {
        return color;
    }
    
    public static EnumWeatherStage fromName(String name) {
        for (EnumWeatherStage stage : values()) if (stage.name.equals(name)) return stage;
        return null;
    }
    
}
