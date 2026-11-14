package com.deeperdepths.mixin;

import com.deeperdepths.common.items.ItemSpyglass;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Unique
    private float mcGamma = 0;

    @Shadow @Final private Minecraft mc;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;turn(FF)V", ordinal = 1), method = "updateCameraAndRender")
    public void deeperdepths$turn(EntityPlayerSP instance, float yaw, float pitch, Operation<Void> original) {
        if (mc.gameSettings.thirdPersonView > 0 | !(instance.getActiveItemStack().getItem() instanceof ItemSpyglass)) original.call(instance, yaw, pitch);
        original.call(instance, yaw * 0.46875f, pitch * 0.46875f);
    }

    //set darkness effect light colour on a cycle
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"), method = "updateLightmap")
    public void deeperdepths$updateLightmap$isPotionActive(float partialTicks, CallbackInfo callback, @Local(ordinal = 9) LocalFloatRef r, @Local(ordinal = 10) LocalFloatRef g, @Local(ordinal = 11) LocalFloatRef b) {
        Entity entity = mc.getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (!((EntityLivingBase) entity).isPotionActive(DeeperDepthsPotions.DARKNESS)) return;
        float darkness = (float) Math.sin((entity.ticksExisted + partialTicks) / 20f) * 0.5f;
        r.set(Math.max(r.get() - darkness, 0));
        g.set(Math.max(g.get() - darkness, 0));
        b.set(Math.max(b.get() - darkness, 0));
    }

    //set darkness effect light gamma on a cycle
    @Inject(at = @At(value = "HEAD"), method = "updateLightmap")
    public void deeperdepths$updateLightmap$HEAD(float partialTicks, CallbackInfo callback) {
        Entity entity = mc.getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (!((EntityLivingBase) entity).isPotionActive(DeeperDepthsPotions.DARKNESS)) return;
        //store the game settings gamma before modifying it
        mcGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting -= (float) Math.sin((entity.ticksExisted + partialTicks) / 20f) * 0.5f;
    }


    //reset game settings gamma if modified by darkness
    @Inject(at = @At(value = "RETURN"), method = "updateLightmap")
    public void deeperdepths$updateLightmap$RETURN(float partialTicks, CallbackInfo callback) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (!((EntityLivingBase) entity).isPotionActive(DeeperDepthsPotions.DARKNESS)) return;
        mc.gameSettings.gammaSetting = mcGamma;
    }

}