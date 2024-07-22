package net.smileycorp.deeperdepths.client.tesr;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.client.model.animation.FastTESR;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileVault;

public class TESRVault extends FastTESR<TileVault> {
    
    @Override
    public void renderTileEntityFast(TileVault te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        /*ItemStack stack = te.getDisplayedItem();
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
        GlStateManager.translate(x + 0.5, y + 0.3,  z + 0.5);
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.scale(0.6f, 0.6f, 0.001f);
        GlStateManager.rotate(180 * ((te.getWorld().getTotalWorldTime() % 10) + partialTicks), 0, 1, 0);
        RenderItem render = mc.getRenderItem();
        IBakedModel model = render.getItemModelWithOverrides(stack, te.getWorld(), mc.player);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
        render.renderItem(stack, model);
        GlStateManager.disableRescaleNormal();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();*/
    }
    
}
