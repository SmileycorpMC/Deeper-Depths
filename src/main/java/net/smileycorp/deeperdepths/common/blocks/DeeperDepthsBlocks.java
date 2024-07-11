package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockStairsBase;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsBlocks {
    
    public static final Set<Block> BLOCKS = Sets.newHashSet();

    public static final Block DEEPSLATE = new BlockDeepslate();
    public static final Block STONE = new BlockDDStone();
    public static final BlockSlab STONE_SLAB = new BlockDDStoneSlab(false);
    public static final BlockSlab DOUBLE_STONE_SLAB = new BlockDDStoneSlab(true);
    public static final Map<EnumStoneType, BlockStairs> STAIRS = Maps.newEnumMap(EnumStoneType.class);
    
    //copper
    public static final Block COPPER_ORE = new BlockDeeperDepths("copper_ore", Material.ROCK, 3, 3, 1);
    public static final Block COPPER_BLOCK = new BlockCopper("copper_block");
    //public static final Block LIGHTNING_ROD = null;
    public static final Block CUT_COPPER = new BlockCopper("cut_copper");
    public static final BlockSlab CUT_COPPER_SLAB = new BlockCopperSlab("cut_copper_slab", false);
    public static final BlockSlab DOUBLE_CUT_COPPER_SLAB = new BlockCopperSlab("cut_copper_slab", true);
    
    public static final Block CHISELED_COPPER = new BlockCopper("chiseled_copper");
    public static final Block COPPER_GRATE = new BlockCopperGrate();
    
    /*public static final Block CUT_COPPER_STAIRS = null;
    public static final Block EXPOSED_CUT_COPPER_STAIRS = null;
    public static final Block WEATHERED_CUT_COPPER_STAIRS = null;
    public static final Block OXIDIZED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_EXPOSED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_WEATHERED_CUT_COPPER_STAIRS = null;
    public static final Block WAXED_OXIDIZED_CUT_COPPER_STAIRS = null;
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
   /* public static final Block COPPER_BULB = null;
    public static final Map<EnumDyeColor, Block> CANDLES = Maps.newEnumMap(EnumDyeColor.class);
    public static final Block TRIAL_SPAWNER = null;
    public static final Block TRIAL_VAULT = null;*/
    
    //sculk
    /*public static final Block SCULK = null;
    public static final Block SCULK_VEIN = null;
    public static final Block SCULK_SHRIEKER = null;
    public static final Block SCULK_SENSOR = null;
    public static final Block CALIBRATED_SCULK_SENSOR = null;*/
    
    //this is a terrible way of doing it, but I'm feeling lazy for this part
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Field field : DeeperDepthsBlocks.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Block) || object == null) continue;
                register(registry, (Block) object);
            } catch (Exception e) {
                DeeperDepths.error(field, e);
            }
        }
        for (EnumStoneType type : EnumStoneType.SHAPED_TYPES)
            STAIRS.put(type, register(registry, new BlockStairsBase(type.getName(), STONE.getDefaultState().withProperty(BlockDDStone.VARIANT, type))));
    }
    
    private static <T extends Block> T register(IForgeRegistry<Block> registry, T block) {
        registry.register(block);
        if (!(block instanceof BlockSlab)) BLOCKS.add(block);
        return block;
    }
    
}
