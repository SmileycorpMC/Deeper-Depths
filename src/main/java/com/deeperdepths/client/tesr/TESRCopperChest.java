package com.deeperdepths.client.tesr;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import com.deeperdepths.common.items.ItemBlockCopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TESRCopperChest extends TileEntitySpecialRenderer<TileCopperChest> {

    private final ModelChest simpleChest = new ModelChest();
    private final ModelChest largeChest = new ModelLargeChest();

    @Override
    public void render(TileCopperChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;
        if (te.hasWorld()) {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        }
        else i = 0;
        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;
                if (destroyStage >= 0) {
                    bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4, 4, 1);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                bindTexture(getTexture(te, false));
            }
            else {
                modelchest = largeChest;
                if (destroyStage >= 0) {
                    bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8, 4, 1);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                bindTexture(getTexture(te, true));
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            if (destroyStage < 0) GlStateManager.color(1, 1, 1, alpha);
            GlStateManager.translate((float)x, (float)y + 1, (float)z + 1);
            GlStateManager.scale(1, -1, -1);
            GlStateManager.translate(0.5f, 0.5f, 0.5f);
            int j = 0;
            if (i == 2) j = 180;
            if (i == 3) j = 0;
            if (i == 4) j = 90;
            if (i == 5) j = -90;
            if (i == 2 && te.adjacentChestXPos != null) GlStateManager.translate(1, 0, 0);
            if (i == 5 && te.adjacentChestZPos != null) GlStateManager.translate(0, 0, -1);
            GlStateManager.rotate(j, 0, 1, 0);
            GlStateManager.translate(-0.5f, -0.5f, -0.5f);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            if (te.adjacentChestZNeg != null) {
                float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
                if (f1 > f) f = f1;
            }
            if (te.adjacentChestXNeg != null) {
                float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
                if (f2 > f) f = f2;
            }
            f = 1 - f;
            f = 1 - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2f));
            modelchest.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }

    private ResourceLocation getTexture(TileCopperChest te, boolean large) {
        String name = te.getWeatherStage().getName();
        if (te.isWaxed()) name = "waxed/" + name;
        if (large) name += "_double";
        return Constants.loc("textures/entity/copper_chest/" + name + ".png");
    }

    public static class Item extends TileEntityItemStackRenderer {

        private final TileCopperChest tile = new TileCopperChest();

        public void renderByItem(ItemStack stack, float partialTicks) {
            tile.setupRenderProperties(EnumWeatherStage.values()[stack.getMetadata() % 4],
                    stack.getItem() == ItemBlockCopper.getItemFromBlock(DeeperDepthsBlocks.WAXED_COPPER_CHEST));
            TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, 0, partialTicks);
        }

    }
}
