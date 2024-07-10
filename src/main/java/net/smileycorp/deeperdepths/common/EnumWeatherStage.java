package net.smileycorp.deeperdepths.common;

import net.minecraft.util.IStringSerializable;

public enum EnumWeatherStage implements IStringSerializable {
   
    NORMAL("normal"),
    EXPOSED("exposed"),
    WEATHERED("weathered"),
    OXIDIZED("oxidized");
    
    private final String name;
    
    EnumWeatherStage(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
