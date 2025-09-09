package com.deeperdepths.common.blocks.enums;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.config.BlockConfig;
import com.deeperdepths.config.BlockStatEntry;
import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.List;

public enum EnumStoneType implements IStringSerializable {
    
    TUFF("tuff", Material.TUFF, true),
    POLISHED_TUFF("polished_tuff", Material.TUFF, true),
    CHISELED_TUFF("chiseled_tuff", Material.TUFF, false),
    TUFF_BRICK("tuff_brick", Material.TUFF_BRICKS, true),
    CHISELED_TUFF_BRICK("chiseled_tuff_brick", Material.TUFF_BRICKS, false),
    CALCITE("calcite", Material.CALCITE, false),
    COBBLED_DEEPSLATE("cobbled_deepslate", Material.DEEPSLATE, true),
    CHISELED_DEEPSLATE("chiseled_deepslate", Material.DEEPSLATE, false),
    POLISHED_DEEPSLATE("polished_deepslate", Material.DEEPSLATE, true),
    DEEPSLATE_BRICK("deepslate_brick", Material.DEEPSLATE_BRICKS, true),
    CRACKED_DEEPSLATE_BRICK("cracked_deepslate_brick", Material.DEEPSLATE_BRICKS, false),
    DEEPSLATE_TILE("deepslate_tile", Material.DEEPSLATE_TILES, true),
    CRACKED_DEEPSLATE_TILE("cracked_deepslate_tile", Material.DEEPSLATE_TILES, false);
    
    public static final List<EnumStoneType> SHAPED_TYPES = getShapedTypes();
    
    private static List<EnumStoneType> getShapedTypes() {
        List<EnumStoneType> list = Lists.newArrayList();
        for (EnumStoneType type : values()) if (type.hasVariants) list.add(type);
        return list;
    }
    
    private final String name;
    private final Material material;
    private final boolean hasVariants;
    
    EnumStoneType(String name, Material material, boolean hasVariants) {
        this.name = name;
        this.material = material;
        this.hasVariants = hasVariants;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public MapColor getMapColor() {
        return material.color;
    }
    
    public float getHardness() {
        return material.stats.getHardness();
    }
    
    public float getResistance() {
        return material.stats.getResistance();
    }
    
    public int getHarvestLevel() {
        return material.stats.getHarvestLevel();
    }
    
    public int getShapedMeta() {
        return SHAPED_TYPES.indexOf(this);
    }
    
    public static EnumStoneType get(int meta) {
        return meta < values().length ? values()[meta] : TUFF;
    }
    
    public static EnumStoneType getShaped(int meta) {
        return meta < SHAPED_TYPES.size() ? SHAPED_TYPES.get(meta) : TUFF;
    }
    
    public static EnumStoneType fromName(String name) {
        for (EnumStoneType type : values()) if (type.name.equals(name)) return type;
        return null;
    }
    
    public SoundType getSoundType() {
        return material.soundType;
    }
    
    public boolean hasVariants() {
        return hasVariants;
    }

    public ItemStack getStack() {
        return new ItemStack(DeeperDepthsBlocks.STONE, 1, ordinal());
    }

    public enum Material {
        
        DEEPSLATE(MapColor.GRAY, BlockConfig.cobbledDeepslate, DeeperDepthsSoundTypes.DEEPSLATE),
        DEEPSLATE_BRICKS(MapColor.GRAY, BlockConfig.deepslateBricks, DeeperDepthsSoundTypes.DEEPSLATE_BRICKS),
        DEEPSLATE_TILES(MapColor.GRAY, BlockConfig.deepslateTiles, DeeperDepthsSoundTypes.DEEPSLATE_TILES),
        TUFF(MapColor.GRAY_STAINED_HARDENED_CLAY, BlockConfig.tuff, DeeperDepthsSoundTypes.TUFF),
        TUFF_BRICKS(MapColor.GRAY_STAINED_HARDENED_CLAY,  BlockConfig.tuffBricks, DeeperDepthsSoundTypes.TUFF_BRICKS),
        CALCITE(MapColor.WHITE_STAINED_HARDENED_CLAY, BlockConfig.calcite, DeeperDepthsSoundTypes.CALCITE);
        
        private final MapColor color;
        private final BlockStatEntry stats;
        private final SoundType soundType;
        private final List<EnumStoneType> types = Lists.newArrayList();
        
        Material(MapColor color, BlockStatEntry stats, SoundType soundType) {
            this.color = color;
            this.stats = stats;
            this.soundType = soundType;
        }
        
        public List<EnumStoneType> getTypes() {
            if (types.isEmpty()) for (EnumStoneType type : EnumStoneType.values()) if (type.material == this) types.add(type);
            return types;
        }
        
    }
    
}
