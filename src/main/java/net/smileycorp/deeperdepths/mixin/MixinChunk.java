package net.smileycorp.deeperdepths.mixin;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(Chunk.class)
public abstract class MixinChunk {
    
    private final ExtendedBlockStorage[] blocks = new ExtendedBlockStorage
    
    {
    }
    
    @Inject(at = @At("HEAD"), method = "getBlockState(III)Lnet/minecraft/block/state/IBlockState;")
    public void DeeperDepths$isOutsideBuildHeight(int x, int y, int z, CallbackInfoReturnable<IBlockState> cir) {
        if (y >= 0 || y < -32) return;
        re
    }
    
}
