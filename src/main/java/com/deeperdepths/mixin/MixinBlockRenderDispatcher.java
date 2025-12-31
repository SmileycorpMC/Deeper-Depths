package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IWaterloggable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.BlockFluidBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRenderDispatcher {

    private final IBlockState WATER = Blocks.WATER.getDefaultState().withProperty(BlockFluidBase.LEVEL, 0);

    @Shadow @Final private BlockFluidRenderer fluidRenderer;

    @Inject(at = @At(value = "HEAD"), method = "renderBlock", cancellable = true)
    private void deeperdepths$renderBlock(IBlockState state, BlockPos pos, IBlockAccess world, BufferBuilder buffer, CallbackInfoReturnable<Boolean> callback) {
        Block block = state.getBlock();
        if (MinecraftForgeClient.getRenderLayer() != BlockRenderLayer.TRANSLUCENT |! (block instanceof IWaterloggable)) return;
        if (!((IWaterloggable) block).isWaterLogged(world, pos, state)) return;
        fluidRenderer.renderFluid(world, WATER, pos, buffer);
        callback.setReturnValue(false);
    }

}
