package net.smileycorp.deeperdepths.common.blocks.enums;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public enum EnumWeatherStage implements IStringSerializable {
   
    NORMAL("normal", MapColor.ADOBE, 0.75f),
    EXPOSED("exposed", MapColor.SILVER_STAINED_HARDENED_CLAY, 1f),
    WEATHERED("weathered", MapColor.CYAN, 1f),
    OXIDIZED("oxidized", MapColor.DIAMOND, 0);
    
    private final String name;
    private final MapColor color;
    private final float ageModifier;
    
    EnumWeatherStage(String name, MapColor color, float ageModifier) {
        this.name = name;
        this.color = color;
        this.ageModifier = ageModifier;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getUnlocalizedName() {
        return this == NORMAL ? "" : name;
    }
    
    public MapColor getMapColor() {
        return color;
    }
    
    public EnumWeatherStage previous() {
       return this == NORMAL ? NORMAL : values()[ordinal() - 1];
    }
    
    public EnumWeatherStage next() {
        return this == OXIDIZED ? OXIDIZED : values()[ordinal() + 1];
    }
    
    public float getAgeModifier() {
        return ageModifier;
    }
    
    public static EnumWeatherStage fromName(String name) {
        for (EnumWeatherStage stage : values()) if (stage.name.equals(name)) return stage;
        return null;
    }
    
}
