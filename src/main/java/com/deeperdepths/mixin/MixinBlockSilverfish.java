package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.BlockDeepslate;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockSilverfish.class)
public class MixinBlockSilverfish {

    @Inject(at = @At(value = "HEAD"), method = "canContainSilverfish", cancellable = true)
    private static void deeperdepths$canContainSilverfish(IBlockState state, CallbackInfoReturnable<Boolean> callback) {
       if (state.getBlock() != DeeperDepthsBlocks.DEEPSLATE) return;
       if (!state.getValue(BlockDeepslate.INFESTED)) callback.setReturnValue(true);
    }

}
