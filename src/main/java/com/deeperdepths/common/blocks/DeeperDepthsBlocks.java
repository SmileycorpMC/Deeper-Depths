package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.blocks.enums.EnumClusterSize;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import com.deeperdepths.common.blocks.tiles.TileTrialPot;
import com.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import com.deeperdepths.common.blocks.tiles.TileVault;
import com.deeperdepths.config.BlockConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.util.TextUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsBlocks {
    
    public static final List<Block> BLOCKS = Lists.newArrayList();

    public static final BlockDeepslate DEEPSLATE = new BlockDeepslate();
    public static final BlockDDStone STONE = new BlockDDStone();
    public static final BlockDDStoneSlab STONE_SLAB = new BlockDDStoneSlab(false);
    public static final BlockDDStoneSlab DOUBLE_STONE_SLAB = new BlockDDStoneSlab(true);
    public static final Map<EnumStoneType, BlockDDStairs> STONE_STAIRS = Maps.newEnumMap(EnumStoneType.class);
    public static final BlockDDStoneWall STONE_WALL = new BlockDDStoneWall();
    public static final BlockReinforcedDeepslate REINFORCED_DEEPSLATE = new BlockReinforcedDeepslate();
    
    //copper
    public static final BlockDeeperDepths COPPER_ORE = new BlockDeeperDepths("Copper_Ore", Material.ROCK, BlockConfig.copperOre.getHardness(), BlockConfig.copperOre.getHardness(), BlockConfig.copperOre.getHarvestLevel());
    public static final BlockCopper COPPER_BLOCK = new BlockCopper("Copper_Block");
    public static final BlockCopper CUT_COPPER = new BlockCopper("Cut_Copper");
    public static final BlockCutCopperSlab CUT_COPPER_SLAB = new BlockCutCopperSlab(false);
    public static final BlockCutCopperSlab DOUBLE_CUT_COPPER_SLAB = new BlockCutCopperSlab(true);
    public static final BlockCopper CHISELED_COPPER = new BlockCopper("Chiseled_Copper");
    public static final BlockCopperGrate COPPER_GRATE = new BlockCopperGrate();
    public static final BlockCopperBulb COPPER_BULB = new BlockCopperBulb(false);
    public static final BlockCopperBulb WAXED_COPPER_BULB = new BlockCopperBulb(true);
    public static final Map<EnumWeatherStage, BlockDDStairs> CUT_COPPER_STAIRS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockDDStairs> WAXED_CUT_COPPER_STAIRS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockCopperTrapdoor> COPPER_TRAPDOORS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockCopperTrapdoor> WAXED_COPPER_TRAPDOORS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockCopperDoor> COPPER_DOORS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockCopperDoor> WAXED_COPPER_DOORS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockLightningRod> LIGHTNING_RODS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockLightningRod> WAXED_LIGHTNING_RODS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockCopperBars> COPPER_BARS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final Map<EnumWeatherStage, BlockCopperBars> WAXED_COPPER_BARS = Maps.newEnumMap(EnumWeatherStage.class);
    public static final BlockCopperChest COPPER_CHEST = new BlockCopperChest(false);
    public static final BlockCopperChest WAXED_COPPER_CHEST = new BlockCopperChest(true);
    public static final BlockCopperChain COPPER_CHAINS = new BlockCopperChain(false);
    public static final BlockCopperChain WAXED_COPPER_CHAINS = new BlockCopperChain(true);
    //public static final Map<EnumWeatherStage, BlockCopperChain> COPPER_CHAINS = Maps.newEnumMap(EnumWeatherStage.class);
    //public static final Map<EnumWeatherStage, BlockCopperChain> WAXED_COPPER_CHAINS = Maps.newEnumMap(EnumWeatherStage.class);
    //public static final Map<EnumWeatherStage, BlockCopperLantern> COPPER_LANTERNS = Maps.newEnumMap(EnumWeatherStage.class);
    //public static final Map<EnumWeatherStage, BlockCopperLantern> WAXED_COPPER_LANTERNS = Maps.newEnumMap(EnumWeatherStage.class);
    //public static final BlockCopperGolemStatue GOLEM_STATUE = new BlockCopperGolemStatue(false);
    //public static final BlockCopperGolemStatue WAXED_GOLEM_STATUE = new BlockCopperGolemStatue(true);

    public static final BlockAmethyst AMETHYST_BLOCK = new BlockAmethyst("amethyst_block");
    public static final BlockBuddingAmethyst BUDDING_AMETHYST = new BlockBuddingAmethyst();
    public static final Map<EnumClusterSize, BlockAmethystBud> AMETHYST_BUDS = Maps.newEnumMap(EnumClusterSize.class);
    
    public static final BlockCandle CANDLE = new BlockCandle(null);
    public static final Map<EnumDyeColor, BlockCandle> CANDLES = Maps.newEnumMap(EnumDyeColor.class);
    public static final BlockTrialSpawner TRIAL_SPAWNER = new BlockTrialSpawner();
    public static final BlockVault VAULT = new BlockVault();
    public static final BlockTrialPot TRIAL_POT = new BlockTrialPot();
    public static final BlockHeavyCore HEAVY_CORE = new BlockHeavyCore();
    public static final BlockTintedGlass TINTED_GLASS = new BlockTintedGlass();
    public static final BlockCopperTorch COPPER_TORCH = new BlockCopperTorch();
    
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
        for (EnumStoneType type : EnumStoneType.SHAPED_TYPES)
            STONE_STAIRS.put(type, new BlockDDStairs(TextUtils.toProperCase(type.getName())
                    .replace(" ", "_"), STONE.getDefaultState().withProperty(BlockDDStone.VARIANT, type)));
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            CUT_COPPER_STAIRS.put(stage, new BlockCutCopperStairs(stage, false));
            WAXED_CUT_COPPER_STAIRS.put(stage, new BlockCutCopperStairs(stage, true));
            COPPER_TRAPDOORS.put(stage, new BlockCopperTrapdoor(stage, false));
            WAXED_COPPER_TRAPDOORS.put(stage, new BlockCopperTrapdoor(stage, true));
            COPPER_DOORS.put(stage, new BlockCopperDoor(stage, false));
            WAXED_COPPER_DOORS.put(stage, new BlockCopperDoor(stage, true));
            LIGHTNING_RODS.put(stage, new BlockLightningRod(stage, false));
            WAXED_LIGHTNING_RODS.put(stage, new BlockLightningRod(stage, true));
            COPPER_BARS.put(stage, new BlockCopperBars(stage, false));
            WAXED_COPPER_BARS.put(stage, new BlockCopperBars(stage, true));
        }
        for (EnumDyeColor color : EnumDyeColor.values()) CANDLES.put(color, new BlockCandle(color));
        for (EnumClusterSize size : EnumClusterSize.values()) AMETHYST_BUDS.put(size, new BlockAmethystBud(size));
        for (Field field : DeeperDepthsBlocks.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (object instanceof Map) {
                    for (Object o : ((Map)object).values()) if (o instanceof Block) register(registry, (Block) o);
                    continue;
                }
                if (!(object instanceof Block) || object == null) continue;
                register(registry, (Block) object);
            } catch (Exception e) {
                DeeperDepths.error(field, e);
            }
        }
        GameRegistry.registerTileEntity(TileTrialSpawner.class, Constants.loc("trial_spawner"));
        GameRegistry.registerTileEntity(TileVault.class, Constants.loc("vault"));
        GameRegistry.registerTileEntity(TileTrialPot.class, Constants.loc("pot"));
        GameRegistry.registerTileEntity(TileCopperChest.class, Constants.loc("copper_chest"));
    }
    
    private static <T extends Block> void register(IForgeRegistry<Block> registry, T block) {
        registry.register(block);
        BLOCKS.add(block);
    }
    
}
