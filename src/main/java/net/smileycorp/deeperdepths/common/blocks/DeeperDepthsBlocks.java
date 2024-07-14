package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileDecoratedPot;

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
    public static final Map<EnumStoneType, BlockStairs> STONE_STAIRS = Maps.newEnumMap(EnumStoneType.class);
    
    //copper
    public static final Block COPPER_ORE = new BlockDeeperDepths("Copper_Ore", Material.ROCK, 3, 3, 1);
    public static final Block COPPER_BLOCK = new BlockCopper("Copper_Block");
    //public static final Block LIGHTNING_ROD = null;
    public static final Block CUT_COPPER = new BlockCopper("Cut_Copper");
    public static final BlockSlab CUT_COPPER_SLAB = new BlockCutCopperSlab(false);
    public static final BlockSlab DOUBLE_CUT_COPPER_SLAB = new BlockCutCopperSlab(true);
    
    public static final Block CHISELED_COPPER = new BlockCopper("Chiseled_Copper");
    public static final Block COPPER_GRATE = new BlockCopperGrate();
    public static final Block COPPER_BULB = new BlockCopperBulb(false);
    public static final Block WAXED_COPPER_BULB = new BlockCopperBulb(true);
    public static final Map<EnumWeatherStage, BlockStairs> CUT_COPPER_STAIRS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockStairs> WAXED_CUT_COPPER_STAIRS = Maps.newEnumMap(EnumWeatherStage.class);
    
   /*
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
   
    //public static final Map<EnumDyeColor, Block> CANDLES = Maps.newEnumMap(EnumDyeColor.class);
    /*public static final Block TRIAL_SPAWNER = null;
    public static final Block VAULT = null*/
    public static final Block DECORATED_POT = new BlockDecoratedPot();
    
    //sculk
    /*public static final Block SCULK = null;
    public static final Block SCULK_VEIN = null;
    public static final Block SCULK_SHRIEKER = null;
    public static final Block SCULK_SENSOR = null;
    public static final Block CALIBRATED_SCULK_SENSOR = null;*/
    
    //this is a terrible way of doing it, but I'm feeling lazy for this part
    @SubscribeEvent
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
            STONE_STAIRS.put(type, register(registry, new BlockDDStairs(TextUtils.toProperCase(type.getName())
                    .replace(" ", "_"), STONE.getDefaultState().withProperty(BlockDDStone.VARIANT, type))));
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            CUT_COPPER_STAIRS.put(stage, register(registry, new BlockCutCopperStairs(stage, false)));
            WAXED_CUT_COPPER_STAIRS.put(stage, register(registry, new BlockCutCopperStairs(stage, true)));
        }
        GameRegistry.registerTileEntity(TileDecoratedPot.class, Constants.loc("pot"));
    }
    
    private static <T extends Block> T register(IForgeRegistry<Block> registry, T block) {
        registry.register(block);
        if (!(block instanceof BlockSlab)) BLOCKS.add(block);
        return block;
    }
    
}
