package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.BlockDeepslate;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(targets = "net.minecraft.entity.monster.EntitySilverfish$AISummonSilverfish")
public class MixinAISummonSilverfish {

    @Shadow @Final private EntitySilverfish silverfish;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getBlock()Lnet/minecraft/block/Block;"), method = "updateTask", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void deeperdepths$startExecuting$getBlock(CallbackInfo callback, World world, Random rand, BlockPos base, int i, int j, int k, BlockPos pos, IBlockState state) {
        if (state.getBlock() != DeeperDepthsBlocks.DEEPSLATE) return;
        if (!state.getValue(BlockDeepslate.INFESTED)) return;
        if (ForgeEventFactory.getMobGriefingEvent(world, silverfish)) world.destroyBlock(pos, true);
        else world.setBlockState(pos, state.withProperty(BlockDeepslate.INFESTED, false), 3);
        if (rand.nextBoolean()) callback.cancel();
    }

}
