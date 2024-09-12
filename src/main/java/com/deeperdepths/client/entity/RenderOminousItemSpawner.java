package com.deeperdepths.client.entity;

import com.deeperdepths.common.entities.EntityOminousItemSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import javax.annotation.Nullable;

public class RenderOminousItemSpawner extends Render<EntityOminousItemSpawner> {
    
    public RenderOminousItemSpawner(RenderManager rm) {
        super(rm);
    }
    
    @Override
    public void doRender(EntityOminousItemSpawner entity, double x, double y, double z, float yaw, float partialTicks) {
        super.doRender(entity, x, y, z, yaw, partialTicks);
        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.translate(x + 0.5, y + 0.4, z + 0.5);
        GlStateManager.rotate(9 * ((entity.world.getTotalWorldTime() % 40) + partialTicks), 0, 1, 0);
        RenderItem render = mc.getRenderItem();
        IBakedModel model = render.getItemModelWithOverrides(stack, entity.world, mc.player);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
        render.renderItem(stack, model);
        GlStateManager.disableRescaleNormal();
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityOminousItemSpawner entityOminousItemSpawner) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
    
}
