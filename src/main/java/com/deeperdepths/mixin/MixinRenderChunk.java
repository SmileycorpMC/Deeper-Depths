package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IWaterloggable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Iterator;

@Mixin(RenderChunk.class)
public abstract class MixinRenderChunk {

    @Shadow private ChunkCache worldView;

    @Shadow protected abstract void preRenderBlocks(BufferBuilder bufferBuilderIn, BlockPos pos);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;canRenderInLayer(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z"), method = "rebuildChunk", locals = LocalCapture.CAPTURE_FAILHARD)
    private void deeperdepths$rebuildChunk$canRenderInLayer(float x, float y, float z, ChunkCompileTaskGenerator generator, CallbackInfo ci, CompiledChunk compiledchunk, int i, BlockPos blockpos, BlockPos blockpos1, VisGraph lvt_9_1_, HashSet lvt_10_1_, boolean[] aboolean, BlockRendererDispatcher blockrendererdispatcher, Iterator var13, BlockPos.MutableBlockPos blockpos$mutableblockpos, IBlockState iblockstate, Block block, BlockRenderLayer[] var17, int var18, int var19, BlockRenderLayer blockrenderlayer1) {
        if (blockrenderlayer1 != BlockRenderLayer.TRANSLUCENT || iblockstate.getRenderType() == EnumBlockRenderType.INVISIBLE) return;
        if (!(block instanceof IWaterloggable)) return;
        if (!((IWaterloggable) block).isWaterLogged(worldView, blockpos$mutableblockpos, iblockstate)) return;
        ForgeHooksClient.setRenderLayer(blockrenderlayer1);
        BufferBuilder buffer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(blockrenderlayer1.ordinal());
        if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
            compiledchunk.setLayerStarted(blockrenderlayer1);
            preRenderBlocks(buffer, blockpos);
        }
        blockrendererdispatcher.renderBlock(iblockstate, blockpos$mutableblockpos, worldView, buffer);
        aboolean[blockrenderlayer1.ordinal()] = true;
    }

}
