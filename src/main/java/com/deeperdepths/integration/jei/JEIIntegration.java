package com.deeperdepths.integration.jei;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.items.ICopperItem;
import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

@JEIPlugin
public class JEIIntegration implements IModPlugin {
    
    public static final String SCRAPING_ID = Constants.locStr("scraping");
    public static final String WAXING_ID = Constants.locStr("waxing");
    public static final String WEATHERING_ID = Constants.locStr("weathering");
    public static List<ItemStack> axes = Lists.newArrayList();
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new SimpleRecipeCategory(guiHelper, "waxing", WAXING_ID));
        registry.addRecipeCategories(new ScrapingRecipeCategory(guiHelper));
        registry.addRecipeCategories(new SimpleRecipeCategory(guiHelper, "weathering", WEATHERING_ID));
    }
    
    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(SimpleRecipeWrapper.class, r -> r, WAXING_ID);
        registry.handleRecipes(SimpleRecipeWrapper.class, r -> r, SCRAPING_ID);
        registry.handleRecipes(SimpleRecipeWrapper.class, r -> r, WEATHERING_ID);
        registry.addRecipeCatalyst(new ItemStack(Items.SLIME_BALL), WAXING_ID);
        List<SimpleRecipeWrapper> waxing_recipes = Lists.newArrayList();
        List<SimpleRecipeWrapper> scraping_recipes = Lists.newArrayList();
        List<SimpleRecipeWrapper> weathering_recipes = Lists.newArrayList();
        for (Item item : ForgeRegistries.ITEMS) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, stacks);
            for (ItemStack stack : stacks) {
                if (item.getToolClasses(stack).contains("axe")) axes.add(stack); //registry.addRecipeCatalyst(stack, SCRAPING_ID);
                if (!(item instanceof ICopperItem)) continue;
                ICopperItem copper = (ICopperItem) item;
                if (copper.canWax(stack)) waxing_recipes.add(new SimpleRecipeWrapper(stack, copper.getWaxed(stack)));
                if (copper.canScrape(stack)) scraping_recipes.add(new SimpleRecipeWrapper(stack, copper.getScraped(stack)));
                if (copper.canWeather(stack)) weathering_recipes.add(new SimpleRecipeWrapper(stack, copper.getWeathered(stack)));
            }
        }
        registry.addRecipes(waxing_recipes, WAXING_ID);
        registry.addRecipes(scraping_recipes, SCRAPING_ID);
        registry.addRecipes(weathering_recipes, WEATHERING_ID);
    }


}
