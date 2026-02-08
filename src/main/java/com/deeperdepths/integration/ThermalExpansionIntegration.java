package com.deeperdepths.integration;

import cofh.core.util.helpers.ItemHelper;
import cofh.thermalexpansion.util.managers.device.FactorizerManager;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.items.DeeperDepthsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ThermalExpansionIntegration {

    public static void registerRecipes() {
        int smelterEnergy = SmelterManager.DEFAULT_ENERGY * 3 / 2;
        ItemStack copperIngot =  ItemHelper.getOre("ingotCopper", 1);
        for (Item item : DeeperDepthsItems.COPPER_TOOLS.getItems()) SmelterManager.addRecycleRecipe(smelterEnergy,
                new ItemStack(item), copperIngot, 1);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getHelmet()),
                copperIngot, 2);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getChestplate()),
                copperIngot, 4);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getLeggings()),
                copperIngot, 3);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getBoots()),
                copperIngot, 2);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getHorseArmour()),
                copperIngot, 2, false);
        PulverizerManager.addRecipe(75, new ItemStack(DeeperDepthsItems.MATERIALS, 1, 3), new ItemStack(DeeperDepthsItems.WIND_CHARGE, 6),
                ItemHelper.getOre("dustSaltpeter", 1), 50);
        FactorizerManager.addRecipe(new ItemStack(DeeperDepthsItems.MATERIALS, 1, 4), new ItemStack(DeeperDepthsBlocks.AMETHYST_BLOCK), true);
    }

}
