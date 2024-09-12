package com.deeperdepths.common.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumVaultState implements IStringSerializable {
    
    INACTIVE("inactive", false),
    ACTIVE("active", true),
    UNLOCKING("unlocking", true),
    EJECTING("ejecting", true);
    
    private final String name;
    private final boolean fullyLit;
    
    EnumVaultState(String name, boolean fullyLit) {
        this.name = name;
        this.fullyLit = fullyLit;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public int getLightLevel() {
        return fullyLit ? 12 : 6;
    }
    
}
