package com.deeperdepths.integration.jei;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ScrapingRecipeCategory implements IRecipeCategory<SimpleRecipeWrapper> {
    
    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/simple_catalyst_recipe.png");
    
    private final IDrawable background, icon;
    
    public ScrapingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 66, 41);
        icon = guiHelper.createDrawableIngredient(new ItemStack(DeeperDepthsBlocks.COPPER_BLOCK));
    }
    
    @Override
    public IDrawable getBackground() {
        return background;
    }
    
    @Override
    public IDrawable getIcon() {
        return icon;
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SimpleRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.init(0, false, 47, 19);
        items.init(1, true, 1, 19);
        items.init(2, true, 24, 1);
        items.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        items.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        items.set(2, JEIIntegration.axes);
    }
    
    @Override
    public String getUid() {
        return JEIIntegration.SCRAPING_ID;
    }
    
    @Override
    public String getTitle() {
        return I18n.format("jei.category.deeperdepths.scraping");
    }
    
    @Override
    public String getModName() {
        return Constants.MODID;
    }
    
}
