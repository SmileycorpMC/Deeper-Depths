package net.smileycorp.deeperdepths.common.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumTrialSpawnerState implements IStringSerializable {
    
    INACTIVE("inactive", false),
    ACTIVE("active", true),
    EJECTING("ejecting", true);
    
    private final String name;
    private final boolean fullyLit;
    
    EnumTrialSpawnerState(String name, boolean fullyLit) {
        this.name = name;
        this.fullyLit = fullyLit;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public int getLightLevel() {
        return fullyLit ? 8 : 4;
    }
    
}
