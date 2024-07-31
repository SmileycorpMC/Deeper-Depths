package net.smileycorp.deeperdepths.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialSpawner;

public class TESRTrialSpawner extends TileEntitySpecialRenderer<TileTrialSpawner> {
    
    @Override
    public void render(TileTrialSpawner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!te.getState().isActive()) return;
        Entity entity = te.getCachedEntity();
        if (entity == null) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        float scale = 0.53125F;
        float size = Math.max(entity.width, entity.height);
        if ((double)size > 1.0) scale /= size;
        GlStateManager.translate(0.0F, 0.4F, 0.0F);
        GlStateManager.rotate((te.getState() == EnumTrialSpawnerState.ACTIVE ? 45 : 9) * ((te.getWorld().getTotalWorldTime() % 40) + partialTicks), 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -0.2F, 0.0F);
        GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(scale, scale, scale);
        entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0, 0.0, 0.0, 0.0F, partialTicks, false);
        GlStateManager.popMatrix();
    }
    
}
