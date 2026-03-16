package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.BlockDeepslate;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenMinable.class)
public class MixinWorldGenMinable {

    @Unique
    private static final IBlockState INFESTED_DEEPSLATE = DeeperDepthsBlocks.DEEPSLATE.getDefaultState().withProperty(BlockDeepslate.INFESTED, true);

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"), method = "generate")
    public boolean deeperdepths$generate$setBlockState(World instance, BlockPos pos, IBlockState newState, int flags, Operation<Boolean> original, @Local IBlockState state) {
        return original.call(instance, pos, newState.getBlock() == Blocks.MONSTER_EGG && state.getBlock() == DeeperDepthsBlocks.DEEPSLATE ? INFESTED_DEEPSLATE :  newState, flags);
    }
    
}
