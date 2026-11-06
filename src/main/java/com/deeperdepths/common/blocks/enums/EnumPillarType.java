package com.deeperdepths.common.blocks.enums;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.atlas.api.config.BlockStatEntry;

public enum EnumPillarType implements IStringSerializable {

    CALCITE("calcite", MapColor.WHITE_STAINED_HARDENED_CLAY, BlockConfig.calcite, DeeperDepthsSoundTypes.CALCITE);

    private final String name;
    private final MapColor color;
    private final BlockStatEntry stats;
    private final SoundType soundType;

    EnumPillarType(String name, MapColor color, BlockStatEntry stats, SoundType soundType) {
        this.name = name;
        this.color = color;
        this.stats = stats;
        this.soundType = soundType;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public MapColor getMapColor() {
        return color;
    }
    
    public float getHardness() {
        return stats.getHardness();
    }
    
    public float getResistance() {
        return stats.getResistance();
    }
    
    public int getHarvestLevel() {
        return stats.getHarvestLevel();
    }
    
    public static EnumPillarType get(int meta) {
        return meta < values().length ? values()[meta] : CALCITE;
    }
    
    public static EnumPillarType fromName(String name) {
        for (EnumPillarType type : values()) if (type.name.equals(name)) return type;
        return null;
    }
    
    public SoundType getSoundType() {
        return soundType;
    }

    public ItemStack getStack() {
        return new ItemStack(DeeperDepthsBlocks.PILLAR, 1, ordinal());
    }

    
}
