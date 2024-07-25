package net.smileycorp.deeperdepths.client.entity.layers;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.entities.EntityBogged;

@SideOnly(Side.CLIENT)
public class LayerBoggedMoss implements LayerRenderer<EntityBogged>
{
    private static final ResourceLocation BOGGED_MOSS_TEXTURES = new ResourceLocation(Constants.MODID + ":textures/entities/bogged/bogged_overlay.png");
    private final RenderLivingBase<?> renderer;
    private final ModelSkeleton layerModel = new ModelSkeleton(0.1F, true);

    public LayerBoggedMoss(RenderLivingBase<?> rendererIn)
    { this.renderer = rendererIn; }

    public void doRenderLayer(EntityBogged entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.layerModel.setModelAttributes(this.renderer.getMainModel());
        this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderer.bindTexture(BOGGED_MOSS_TEXTURES);
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}