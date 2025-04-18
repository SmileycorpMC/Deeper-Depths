package com.deeperdepths.integration;

import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.items.ICopperItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;

public class FutureMCIntegration {

    public static void registerRecipes() {
        for (EnumStoneType type : EnumStoneType.SHAPED_TYPES) addStoneRecipes(type);
        addSubStoneRecipes(EnumStoneType.TUFF, EnumStoneType.POLISHED_TUFF);
        addSubStoneRecipes(EnumStoneType.TUFF, EnumStoneType.TUFF_BRICK);
        addSubStoneRecipes(EnumStoneType.POLISHED_TUFF, EnumStoneType.TUFF_BRICK);
        addSubStoneRecipes(EnumStoneType.COBBLED_DEEPSLATE, EnumStoneType.POLISHED_DEEPSLATE);
        addSubStoneRecipes(EnumStoneType.COBBLED_DEEPSLATE, EnumStoneType.DEEPSLATE_BRICK);
        addSubStoneRecipes(EnumStoneType.POLISHED_DEEPSLATE, EnumStoneType.DEEPSLATE_BRICK);
        addSubStoneRecipes(EnumStoneType.POLISHED_DEEPSLATE, EnumStoneType.DEEPSLATE_TILE);
        addSubStoneRecipes(EnumStoneType.DEEPSLATE_BRICK, EnumStoneType.DEEPSLATE_TILE);
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE)),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 2));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE)),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 4));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, 1)),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 4));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, 3)),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 4));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, 6)),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 7));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, 6)),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, 7));
        for (EnumWeatherStage stage : EnumWeatherStage.values()) addCopperRecipes(stage);
    }
    
    private static void addStoneRecipes(EnumStoneType type) {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, type.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type)));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, type.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type.getShapedMeta()));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, type.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 2, type.getShapedMeta()));
    }
    
    private static void addSubStoneRecipes(EnumStoneType type, EnumStoneType main) {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, main.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE, 1, type.ordinal()));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, main.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type)));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, main.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type.getShapedMeta()));
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(new ItemStack(DeeperDepthsBlocks.STONE, 1, main.ordinal())),
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 2, type.getShapedMeta()));
    }
    
    private static void addCopperRecipes(EnumWeatherStage stage) {
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
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage), 4));
        addCopperRecipe(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()),
                new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal()));
    }
    
    private static void addCopperRecipe(ItemStack input, ItemStack output) {
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(input), output);
        StonecutterRecipes.INSTANCE.addRecipe(Ingredient.fromStacks(((ICopperItem)input.getItem()).getWaxed(input)), ((ICopperItem)output.getItem()).getWaxed(output));
    }

}
