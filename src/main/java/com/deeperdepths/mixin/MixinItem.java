package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IHoeEfficient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {
   
    @Inject(at = @At("HEAD"), method = "getDestroySpeed", cancellable = true)
    public void getDestroySpeed(ItemStack stack, IBlockState state, CallbackInfoReturnable<Float> callback) {
        if (stack.getItem() instanceof ItemHoe && state.getBlock() instanceof IHoeEfficient)
            callback.setReturnValue(((ItemHoe) stack.getItem()).toolMaterial.getEfficiency());
    }
    
}