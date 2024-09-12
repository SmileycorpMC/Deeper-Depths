package com.deeperdepths.mixin;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;

@Mixin(InventoryEffectRenderer.class)
public class MixinInventoryEffectRenderer {
    
    private int amplifier = 0;
    
    @Inject(method = "drawActivePotionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void drawActivePotionEffects(CallbackInfo ci, int i, int j, int k, Collection collection, int l, Iterator var6, PotionEffect potioneffect, Potion potion) {
        amplifier = potioneffect.getAmplifier();
    }
    
    @Redirect(method = "drawActivePotionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", ordinal = 0))
    public String drawActivePotionEffects(String translateKey, Object[] parameters) {
        String string = I18n.format(translateKey, parameters);
        if (amplifier > 3) string += " " + I18n.format("enchantment.level." + (amplifier + 1));
        amplifier = 0;
        return string;
    }
    
}
