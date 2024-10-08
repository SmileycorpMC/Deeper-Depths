package com.deeperdepths.client.entity.layers;

import com.deeperdepths.client.entity.model.ModelBoggedMushrooms;
import com.deeperdepths.common.Constants;
import com.deeperdepths.common.entities.EntityBogged;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBoggedMushrooms implements LayerRenderer<EntityBogged>
{
    private static final ResourceLocation BOGGED_MUSHROOMS_TEXTURES = new ResourceLocation(Constants.MODID + ":textures/entities/bogged/bogged_overlay.png");
    private final RenderLivingBase<?> renderer;
    private final ModelBoggedMushrooms boggedMushroomsModel;

    public LayerBoggedMushrooms(RenderLivingBase<?> rendererIn, ModelBoggedMushrooms boggedMushroomsModelIn)
    {
        this.renderer = rendererIn;
        this.boggedMushroomsModel = boggedMushroomsModelIn;
    }

    public void doRenderLayer(EntityBogged entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entityIn.isInvisible() && !entityIn.getSheared())
        {
            this.boggedMushroomsModel.setModelAttributes(this.renderer.getMainModel());
            this.boggedMushroomsModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
            //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            //this.renderer.bindTexture(BOGGED_MUSHROOMS_TEXTURES);
            this.boggedMushroomsModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures()
    { return true; }
}
