package com.deeperdepths.mixin;

import com.deeperdepths.common.items.ItemExplorerMap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemMap.class)
public abstract class MixinItemMap {

    //vanilla does not send this data to the client for some reason, so we gotta do it ourself
    @Inject(at= @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setData(Ljava/lang/String;Lnet/minecraft/world/storage/WorldSavedData;)V"), method = "scaleMap")
    private static void deeperDepths$scaleMap(ItemStack stack, World world, int scale, CallbackInfo callbackInfo, @Local(ordinal = 1) MapData data) {
        if (!(stack.getItem() instanceof ItemMap)) return;
        ItemExplorerMap.Type type = ItemExplorerMap.getType(stack);
        if (type == null) return;
        ItemExplorerMap.updateCenter(stack, data);
    }

}
