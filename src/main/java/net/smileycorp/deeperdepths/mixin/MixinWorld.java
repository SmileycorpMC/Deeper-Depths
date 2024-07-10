package net.smileycorp.deeperdepths.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
    
    @Shadow @Final public WorldProvider provider;
    
    @Inject(at = @At("HEAD"), method = "isOutsideBuildHeight")
    public void DeeperDepths$isOutsideBuildHeight(BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        if (provider.getDimension() == 0) callback.setReturnValue(pos.getY() < -32 || pos.getY() >= 256 );
    }
    
}
