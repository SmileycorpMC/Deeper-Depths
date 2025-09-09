package com.deeperdepths.integration;

import cofh.core.util.helpers.ItemHelper;
import cofh.thermalexpansion.util.managers.device.FactorizerManager;
import cofh.thermalexpansion.util.managers.machine.CompactorManager;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.items.DeeperDepthsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.ArmourSet;

public class ThermalExpansionIntegration {

    public static void registerRecipes() {
        int smelterEnergy = SmelterManager.DEFAULT_ENERGY * 3 / 2;
        ItemStack copperIngot = ItemHelper.getOre("ingotCopper");
        for (Item item : DeeperDepthsItems.COPPER_TOOLS.getItems()) SmelterManager.addRecycleRecipe(smelterEnergy,
                new ItemStack(item), copperIngot, 1);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HELMET)),
                copperIngot, 2);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.CHESTPLATE)),
                copperIngot, 4);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.LEGGINGS)),
                copperIngot, 3);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.BOOTS)),
                copperIngot, 2);
        SmelterManager.addRecycleRecipe(smelterEnergy, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getItem(ArmourSet.ArmorType.HORSE)),
                copperIngot, 2, false);
        PulverizerManager.addRecipe(75, ItemHelper.getOre("rodBreeze"), ItemHelper.getOre("dustBreeze", 6),
                ItemHelper.getOre("dustSaltpeter", 1), 50);
        FactorizerManager.addRecipe(new ItemStack(DeeperDepthsItems.MATERIALS, 1, 1), new ItemStack(DeeperDepthsBlocks.AMETHYST_BLOCK), true);
    }

}
