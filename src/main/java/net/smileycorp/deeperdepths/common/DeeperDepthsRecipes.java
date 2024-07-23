package net.smileycorp.deeperdepths.common;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumStoneType;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;
import net.smileycorp.deeperdepths.common.items.DeeperDepthsItems;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsRecipes {
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent<IRecipe> event) {
        registerOreDictionary();
        registerFurnaceRecipes();
        registerCraftingRecipes();
    }
    
    private static void registerOreDictionary() {
        OreDictionary.registerOre("ingotCopper", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 0));
        OreDictionary.registerOre("gemAmethyst", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 1));
        OreDictionary.registerOre("stone", DeeperDepthsBlocks.DEEPSLATE);
        OreDictionary.registerOre("stoneDeepslate", DeeperDepthsBlocks.DEEPSLATE);
        OreDictionary.registerOre("stoneTuff", new ItemStack(DeeperDepthsBlocks.STONE, 1, 0));
        OreDictionary.registerOre("stoneCalcite", new ItemStack(DeeperDepthsBlocks.STONE, 1, 5));
        OreDictionary.registerOre("cobblestone", new ItemStack(DeeperDepthsBlocks.STONE, 1, 6));
        OreDictionary.registerOre("cobblestoneDeepslate", new ItemStack(DeeperDepthsBlocks.STONE, 1, 6));
        OreDictionary.registerOre("oreCopper", new ItemStack(DeeperDepthsBlocks.COPPER_ORE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockCopper", new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockCopperCut", new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockCopperChiseled", new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, OreDictionary.WILDCARD_VALUE));
    }
    
    private static void registerFurnaceRecipes() {
        GameRegistry.addSmelting(new ItemStack(DeeperDepthsBlocks.COPPER_ORE),
                new ItemStack(DeeperDepthsItems.MATERIALS, 1, 0), 0.7f);
        GameRegistry.addSmelting(new ItemStack(DeeperDepthsBlocks.STONE, 1, 6),
                new ItemStack(DeeperDepthsBlocks.DEEPSLATE), 0.1f);
        GameRegistry.addSmelting(new ItemStack(DeeperDepthsBlocks.STONE, 1, 9),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 10), 0.1f);
        GameRegistry.addSmelting(new ItemStack(DeeperDepthsBlocks.STONE, 1, 11),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 12), 0.1f);
    }
    
    //this should be done with jsons probably, but I'm lazy, I may switch them over to be json files eventually
    private static void registerCraftingRecipes() {
        //tuff
        GameRegistry.addShapedRecipe(Constants.loc("polished_tuff"), Constants.loc("tuff"),
                new ItemStack(DeeperDepthsBlocks.STONE, 4, 1), "MM", "MM", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 0));
        GameRegistry.addShapedRecipe(Constants.loc("chiseled_tuff"), Constants.loc("tuff"),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 2), "M", "M", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, 0));
        GameRegistry.addShapedRecipe(Constants.loc("tuff_bricks"), Constants.loc("tuff"),
                new ItemStack(DeeperDepthsBlocks.STONE, 4, 3), "MM", "MM", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 1));
        GameRegistry.addShapedRecipe(Constants.loc("chiseled_tuff_bricks"), Constants.loc("tuff"),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 4), "M", "M", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, 2));
        //deepslate
        GameRegistry.addShapedRecipe(Constants.loc("chiseled_deepslate"), Constants.loc("deepslate"),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 7), "M", "M", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, 3));
        GameRegistry.addShapedRecipe(Constants.loc("polished_deepslate"), Constants.loc("deepslate"),
                new ItemStack(DeeperDepthsBlocks.STONE, 4, 8), "MM", "MM", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 6));
        GameRegistry.addShapedRecipe(Constants.loc("deepslate_bricks"), Constants.loc("deepslate"),
                new ItemStack(DeeperDepthsBlocks.STONE, 4, 9), "MM", "MM", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 8));
        GameRegistry.addShapedRecipe(Constants.loc("deepslate_tiles"), Constants.loc("deepslate"),
                new ItemStack(DeeperDepthsBlocks.STONE, 4, 11), "MM", "MM", 'M',
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 9));
        //shaped stone items
        for (EnumStoneType variant : EnumStoneType.SHAPED_TYPES) {
            GameRegistry.addShapedRecipe(Constants.loc(variant.getName() + "_slab"), Constants.loc(variant.getName()),
                    new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 6, variant.getShapedMeta()), "MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.STONE, 1, variant.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc(variant.getName() + "_stairs"), Constants.loc(variant.getName()),
                    new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(variant), 4), true, "M  ", "MM ", "MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.STONE, 1, variant.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc(variant.getName() + "_wall"), Constants.loc(variant.getName()),
                    new ItemStack(DeeperDepthsBlocks.STONE_WALL, 6, variant.getShapedMeta()), "MMM", "MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.STONE, 1, variant.ordinal()));
        }
        //copper
        GameRegistry.addShapedRecipe(Constants.loc("copper_block"), Constants.loc("copper"),
                new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, 0), "MM", "MM", 'M',
                "ingotCopper");
        GameRegistry.addShapedRecipe(Constants.loc("copper_ingot"), Constants.loc("copper"),
                new ItemStack(DeeperDepthsItems.MATERIALS, 9, 0), "M", 'M',
                "blockCopper");
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            String name = stage == EnumWeatherStage.NORMAL ? "" : stage.getName() + "_";
            GameRegistry.addShapedRecipe(Constants.loc(name + "cut_copper"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 4, stage.ordinal()), "MM", "MM", 'M',
                    new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc("waxed_" + name + "cut_copper"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 4, stage.ordinal() + 4), "MM", "MM", 'M',
                    new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal() + 4));
            GameRegistry.addShapedRecipe(Constants.loc(stage.getName() + "cut_copper_slab"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 6, stage.ordinal()),"MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc("waxed_" + stage.getName() + "cut_copper_slab"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 6, stage.ordinal() + 4),"MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal() + 4));
            GameRegistry.addShapedRecipe(Constants.loc(stage.getName() + "cut_copper_stairs"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage), 4), true,"M  ", "MM ", "MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc("waxed_" + stage.getName() + "cut_copper_stairs"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.WAXED_CUT_COPPER_STAIRS.get(stage), 4), true,"M  ", "MM ", "MMM", 'M',
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal() + 4));
            GameRegistry.addShapedRecipe(Constants.loc(name + "chiseled_copper"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal()), "M", "M", 'M',
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, stage.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc("waxed_" + name + "chiseled_copper"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal() + 4), "M", "M", 'M',
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, stage.ordinal() + 4));
            GameRegistry.addShapedRecipe(Constants.loc(name + "copper_grate"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 4, stage.ordinal()), " M ", "M M", " M ", 'M',
                    new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()));
            GameRegistry.addShapedRecipe(Constants.loc("waxed_" + name + "copper_grate"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 4, stage.ordinal() + 4), " M ", "M M", " M ", 'M',
                    new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal() + 4));
            GameRegistry.addShapedRecipe(Constants.loc(name + "copper_bulb"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.COPPER_BULB, 4, stage.ordinal()), " M ", "MBM", " R ", 'M',
                    new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()), 'B', Items.BLAZE_ROD, 'R', "dustRedstone");
            GameRegistry.addShapedRecipe(Constants.loc("waxed_" + name + "copper_bulb"), Constants.loc("copper"),
                    new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_BULB, 4, stage.ordinal()), " M ", "MBM", " R ", 'M',
                    new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal() + 4), 'B', Items.BLAZE_ROD, 'R', "dustRedstone");
        }
        GameRegistry.addShapedRecipe(Constants.loc("copper_trapdoor"), Constants.loc("copper"),
                new ItemStack(DeeperDepthsBlocks.COPPER_TRAPDOORS.get(EnumWeatherStage.NORMAL), 2, 0), "MMM", "MMM", 'M',
                "ingotCopper");
    }
    
}