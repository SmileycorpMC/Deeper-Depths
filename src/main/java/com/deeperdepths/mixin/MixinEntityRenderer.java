package com.deeperdepths.mixin;

import com.deeperdepths.common.items.ItemSpyglass;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow @Final private Minecraft mc;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;turn(FF)V", ordinal = 1), method = "updateCameraAndRender")
    public void deeperdepths$turn(EntityPlayerSP instance, float yaw, float pitch, Operation<Void> original) {
        if (mc.gameSettings.thirdPersonView > 0 | !(instance.getActiveItemStack().getItem() instanceof ItemSpyglass)) original.call(instance, yaw, pitch);
        original.call(instance, yaw * 0.46875f, pitch * 0.46875f);
    }

}