package net.smileycorp.deeperdepths.mixin;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BehaviorDefaultDispenseItem.class)
public class MixinBehaviorDefaultDispenseItem {
    
    //has to exist to make dispensing items with anything other than a dispenser not work
    //for some reason the entire dispenser code is completely just messed up and doesn't actually work properly
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getValue(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;"), method = "dispenseStack")
    public Comparable dispenseStack(IBlockState state, IProperty property) {
        if (!state.getPropertyKeys().contains(property)) return EnumFacing.DOWN;
        return state.getValue(property);
    }
    
    //4 mixin injects (ㅠ﹏ㅠ)
    //surely there's gotta be an easier way to do this without the hardcoding
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getValue(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;"), method = "dispense")
    public Comparable dispense(IBlockState state, IProperty property) {
        if (!state.getPropertyKeys().contains(property)) return EnumFacing.DOWN;
        return state.getValue(property);
    }
    
}
