package com.deeperdepths.client.tesr;

import com.deeperdepths.common.blocks.tiles.TileVault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public class TESRVault extends TileEntitySpecialRenderer<TileVault> {
    
    @Override
    public void render(TileVault te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getDisplayedItem();
        if (stack.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        setLightmapDisabled(true);
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
        GlStateManager.rotate(9 * ((te.getWorld().getTotalWorldTime() % 40) + partialTicks), 0, 1, 0);
        RenderItem render = mc.getRenderItem();
        IBakedModel model = render.getItemModelWithOverrides(stack, te.getWorld(), mc.player);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
        render.renderItem(stack, model);
        GlStateManager.disableRescaleNormal();
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }
    
}
