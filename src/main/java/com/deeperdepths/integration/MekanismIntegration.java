package com.deeperdepths.integration;

import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.items.DeeperDepthsItems;
import mekanism.api.MekanismAPI;
import mekanism.api.MekanismRecipeHelper;
import mekanism.common.MekanismFluids;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class MekanismIntegration {

    public static void registerRecipes() {
        MekanismRecipeHelper helper = MekanismAPI.recipeHelper();
        //tuff
        addReversibleRecipe(helper, EnumStoneType.TUFF.getStack(), EnumStoneType.CHISELED_TUFF.getStack());
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(EnumStoneType.TUFF)),
                new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(EnumStoneType.TUFF_BRICK)));
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, EnumStoneType.TUFF.getShapedMeta()),
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, EnumStoneType.TUFF_BRICK.getShapedMeta()));
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, EnumStoneType.TUFF.getShapedMeta()),
                new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, EnumStoneType.TUFF_BRICK.getShapedMeta()));
        addStoneRecipe(helper, EnumStoneType.POLISHED_TUFF, EnumStoneType.TUFF);
        addStoneRecipe(helper, EnumStoneType.TUFF_BRICK, EnumStoneType.POLISHED_TUFF);
        addReversibleRecipe(helper, EnumStoneType.CHISELED_TUFF.getStack(), EnumStoneType.TUFF_BRICK.getStack());
        //deepslate
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.DEEPSLATE), EnumStoneType.COBBLED_DEEPSLATE.getStack());
        helper.addEnrichmentChamberRecipe(new ItemStack(DeeperDepthsBlocks.DEEPSLATE), EnumStoneType.POLISHED_DEEPSLATE.getStack());
        helper.addCrusherRecipe(EnumStoneType.CHISELED_DEEPSLATE.getStack(), new ItemStack(DeeperDepthsBlocks.DEEPSLATE));
        helper.addEnrichmentChamberRecipe(EnumStoneType.POLISHED_DEEPSLATE.getStack(), EnumStoneType.CHISELED_DEEPSLATE.getStack());
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(EnumStoneType.POLISHED_DEEPSLATE)),
                new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(EnumStoneType.DEEPSLATE_BRICK)));
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, EnumStoneType.POLISHED_DEEPSLATE.getShapedMeta()),
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, EnumStoneType.DEEPSLATE_BRICK.getShapedMeta()));
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, EnumStoneType.POLISHED_DEEPSLATE.getShapedMeta()),
                new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, EnumStoneType.DEEPSLATE_BRICK.getShapedMeta()));
        helper.addCrusherRecipe(EnumStoneType.POLISHED_DEEPSLATE.getStack(), EnumStoneType.DEEPSLATE_BRICK.getStack());
        addStoneRecipe(helper, EnumStoneType.DEEPSLATE_BRICK, EnumStoneType.CRACKED_DEEPSLATE_BRICK);
        addStoneRecipe(helper, EnumStoneType.CRACKED_DEEPSLATE_BRICK, EnumStoneType.DEEPSLATE_TILE);
        addStoneRecipe(helper, EnumStoneType.DEEPSLATE_TILE, EnumStoneType.CRACKED_DEEPSLATE_TILE);
        addReversibleRecipe(helper, EnumStoneType.CRACKED_DEEPSLATE_TILE.getStack(), EnumStoneType.CHISELED_DEEPSLATE.getStack());
        //copper
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            //unwaxing
            addUnwaxingRecipe(helper, stage, DeeperDepthsBlocks.COPPER_BLOCK);
            addUnwaxingRecipe(helper, stage, DeeperDepthsBlocks.CUT_COPPER);
            addUnwaxingRecipe(helper, stage, DeeperDepthsBlocks.CUT_COPPER_SLAB);
            addUnwaxingRecipe(helper, stage, DeeperDepthsBlocks.CHISELED_COPPER);
            addUnwaxingRecipe(helper, stage, DeeperDepthsBlocks.COPPER_GRATE);
            helper.addCrusherRecipe(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_BULB, 1, stage.ordinal()),
                    new ItemStack(DeeperDepthsBlocks.COPPER_BULB, 1, stage.ordinal()));
            helper.addCrusherRecipe(new ItemStack(DeeperDepthsBlocks.WAXED_CUT_COPPER_STAIRS.get(stage)),
                    new ItemStack(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage)));
            helper.addCrusherRecipe(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_TRAPDOORS.get(stage)),
                    new ItemStack(DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage)));
            helper.addCrusherRecipe(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_DOORS.get(stage).getItem()),
                    new ItemStack(DeeperDepthsBlocks.COPPER_DOORS.get(stage).getItem()));
            if (stage == EnumWeatherStage.OXIDIZED) break;
            //weathering
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.COPPER_BLOCK);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.CUT_COPPER);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.CUT_COPPER_SLAB);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.CHISELED_COPPER);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.COPPER_GRATE);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.COPPER_BULB);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.CUT_COPPER_STAIRS);
            addCopperRecipes(helper, stage, DeeperDepthsBlocks.COPPER_TRAPDOORS);
            addCopperRecipes(helper, new ItemStack(DeeperDepthsBlocks.COPPER_DOORS.get(stage).getItem()),
                    new ItemStack(DeeperDepthsBlocks.COPPER_DOORS.get(stage.next()).getItem()));
        }
        //breeze rods
        helper.addCrusherRecipe(new ItemStack(DeeperDepthsItems.MATERIALS, 1, 3),
                new ItemStack(DeeperDepthsItems.WIND_CHARGE, 6));
    }

    private static void addStoneRecipe(MekanismRecipeHelper helper, EnumStoneType type1, EnumStoneType type2) {
        addReversibleRecipe(helper, type1.getStack(), type2.getStack());
        if (!type1.hasVariants() |!type2.hasVariants()) return;
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type1)),
                new ItemStack(DeeperDepthsBlocks.STONE_STAIRS.get(type2)));
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, type1.getShapedMeta()),
                new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, type2.getShapedMeta()));
        addReversibleRecipe(helper, new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type1.getShapedMeta()),
                new ItemStack(DeeperDepthsBlocks.STONE_WALL, 1, type2.getShapedMeta()));
    }

    private static void addReversibleRecipe(MekanismRecipeHelper helper, ItemStack stack1, ItemStack stack2) {
        helper.addCrusherRecipe(stack1, stack2);
        helper.addEnrichmentChamberRecipe(stack2, stack1);
    }

    private static void addUnwaxingRecipe(MekanismRecipeHelper helper, EnumWeatherStage stage, Block block) {
        helper.addCrusherRecipe(new ItemStack(block, 1, stage.ordinal() + 4), new ItemStack(block, 1, stage.ordinal()));
    }

    private static void addCopperRecipes(MekanismRecipeHelper helper, EnumWeatherStage stage, Block block) {
        addCopperRecipes(helper, new ItemStack(block, 1, stage.ordinal()),
                new ItemStack(block, 1, stage.ordinal() + 1));
    }

    private static void addCopperRecipes(MekanismRecipeHelper helper, EnumWeatherStage stage, Map<EnumWeatherStage, ? extends Block> map) {
        addCopperRecipes(helper, new ItemStack(map.get(stage)),
                new ItemStack(map.get(stage.next())));
    }

    private static void addCopperRecipes(MekanismRecipeHelper helper, ItemStack stack1, ItemStack stack2) {
        helper.addChemicalInjectionChamberRecipe(stack1, MekanismFluids.Oxygen, stack2);
        helper.addCrusherRecipe(stack2, stack1);
    }

}
