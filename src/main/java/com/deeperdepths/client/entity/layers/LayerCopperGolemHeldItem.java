package com.deeperdepths.client.entity.layers;

import com.deeperdepths.client.entity.RenderCopperGolem;
import com.deeperdepths.client.entity.model.ModelCopperGolem;
import com.deeperdepths.common.entities.EntityCopperGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCopperGolemHeldItem implements LayerRenderer<EntityCopperGolem>
{
    private final RenderCopperGolem copperGolemRenderer;
    private float holdingOffset;

    public LayerCopperGolemHeldItem(RenderCopperGolem copperGolemRendererIn, float heldOffsetIn)
    {
        this.copperGolemRenderer = copperGolemRendererIn;
        this.holdingOffset = heldOffsetIn;
    }

    public void doRenderLayer(EntityCopperGolem entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();

        if (!itemstack.isEmpty())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();

            ModelCopperGolem model = (ModelCopperGolem) this.copperGolemRenderer.getMainModel();
            ModelRenderer main = model.main;
            ModelRenderer bodyUpper = model.upper_body;

            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();

            ModelRenderer arms = model.arms;
            /* First, move to the center. */
            GlStateManager.translate(0, 1.2F, 0.0F);

            arms.postRender(scale);
            bodyUpper.postRender(scale);

            GlStateManager.translate(0, 0.6F, -0.1F);

            if (item.isFull3D())
            {
                if (item.shouldRotateAroundWhenRendering())
                {
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(0.0F, -0.0625F, 0.0F);
                }

                GlStateManager.translate(0, 0, -0.1F);
                this.copperGolemRenderer.transformHeldFull3DItemLayer();
            }
            else
            {
                GlStateManager.scale(0.875F, 0.875F, 0.875F);
            }

            GlStateManager.rotate(-90F, 1F, 0F, 0F);

            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
