package com.deeperdepths.integration;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.blocks.BlockCopperDoor;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.items.DeeperDepthsItems;
import crazypants.enderio.base.recipe.Recipe;
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.ArmourSet;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.shared.TinkerFluids;

public class TinkersConstructIntegration {

    //tinkers autoregisters copper but doesn't detect every block, and does a terrible job at figuring out how much copper to give back
    //it's auto detection makes a terrible duping issue with doors and trapdoors
    //plus if futuremc is installed vanilla's jank ass copper to cut copper 4x stonecutting recipe makes that a whole lot worse
    public static void registerRecipes() {
        TinkerRegistry.registerMelting(DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE), TinkerFluids.copper, Material.VALUE_Ingot * 4);
        for (EnumWeatherStage stage : EnumWeatherStage.values()) {
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, stage.ordinal() + 4),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Block / 8);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, stage.ordinal() + 4),
                    TinkerFluids.copper, Material.VALUE_Block / 8);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, stage.ordinal() + 4),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 1, stage.ordinal() + 4),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_BULB, 1, stage.ordinal()),
                    TinkerFluids.copper, (int)(Material.VALUE_Block * 1.5f));
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_BULB, 1, stage.ordinal()),
                    TinkerFluids.copper, (int)(Material.VALUE_Block * 1.5f));
            TinkerRegistry.registerMelting(DeeperDepthsBlocks.CUT_COPPER_STAIRS.get(stage),
                    TinkerFluids.copper, Material.VALUE_Block / 3);
            TinkerRegistry.registerMelting(DeeperDepthsBlocks.WAXED_CUT_COPPER_STAIRS.get(stage),
                    TinkerFluids.copper, Material.VALUE_Block / 3);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_GRATE, 1, stage.ordinal() + 4),
                    TinkerFluids.copper, Material.VALUE_Block / 4);
            TinkerRegistry.registerMelting(DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage),
                    TinkerFluids.copper, Material.VALUE_Ingot / 4);
            TinkerRegistry.registerMelting(DeeperDepthsBlocks.WAXED_COPPER_TRAPDOORS.get(stage),
                    TinkerFluids.copper, Material.VALUE_Ingot / 4);
            TinkerRegistry.registerMelting(DeeperDepthsBlocks.WAXED_COPPER_DOORS.get(stageu).getItem(),
                    TinkerFluids.copper, Material.VALUE_Ingot * 2);
            if (stage != EnumWeatherStage.NORMAL) TinkerRegistry.registerMelting(DeeperDepthsBlocks.COPPER_BARS.get(stage),
                    TinkerFluids.copper, (int)(Material.VALUE_Ingot * 0.375f));
            TinkerRegistry.registerMelting(DeeperDepthsBlocks.WAXED_COPPER_BARS.get(stage),
                    TinkerFluids.copper, (int)(Material.VALUE_Ingot * 0.375f));
            if (stage != EnumWeatherStage.NORMAL) TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_CHEST, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Ingot * 8);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_CHEST, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Ingot * 8);
            if (stage != EnumWeatherStage.NORMAL) TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_CHAINS, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Ingot + Material.VALUE_Nugget * 2);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_CHAINS, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Ingot + Material.VALUE_Nugget * 2);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_LANTERN, 1, stage.ordinal()),
                    TinkerFluids.copper, Material.VALUE_Nugget * 8);
            TinkerRegistry.registerMelting(new ItemStack(DeeperDepthsBlocks.COPPER_LANTERN, 1, stage.ordinal() + 4),
                    TinkerFluids.copper, Material.VALUE_Nugget * 8);
        }
    }

}
