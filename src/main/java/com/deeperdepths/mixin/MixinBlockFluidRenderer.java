package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IWaterloggable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockFluidRenderer.class)
public class MixinBlockFluidRenderer {

    private final IBlockState WATER = Blocks.WATER.getDefaultState();

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"), method = "getFluidHeight")
    private IBlockState deeperdepths$getFluidHeight$getBlockState(IBlockAccess world, BlockPos pos, Operation<IBlockState> operation) {
        IBlockState state = operation.call(world, pos);
        Block block = state.getBlock();
        if (!(block instanceof IWaterloggable)) return state;
        return ((IWaterloggable) block).isWaterLogged(world, pos, state) ? WATER : state;
    }

}
