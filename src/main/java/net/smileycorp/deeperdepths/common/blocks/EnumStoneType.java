package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

import java.util.List;

public enum EnumStoneType implements IStringSerializable {
    
    TUFF("tuff", Material.TUFF, true),
    POLISHED_TUFF("polished_tuff", Material.TUFF, true),
    CHISELED_TUFF("chiseled_tuff", Material.TUFF, false),
    TUFF_BRICKS("tuff_bricks", Material.TUFF, true),
    CHISELED_TUFF_BRICKS("chisled_tuff_bricks", Material.TUFF, false),
    CALCITE("calcite", Material.CALCITE, false),
    COBBLED_DEEPSLATE("cobbled_deepslate", Material.DEEPSLATE, true),
    CHISELED_DEEPSLATE("chiseled_deepslate", Material.DEEPSLATE, false),
    POLISHED_DEEPSLATE("polished_deepslate", Material.DEEPSLATE, true),
    DEEPSLATE_BRICKS("deepslate_bricks", Material.DEEPSLATE, true),
    CRACKED_DEEPSLATE_BRICKS("cracked_deepslate_bricks", Material.DEEPSLATE, false),
    DEEPSLATE_TILES("deepslate_tiles", Material.DEEPSLATE, true),
    CRACKED_DEEPSLATE_TILES("cracked_deepslate_tiles", Material.DEEPSLATE, false);
    
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
        return material.hardness;
    }
    
    public float getResistance() {
        return material.resistance;
    }
    
    public boolean hasVariants() {
        return hasVariants;
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
    
    public enum Material {
        
        TUFF(MapColor.GRAY_STAINED_HARDENED_CLAY, 1.5f, 6),
        CALCITE(MapColor.WHITE_STAINED_HARDENED_CLAY, 0.75f, 0.75f),
        DEEPSLATE(MapColor.GRAY, 3.5f, 6);
        
        private final MapColor color;
        private final float hardness, resistance;
        
        Material(MapColor color, float hardness, float resistance) {
            this.color = color;
            this.hardness = hardness;
            this.resistance = resistance;
        }
        
    }
    
}
