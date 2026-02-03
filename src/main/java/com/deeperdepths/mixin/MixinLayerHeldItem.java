package com.deeperdepths.mixin;

import com.deeperdepths.common.items.ItemSpyglass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {

    @Shadow @Final protected RenderLivingBase<?> livingEntityRenderer;

    @Inject(method = "renderHeldItem", at = @At("HEAD"), cancellable = true)
    public void deeperdepths$setRotationAngles$TAIL(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType transforms, EnumHandSide hand, CallbackInfo callback) {
        if (!(entity instanceof EntityPlayer)) return;
        if (entity.getActiveItemStack() != stack || !(stack.getItem() instanceof ItemSpyglass)) return;
        if (!(livingEntityRenderer.getMainModel() instanceof ModelBiped)) return;
        GlStateManager.pushMatrix();
        ModelRenderer head = ((ModelBiped) livingEntityRenderer.getMainModel()).bipedHead;
        float rotX = head.rotateAngleX;
        head.rotateAngleX = MathHelper.clamp(head.rotateAngleX, (-(float)Math.PI / 6f), ((float)Math.PI / 2f));
        head.postRender(0.0625f);
        head.rotateAngleX = rotX;
        GlStateManager.translate(0, -0.25f, 0);
        GlStateManager.rotate(180, 0, 1, 0);
        GlStateManager.scale(0.625f, -0.625f, -0.625f);
        GlStateManager.translate((hand == EnumHandSide.LEFT ? -0.15625 : 0.15625)- 0.234375, -0.015625, -0.3125);
        Minecraft.getMinecraft().getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.HEAD);
        GlStateManager.popMatrix();
        callback.cancel();
    }

}
