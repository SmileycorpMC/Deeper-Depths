package com.deeperdepths.mixin;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockDispenser.class)
public class MixinBlockDispenser {
    
    //yeah this needs 3 mixins, because why would things ever be simple
    //I shoulda just hardcoded it, damn it
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getValue(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;"), method = "getDispensePosition")
    private static Comparable getDispensePosition(IBlockState state, IProperty property) {
        if (!state.getPropertyKeys().contains(property)) return EnumFacing.DOWN;
        return state.getValue(property);
    }
    
}
