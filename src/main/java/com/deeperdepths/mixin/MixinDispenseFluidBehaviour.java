package com.deeperdepths.mixin;

import com.deeperdepths.common.blocks.IFluidloggable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = DispenseFluidContainer.class, remap = false)
public class MixinDispenseFluidBehaviour {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidUtil;tryPlaceFluid(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraftforge/fluids/FluidActionResult;"), method = "dumpContainer")
    public FluidActionResult deeperdepths$dumpContainer$tryPlaceFluid(EntityPlayer player, World world, BlockPos pos, ItemStack bucket, FluidStack fluidStack, Operation<FluidActionResult> original) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        IFluidHandlerItem cap = FluidUtil.getFluidHandler(bucket);
        if (!(block instanceof IFluidloggable) || cap == null) return original.call(player, world, pos, bucket, fluidStack);
        IFluidloggable loggableBlock = (IFluidloggable) block;
        if (fluidStack.amount < 1000 |! loggableBlock.canContainFluid(world, pos, state, fluidStack.getFluid())) return original.call(player, world, pos, bucket, fluidStack);
        if (cap.drain(fluidStack, false).amount < 1000) return original.call(player, world, pos, bucket, fluidStack);
        if (world.provider.doesWaterVaporize()) {
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
            for (int k = 0; k < 8; ++k)
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
        } else {
            world.playSound(player, pos, fluidStack.getFluid().getEmptySound(fluidStack), SoundCategory.BLOCKS, 1, 1);
            loggableBlock.fillWithFluid(world, pos, state, fluidStack.getFluid());
        }
        cap.drain(fluidStack, true);
        return new FluidActionResult(cap.getContainer().copy());
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidUtil;tryPickUpFluid(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Lnet/minecraftforge/fluids/FluidActionResult;"), method = "fillContainer")
    public FluidActionResult deeperdepths$fillContainer$tryPickUpFluid(ItemStack bucket, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, Operation<FluidActionResult> original) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        IFluidHandlerItem cap = FluidUtil.getFluidHandler(bucket);
        if (!(block instanceof IFluidloggable) || cap == null) return original.call(bucket, player, world, pos, facing);
        IFluidloggable loggableBlock = (IFluidloggable) block;
        Optional<Fluid> optional = loggableBlock.getContainedFluid(world, pos, state);
        if (!optional.isPresent()) return original.call(bucket, player, world, pos, facing);
        Fluid fluid = optional.get();
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        if (cap.fill(fluidStack, false) < 1000) return original.call(bucket, player, world, pos, facing);
        world.playSound(player, pos, fluidStack.getFluid().getFillSound(fluidStack), SoundCategory.BLOCKS, 1, 1);
        loggableBlock.empty(world, pos, state);
        cap.fill(fluidStack, true);
        return new FluidActionResult(cap.getContainer().copy());
    }

}
