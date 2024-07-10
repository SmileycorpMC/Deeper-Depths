package net.smileycorp.deeperdepths.mixin;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chunk.class)
public abstract class MixinChunk {
    
    /*private final ExtendedBlockStorage[] blocks = new ExtendedBlockStorage
    
    {
    }
    
    @Inject(at = @At("HEAD"), method = "getBlockState(III)Lnet/minecraft/block/state/IBlockState;")
    public void DeeperDepths$isOutsideBuildHeight(int x, int y, int z, CallbackInfoReturnable<IBlockState> cir) {
        if (y >= 0 || y < -32) return;
        re
    }*/
    
}
