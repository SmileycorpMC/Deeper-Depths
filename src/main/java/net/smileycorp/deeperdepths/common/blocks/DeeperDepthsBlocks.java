package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.smileycorp.atlas.api.block.ShapedBlock;

import java.util.Map;

public class DeeperDepthsBlocks {

    //deepslate
    public static final Block DEEPSLATE = null;
    public static final ShapedBlock COBBLED_DEEPSLATE = null;
    public static final Block CHISELED_DEEPSLATE = null;
    public static final ShapedBlock POLISHED_DEEPSLATE = null;
    public static final ShapedBlock DEEPSLATE_BRICKS = null;
    public static final Block CRACKED_DEEPSLATE_BRICKS = null;
    public static final ShapedBlock DEEPSLATE_TILES = null;
    public static final Block CRACKED_DEEPSLATE_TILES = null;
    public static final Block REINFORCED_DEEPSLATE = null;
    
    //tuff
    public static final ShapedBlock TUFF = null;
    public static final Block CHISELED_TUFF = null;
    public static final ShapedBlock POLISHED_TUFF = null;
    public static final ShapedBlock TUFF_BRICKS = null;
    public static final Block CHISELED_TUFF_BRICKS = null;

    //geodes
    public static final Block CALCITE = null;
    public static final Block BLOCK_OF_AMETHYST = null;
    public static final Block AMETHYST_BUD = null;
    
    //copper
    //public static final Block COPPER_ORE = null;
    public static final Block COPPER_BLOCK = new BlockCopper("copper_block");
    public static final Block LIGHTNING_ROD = null;
    public static final Block CUT_COPPER = new BlockCopper("cut_copper_block");
    public static final Block CUT_COPPER_SLAB = new BlockCopperSlab("cut_copper_slab", false);
    public static final Block DOUBLE_CUT_COPPER_SLAB = new BlockCopperSlab("cut_copper_slab", true);
    
    /*public static final Block CUT_COPPER_STAIRS = null;
    public static final Block EXPOSED_CUT_COPPER_STAIRS = null;
    public static final Block WEATHERED_CUT_COPPER_STAIRS = null;
    public static final Block OXIDIZED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_EXPOSED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_WEATHERED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_OXIDIZED_CUT_COPPER_STAIRS = null;
    public static final Block CHISELED_COPPER = null;
    public static final Block COPPER_GRATE = null;
    public static final Block COPPER_DOOR = null;
    public static final Block EXPOSED_COPPER_DOOR = null;
    public static final Block WEATHERED_COPPER_DOOR = null;
    public static final Block OXIDIZED_COPPER_DOOR = null;
    public static final Block WAXED_COPPER_DOOR = null;
    public static final Block WAXED_EXPOSED_COPPER_DOOR = null;
    public static final Block WAXED_WEATHERED_COPPER_DOOR = null;
    public static final Block WAXED_OXIDIZED_COPPER_DOOR = null;
    public static final Block COPPER_TRAPDOOR = null;
    public static final Block EXPOSED_COPPER_TRAPDOOR = null;
    public static final Block WEATHERED_COPPER_TRAPDOOR = null;
    public static final Block OXIDIZED_COPPER_TRAPDOOR = null;
    public static final Block WAXED_COPPER_TRAPDOOR = null;
    public static final Block WAXED_EXPOSED_COPPER_TRAPDOOR = null;
    public static final Block WAXED_WEATHERED_COPPER_TRAPDOOR = null;
    public static final Block WAXED_OXIDIZED_COPPER_TRAPDOOR = null;*/
    
    //functional blocks
    public static final Block COPPER_BULB = null;
    public static final Map<EnumDyeColor, Block> CANDLES = Maps.newEnumMap(EnumDyeColor.class);
    public static final Block TRIAL_SPAWNER = null;
    public static final Block TRIAL_VAULT = null;
    
    //sculk
    public static final Block SCULK = null;
    public static final Block SCULK_VEIN = null;
    public static final Block SCULK_SHRIEKER = null;
    public static final Block SCULK_SENSOR = null;
    public static final Block CALIBRATED_SCULK_SENSOR = null;
    
}
