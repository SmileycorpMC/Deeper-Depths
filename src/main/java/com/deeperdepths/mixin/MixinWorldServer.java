package com.deeperdepths.mixin;

import com.deeperdepths.common.capabilities.LightingRods;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WorldServer.class)
public class MixinWorldServer {
    
    @Inject(at = @At("HEAD"), method = "adjustPosToNearbyEntity", cancellable = true)
    public void adjustPosToNearbyEntity(BlockPos pos, CallbackInfoReturnable<BlockPos> callback) {
        Optional<BlockPos> optional = LightingRods.get((WorldServer)(Object)this).getClosest((WorldServer)(Object)this, pos, 128);
        if (optional.isPresent()) callback.setReturnValue(optional.get().up());
    }
    
}
