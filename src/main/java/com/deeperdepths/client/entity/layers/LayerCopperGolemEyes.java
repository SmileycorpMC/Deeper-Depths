package com.deeperdepths.client.entity.layers;

import com.deeperdepths.client.entity.RenderCopperGolem;
import com.deeperdepths.common.Constants;
import com.deeperdepths.common.entities.EntityCopperGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

/* TODO: Replace model w/ a scaled version of the base model. */
public class LayerCopperGolemEyes implements LayerRenderer<EntityCopperGolem>
{
    private final RenderCopperGolem copperGolemRenderer;

    public LayerCopperGolemEyes(RenderCopperGolem copperGolemRendererIn)
    { this.copperGolemRenderer = copperGolemRendererIn; }

    public void doRenderLayer(EntityCopperGolem entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ResourceLocation EYES = new ResourceLocation(Constants.MODID + ":textures/entities/copper_golem/eyes/normal.png");
        this.copperGolemRenderer.bindTexture(EYES);

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        /* Fuck it, just SLIGHTLY offset the eyes forward.*/
        GlStateManager.translate(0,0,-0.001);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        this.copperGolemRenderer.getMainModel().setModelAttributes(copperGolemRenderer.getMainModel());
        this.copperGolemRenderer.getMainModel().setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        this.copperGolemRenderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        this.copperGolemRenderer.setLightmap(entity);
        GlStateManager.popMatrix();

    }

    public boolean shouldCombineTextures() { return true; }
}