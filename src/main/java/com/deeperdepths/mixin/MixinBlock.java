package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IFluidloggable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(at = @At(value = "HEAD"), method = "canRenderInLayer", cancellable = true, remap = false)
    private void deeperdepths$canRenderInLayer(IBlockState state, BlockRenderLayer layer, CallbackInfoReturnable<Boolean> callback) {
        if (layer != BlockRenderLayer.TRANSLUCENT | !(this instanceof IFluidloggable)) return;
        callback.setReturnValue(true);
    }

}
