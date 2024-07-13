package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum EnumWeatherStage implements IStringSerializable {
   
    NORMAL("Normal", MapColor.ADOBE),
    EXPOSED("Exposed", MapColor.SILVER_STAINED_HARDENED_CLAY),
    WEATHERED("Weathered", MapColor.CYAN),
    OXIDIZED("Oxidized", MapColor.DIAMOND);
    
    private final String name, unlocalizedName;
    private final MapColor color;
    
    EnumWeatherStage(String name, MapColor color) {
        this.name = name.toLowerCase(Locale.US);
        this.unlocalizedName = name.replace("_", "");
        this.color = color;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getUnlocalizedName() {
        return this == NORMAL ? "" : unlocalizedName;
    }
    
    public MapColor getMapColor() {
        return color;
    }
    
    public EnumWeatherStage previous() {
       return this == NORMAL ? NORMAL : values()[ordinal() - 1];
    }
    
    public EnumWeatherStage next() {
        return this == WEATHERED ? WEATHERED : values()[ordinal() + 1];
    }
    
    public static EnumWeatherStage fromName(String name) {
        for (EnumWeatherStage stage : values()) if (stage.name.equals(name)) return stage;
        return null;
    }
    
}
