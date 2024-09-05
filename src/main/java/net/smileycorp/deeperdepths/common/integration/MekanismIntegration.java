package net.smileycorp.deeperdepths.common.integration;

import mekanism.api.MekanismAPI;
import mekanism.api.MekanismRecipeHelper;
import mekanism.common.MekanismFluids;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;

import java.util.Map;

public class MekanismIntegration {

    public static void registerRecipes() {
        MekanismRecipeHelper helper = MekanismAPI.recipeHelper();
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
                    new ItemStack(DeeperDepthsBlocks.COPPER_DOORS.get(stage).getItem()));
        }
    }

    private static void addUnwaxingRecipe(MekanismRecipeHelper helper, EnumWeatherStage stage, Block block) {
        helper.addCrusherRecipe(new ItemStack(block, 1, stage.ordinal() + 4), new ItemStack(block, 1, stage.ordinal()));
    }

    private static void addCopperRecipes(MekanismRecipeHelper helper, EnumWeatherStage stage, Block block) {
        addCopperRecipes(helper, new ItemStack(block, 1, stage.ordinal()),
                new ItemStack(block, 1, stage.ordinal()));
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
