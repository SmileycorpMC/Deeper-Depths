package com.deeperdepths.mixin;

import com.deeperdepths.common.potion.DeeperDepthsPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    //prevent rendering the sky while darkness is active
    @Inject(at = @At(value = "HEAD"), method = "renderSky(FI)V", cancellable = true)
    public void deeperdepths$renderSky(float partialTicks, int pass, CallbackInfo callback) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (!((EntityLivingBase) entity).isPotionActive(DeeperDepthsPotions.DARKNESS)) return;
        callback.cancel();
    }

}
