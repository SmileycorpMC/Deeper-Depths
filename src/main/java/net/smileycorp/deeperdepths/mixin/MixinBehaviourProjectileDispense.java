package net.smileycorp.deeperdepths.mixin;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BehaviorProjectileDispense.class)
public class MixinBehaviourProjectileDispense {
    
    //mojank why
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getValue(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;"), method = "dispenseStack")
    public Comparable dispenseStack(IBlockState state, IProperty property) {
        if (!state.getPropertyKeys().contains(property)) return EnumFacing.DOWN;
        return state.getValue(property);
    }
    
}
