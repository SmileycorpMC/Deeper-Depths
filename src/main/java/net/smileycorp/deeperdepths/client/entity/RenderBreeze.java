package net.smileycorp.deeperdepths.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.animation.model.BasicModelEntity;
import net.smileycorp.deeperdepths.client.entity.model.ModelBreeze;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderBreeze extends RenderLiving<EntityBreeze> {

    private static final ResourceLocation BREEZE = new ResourceLocation(Constants.MODID + ":textures/entities/breeze/breeze.png");

    public RenderBreeze(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, new ModelBreeze(), 0.8F);
        this.addLayer(new LayerBreezeWind(this, new ModelBreeze.ModelBreezeWind()));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBreeze entity) {
        return BREEZE;
    }


    @SideOnly(Side.CLIENT)
    private class LayerBreezeWind implements LayerRenderer<EntityBreeze> {
        private final ResourceLocation BREEZE_WIND_TEXTURE = new ResourceLocation(Constants.MODID + ":textures/entities/breeze/breeze_wind.png");
        private final RenderBreeze renderer;
        private BasicModelEntity layerModel;

        public LayerBreezeWind(RenderBreeze renderer, BasicModelEntity layerModelIn)
        {
            this.layerModel = layerModelIn;
            this.renderer = renderer;

        }

        public void doRenderLayer(EntityBreeze entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {

            boolean flag = entitylivingbaseIn.isInvisible();
            GlStateManager.depthMask(!flag);
            this.renderer.bindTexture(BREEZE_WIND_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.02F, 0.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float f1 = 0.5F;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.layerModel.setModelAttributes(this.renderer.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(!flag);
        }

        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
