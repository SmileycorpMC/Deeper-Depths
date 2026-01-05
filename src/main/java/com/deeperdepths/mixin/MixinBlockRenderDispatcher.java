package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IFluidloggable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRenderDispatcher {

    @Shadow @Final private BlockFluidRenderer fluidRenderer;

    @Inject(at = @At(value = "HEAD"), method = "renderBlock", cancellable = true)
    private void deeperdepths$renderBlock(IBlockState state, BlockPos pos, IBlockAccess world, BufferBuilder buffer, CallbackInfoReturnable<Boolean> callback) {
        Block block = state.getBlock();
        if (MinecraftForgeClient.getRenderLayer() != BlockRenderLayer.TRANSLUCENT |! (block instanceof IFluidloggable)) return;
        if (!((IFluidloggable) block).isFluidLogged(world, pos, state)) return;
        Optional<Fluid> optional = ((IFluidloggable) block).getContainedFluid(world, pos, state);
        if (!optional.isPresent()) return;
        double x = buffer.xOffset;
        double y = buffer.yOffset;
        double z = buffer.zOffset;
        buffer.setTranslation(x, y + 0.001, z);
        fluidRenderer.renderFluid(world, optional.get().getBlock().getDefaultState(), pos, buffer);
        //buffer.setTranslation(0, 0, 0);
        callback.setReturnValue(false);
    }

}
