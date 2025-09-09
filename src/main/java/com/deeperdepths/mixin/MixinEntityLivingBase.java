package com.deeperdepths.mixin;

import com.deeperdepths.common.items.DeeperDepthsItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {
    
    @Shadow
    public abstract ItemStack getActiveItemStack();
    
    @Inject(method = "onItemUseFinish", at = @At("HEAD"), cancellable = true)
    public void deeperdepths$onItemUseFinish(CallbackInfo callback) {
        if (getActiveItemStack().getItem() == DeeperDepthsItems.SPYGLASS) callback.cancel();
    }
    
}
