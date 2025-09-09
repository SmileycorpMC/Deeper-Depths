package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.BlockDeepslate;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.entity.monster.EntitySilverfish$AIHideInStone")
public class MixinAIHideInStone extends EntityAIWander {

    public MixinAIHideInStone(EntityCreature p_i1648_1_, double p_i1648_2_) {
        super(p_i1648_1_, p_i1648_2_);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"), method = "startExecuting", cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void deeperdepths$startExecuting$setBlockState(CallbackInfo callback, World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() != DeeperDepthsBlocks.DEEPSLATE) return;
        world.setBlockState(pos, state.withProperty(BlockDeepslate.INFESTED, true), 3);
        entity.spawnExplosionParticle();
        entity.setDead();
        callback.cancel();
    }

}
