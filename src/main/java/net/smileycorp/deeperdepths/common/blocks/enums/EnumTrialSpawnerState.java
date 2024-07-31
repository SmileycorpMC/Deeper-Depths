package net.smileycorp.deeperdepths.common.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumTrialSpawnerState implements IStringSerializable {
    
    INACTIVE("inactive", 0, false),
    WAITING("waiting", 4, true),
    ACTIVE("active", 8, true),
    EJECTING("ejecting", 8, false);
    
    private final String name;
    private final int light_level;
    private final boolean is_active;
    
    EnumTrialSpawnerState(String name, int light_level, boolean is_active) {
        this.name = name;
        this.light_level = light_level;
        this.is_active = is_active;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public int getLightLevel() {
        return light_level;
    }
    
    public boolean isActive() {
        return is_active;
    }
    
}
