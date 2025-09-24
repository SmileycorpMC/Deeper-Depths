package com.deeperdepths.client.tesr;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.BlockCopperChest;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TESRCopperChest extends TileEntitySpecialRenderer<TileCopperChest> {

    private final ModelChest simpleChest = new ModelChest();
    private final ModelChest largeChest = new ModelLargeChest();

    @Override
    public void render(TileCopperChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing direction = te.getOtherDirection();
        if ((direction == EnumFacing.NORTH || direction == EnumFacing.WEST) && destroyStage < 0) return;
        World world = getWorld();
        if (te.isInvalid() || world == null || te.getPos() == null) return;
        IBlockState state = world.getBlockState(te.getPos());
        if (!(state.getBlock() instanceof BlockCopperChest)) return;
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        ModelChest model;
        float lidAngle = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        if (direction == null) {
            model = simpleChest;
            if (destroyStage >= 0) {
                bindTexture(DESTROY_STAGES[destroyStage]);
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4, 4, 1);
                GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                GlStateManager.matrixMode(5888);
            }
        }
        else {
            model = largeChest;
            if (destroyStage >= 0) {
                bindTexture(DESTROY_STAGES[destroyStage]);
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scale(8, 4, 1);
                GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                GlStateManager.matrixMode(5888);
            }
            TileCopperChest other = (TileCopperChest) world.getTileEntity(te.getPos().offset(direction));
            float otherLidAngle = other.prevLidAngle + (other.lidAngle - other.prevLidAngle) * partialTicks;
            if (otherLidAngle > lidAngle) lidAngle = otherLidAngle;
        }
        if (destroyStage < 0) bindTexture(getTexture(te));
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        if (destroyStage < 0) GlStateManager.color(1, 1, 1, alpha);
        GlStateManager.translate((float)x, (float)y + 1, (float)z + 1);
        GlStateManager.scale(1, -1, -1);
        GlStateManager.translate(0.5f, 0.5f, 0.5f);
        int rotation = 0;
        EnumFacing facing = state.getValue(BlockHorizontal.FACING);
        switch (facing) {
            case NORTH:
                rotation = 180;
                break;
            case WEST:
                rotation = 90;
                break;
            case EAST:
                rotation = -90;
                break;
        }
        if (direction != null) {
            if (facing == EnumFacing.EAST && direction == EnumFacing.SOUTH) GlStateManager.translate(0, 0, -1);
            else if (facing == EnumFacing.WEST && direction == EnumFacing.NORTH) GlStateManager.translate(0, 0, 1);
            else if (facing == EnumFacing.NORTH && direction == EnumFacing.EAST) GlStateManager.translate(1, 0, 0);
            else if (facing == EnumFacing.SOUTH && direction == EnumFacing.WEST) GlStateManager.translate(-1, 0, 0);
        }
        GlStateManager.rotate(rotation, 0, 1, 0);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        lidAngle = 1 - lidAngle;
        lidAngle = 1 - lidAngle * lidAngle * lidAngle;
        model.chestLid.rotateAngleX = -(lidAngle * ((float)Math.PI / 2f));
        model.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private ResourceLocation getTexture(TileCopperChest te) {
        EnumWeatherStage stage = te.getWeatherStage();
        String name = stage == null ? "normal" : te.getWeatherStage().getName();
        if (te.isWaxed()) name = "waxed/" + name;
        if (te.isLarge()) name += "_double";
        return Constants.loc("textures/entities/copper_chest/" + name + ".png");
    }

    public static class ItemRenderer extends TileEntityItemStackRenderer {

        private final TileCopperChest tile = new TileCopperChest();

        public void renderByItem(ItemStack stack, float partialTicks) {
            tile.setupRenderProperties(EnumWeatherStage.values()[stack.getMetadata() % 4],
                    stack.getItem() == Item.getItemFromBlock(DeeperDepthsBlocks.WAXED_COPPER_CHEST));
            TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, 0, partialTicks);
        }

    }
}
