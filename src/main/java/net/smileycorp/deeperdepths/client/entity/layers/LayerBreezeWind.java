package net.smileycorp.deeperdepths.client.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.client.entity.model.ModelBreezeWind;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;

@SideOnly(Side.CLIENT)
public class LayerBreezeWind implements LayerRenderer<EntityBreeze>
{
    private static final ResourceLocation BREEZE_WIND_TEXTURE = new ResourceLocation(Constants.MODID + ":textures/entities/breeze/breeze_wind.png");
    private final RenderLivingBase<?> renderer;
    private final ModelBreezeWind layerModel = new ModelBreezeWind();

    public LayerBreezeWind(RenderLivingBase<?> rendererIn, ModelBreezeWind layerModelIn)
    {
        this.renderer = rendererIn;
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
        //GlStateManager.disableLighting();
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.layerModel.setModelAttributes(this.renderer.getMainModel());
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(flag);


    }

    public boolean shouldCombineTextures() {
        return true;
    }
}