package com.deeperdepths.common;

import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.common.items.DeeperDepthsItems;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.deeperdepths.integration.*;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsRecipes {
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        registerOreDictionary();
        registerFurnaceRecipes();
        registerBrewingRecipes();
        registerTrades();
        if (Loader.isModLoaded("chisel")) ChiselIntegration.registerRecipes();
        if (Loader.isModLoaded("futuremc")) FutureMCIntegration.registerRecipes();
        if (Loader.isModLoaded("mekanism")) MekanismIntegration.registerRecipes();
        if (Loader.isModLoaded("tconstruct")) TinkersConstructIntegration.registerRecipes();
        if (Loader.isModLoaded("thermalexpansion")) ThermalExpansionIntegration.registerRecipes();
    }

    
    public static void registerLateRecipes() {
        for (ItemStack stack : OreDictionary.getOres("tallow")) OreDictionary.registerOre("wax", stack);
        if (OreDictionary.getOres("wax").isEmpty()) OreDictionary.registerOre("wax", Items.SLIME_BALL);
    }
    
    private static void registerOreDictionary() {
        OreDictionary.registerOre("rodBlaze", Items.BLAZE_ROD);
        OreDictionary.registerOre("ingotCopper", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 0));
        OreDictionary.registerOre("gemAmethyst", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 1));
        OreDictionary.registerOre("gemEcho", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 2));
        OreDictionary.registerOre("rodBreeze", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 3));
        OreDictionary.registerOre("nuggetCopper", new ItemStack(DeeperDepthsItems.MATERIALS, 1, 4));
        OreDictionary.registerOre("stone", DeeperDepthsBlocks.DEEPSLATE);
        OreDictionary.registerOre("stoneDeepslate", DeeperDepthsBlocks.DEEPSLATE);
        OreDictionary.registerOre("stone", EnumStoneType.TUFF.getStack());
        OreDictionary.registerOre("stoneTuff", EnumStoneType.TUFF.getStack());
        OreDictionary.registerOre("stoneCalcite", EnumStoneType.CALCITE.getStack());
        OreDictionary.registerOre("cobblestone", EnumStoneType.COBBLED_DEEPSLATE.getStack());
        OreDictionary.registerOre("cobblestoneDeepslate", EnumStoneType.COBBLED_DEEPSLATE.getStack());
        OreDictionary.registerOre("oreCopper", new ItemStack(DeeperDepthsBlocks.COPPER_ORE));
        for (int i = 0; i < DeeperDepthsBlocks.COPPER_BLOCK.getMaxMeta(); i++)
            OreDictionary.registerOre("blockCopper", new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK, 1, i));
        for (int i = 0; i < DeeperDepthsBlocks.CUT_COPPER.getMaxMeta(); i++)
            OreDictionary.registerOre("blockCopperCut", new ItemStack(DeeperDepthsBlocks.CUT_COPPER, 1, i));
        for (int i = 0; i < DeeperDepthsBlocks.CHISELED_COPPER.getMaxMeta(); i++)
            OreDictionary.registerOre("blockCopperChiseled", new ItemStack(DeeperDepthsBlocks.CHISELED_COPPER, 1, i));
        OreDictionary.registerOre("blockGlass", new ItemStack(DeeperDepthsBlocks.TINTED_GLASS));
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
        DeeperDepthsItems.COPPER_TOOLS.getItems().forEach(item ->
                GameRegistry.addSmelting(item, new ItemStack(DeeperDepthsItems.MATERIALS, 1, 4), 0.1F));
        DeeperDepthsItems.COPPER_ARMOR.getItems().forEach(item ->
                GameRegistry.addSmelting(item, new ItemStack(DeeperDepthsItems.MATERIALS, 1, 4), 0.1F));
    }

    private static void registerBrewingRecipes() {
        PotionHelper.addMix(PotionTypes.AWKWARD, Item.getItemFromBlock(Blocks.STONE), DeeperDepthsPotions.INFESTED_POTION);
        PotionHelper.addMix(PotionTypes.AWKWARD, Item.getItemFromBlock(Blocks.SLIME_BLOCK), DeeperDepthsPotions.OOZING_POTION);
        PotionHelper.addMix(PotionTypes.AWKWARD, Item.getItemFromBlock(Blocks.WEB), DeeperDepthsPotions.WEAVING_POTION);
        PotionHelper.addMix(PotionTypes.AWKWARD, Ingredient.fromStacks(new ItemStack(DeeperDepthsItems.MATERIALS, 1, 3)), DeeperDepthsPotions.WIND_CHARGED_POTION);
    }

    public static void registerTrades() {
        ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:librarian")).getCareer(1)
                .addTrade(2, explorerMap(0, new EntityVillager.PriceInfo(12, 12)));
    }

    public static ItemForItemAndEmeralds explorerMap(int id, EntityVillager.PriceInfo price) {
        return new ItemForItemAndEmeralds(price, new ItemStack(Items.COMPASS), new EntityVillager.PriceInfo(1, 1),
                new ItemStack(DeeperDepthsItems.EXPLORER_MAP, 3, id), new EntityVillager.PriceInfo(1, 1));
    }

    public static class ItemForItemAndEmeralds implements EntityVillager.ITradeList {

        private final ItemStack buy, sell;
        private final EntityVillager.PriceInfo emeraldPrice, buyPrice, sellPrice;

        public ItemForItemAndEmeralds( EntityVillager.PriceInfo emeraldPrice, ItemStack buy, EntityVillager.PriceInfo buyPrice, ItemStack sell, EntityVillager.PriceInfo sellPrice) {
            this.emeraldPrice = emeraldPrice;
            this.buy = buy;
            this.buyPrice = buyPrice;
            this.sell = sell;
            this.sellPrice = sellPrice;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random rand) {
            ItemStack buy = this.buy.copy();
            buy.setCount(buyPrice.getPrice(rand));
            ItemStack sell = this.sell.copy();
            sell.setCount(sellPrice.getPrice(rand));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldPrice.getPrice(rand)), buy, sell));
        }

    }
    
}