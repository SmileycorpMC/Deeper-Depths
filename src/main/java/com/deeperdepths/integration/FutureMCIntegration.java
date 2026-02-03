package com.deeperdepths.integration;

import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.items.ICopperItem;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import thedarkcolour.futuremc.container.StonecutterContainer;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;
import thedarkcolour.futuremc.registry.FItems;

public class FutureMCIntegration {

    public static void registerRecipes() {
        OreDictionary.registerOre("wax", new ItemStack(FItems.INSTANCE.getHONEYCOMB()));
        for (EnumStoneType type : EnumStoneType.SHAPED_TYPES) addStoneRecipes(type);
        addSubStoneRecipes(EnumStoneType.TUFF, EnumStoneType.POLISHED_TUFF);
        addSubStoneRecipes(EnumStoneType.TUFF, EnumStoneType.TUFF_BRICK);
        addSubStoneRecipes(EnumStoneType.POLISHED_TUFF, EnumStoneType.TUFF_BRICK);
        addSubStoneRecipes(EnumStoneType.COBBLED_DEEPSLATE, EnumStoneType.POLISHED_DEEPSLATE);
        addSubStoneRecipes(EnumStoneType.COBBLED_DEEPSLATE, EnumStoneType.DEEPSLATE_BRICK);
        addSubStoneRecipes(EnumStoneType.COBBLED_DEEPSLATE, EnumStoneType.DEEPSLATE_TILE);
        addSubStoneRecipes(EnumStoneType.POLISHED_DEEPSLATE, EnumStoneType.DEEPSLATE_BRICK);
        addSubStoneRecipes(EnumStoneType.POLISHED_DEEPSLATE, EnumStoneType.DEEPSLATE_TILE);
        addSubStoneRecipes(EnumStoneType.DEEPSLATE_BRICK, EnumStoneType.DEEPSLATE_TILE);
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(EnumStoneType.TUFF.getStack()), EnumStoneType.CHISELED_TUFF.getStack());
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(EnumStoneType.TUFF.getStack()), EnumStoneType.CHISELED_TUFF_BRICK.getStack());
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(EnumStoneType.POLISHED_TUFF.getStack()), EnumStoneType.CHISELED_TUFF_BRICK.getStack());
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(EnumStoneType.TUFF_BRICK.getStack()), EnumStoneType.CHISELED_TUFF_BRICK.getStack());
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(EnumStoneType.COBBLED_DEEPSLATE.getStack()), EnumStoneType.CHISELED_DEEPSLATE.getStack());
        for (EnumWeatherStage stage : EnumWeatherStage.values()) addCopperRecipes(stage);
    }
    
    private static void addStoneRecipes(EnumStoneType type) {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(type.getStack()), new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type)));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(type.getStack()), new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type.getShapedMeta()));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(type.getStack()), new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 2, type.getShapedMeta()));
    }
    
    private static void addSubStoneRecipes(EnumStoneType main, EnumStoneType type) {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(main.getStack()), type.getStack());
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(main.getStack()),
                new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type)));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(main.getStack()),
                new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type.getShapedMeta()));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(main.getStack()),
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 2, type.getShapedMeta()));
    }
    
    private static void addCopperRecipes(EnumWeatherStage stage) {
        //somehow cut blocks giving quad output is not a bug?
        //at least not on our end
        //mojank intended? (it's probably still a bug but fuck it you can't uncraft them)
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 4, stage.ordinal()));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 8, stage.ordinal()));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage), 4));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 4, stage.ordinal()));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 4, stage.ordinal()));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 2, stage.ordinal()));
        //except you, you were actually bugged
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage), 1));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal()));
    }
    
    private static void addCopperRecipe(ItemStack input, ItemStack output) {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(input), output);
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(((ICopperItem)input.getItem()).getWaxed(input)), ((ICopperItem)output.getItem()).getWaxed(output));
    }

    public static boolean isStonecutter(Container container) {
        return container instanceof StonecutterContainer;
    }

}
