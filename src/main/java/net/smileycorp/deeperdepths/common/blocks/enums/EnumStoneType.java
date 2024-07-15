package net.smileycorp.deeperdepths.common.blocks.enums;

import com.google.common.collect.Lists;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

import java.util.List;
import java.util.Locale;

public enum EnumStoneType implements IStringSerializable {
    
    TUFF("Tuff", Material.TUFF, true),
    POLISHED_TUFF("Polished_Tuff", Material.TUFF, true),
    CHISELED_TUFF("Chiseled_Tuff", Material.TUFF, false),
    TUFF_BRICK("Tuff_Brick", Material.TUFF, true),
    CHISELED_TUFF_BRICK("Chiseled_Tuff_Brick", Material.TUFF, false),
    CALCITE("Calcite", Material.CALCITE, false),
    COBBLED_DEEPSLATE("Cobbled_Deepslate", Material.DEEPSLATE, true),
    CHISELED_DEEPSLATE("Chiseled_Deepslate", Material.DEEPSLATE, false),
    POLISHED_DEEPSLATE("Polished_Deepslate", Material.DEEPSLATE, true),
    DEEPSLATE_BRICK("Deepslate_Brick", Material.DEEPSLATE, true),
    CRACKED_DEEPSLATE_BRICK("Cracked_Deepslate_Brick", Material.DEEPSLATE, false),
    DEEPSLATE_TILE("Deepslate_Tile", Material.DEEPSLATE, true),
    CRACKED_DEEPSLATE_TILE("Cracked_Deepslate_Tile", Material.DEEPSLATE, false);
    
    public static final List<EnumStoneType> SHAPED_TYPES = getShapedTypes();
    
    private static List<EnumStoneType> getShapedTypes() {
        List<EnumStoneType> list = Lists.newArrayList();
        for (EnumStoneType type : values()) if (type.hasVariants) list.add(type);
        return list;
    }
    
    private final String name, unlocalizedName;
    private final Material material;
    private final boolean hasVariants;
    
    EnumStoneType(String name, Material material, boolean hasVariants) {
        this.name = name.toLowerCase(Locale.US);
        this.unlocalizedName = name.replace("_", "");
        this.material = material;
        this.hasVariants = hasVariants;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getUnlocalizedName() {
        return unlocalizedName;
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
