package com.deeperdepths.integration.jei;

import com.deeperdepths.common.Constants;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class SimpleRecipeCategory implements IRecipeCategory<SimpleRecipeWrapper> {

	public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/simple_recipe.png");
	
	private final IDrawable background;
	private final String name, id;
	
	public SimpleRecipeCategory(IGuiHelper guiHelper, String name, String id) {
		background = guiHelper.createDrawable(TEXTURE, 0, 0, 67, 28);
		this.name = name;
		this.id = id;
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SimpleRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup items = recipeLayout.getItemStacks();
		items.init(0, false, 48, 5);
		items.init(1, true, 1, 5);
		items.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		items.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(0));
	}
	
	@Override
	public String getUid() {
		return id;
	}
	
	@Override
	public String getTitle() {
		return I18n.format("jei.category.deeperdepths." + name);
	}
	
	@Override
	public String getModName() {
		return Constants.MODID;
	}

}
