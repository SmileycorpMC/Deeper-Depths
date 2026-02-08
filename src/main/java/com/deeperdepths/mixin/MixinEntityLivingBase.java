package com.deeperdepths.mixin;

import com.deeperdepths.common.EntityMaceDamageSource;
import com.deeperdepths.common.items.DeeperDepthsItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {
    
    @Shadow
    public abstract ItemStack getActiveItemStack();
    
    @Inject(method = "onItemUseFinish", at = @At("HEAD"), cancellable = true)
    public void deeperdepths$onItemUseFinish(CallbackInfo callback) {
        if (getActiveItemStack().getItem() == DeeperDepthsItems.SPYGLASS) callback.cancel();
    }

    @Shadow protected abstract void damageArmor(float damage);
    @Shadow public abstract int getTotalArmorValue();
    @Shadow public abstract IAttributeInstance getEntityAttribute(IAttribute attribute);

    @Inject(method = "applyArmorCalculations", at = @At("HEAD"), cancellable = true)
    protected void applyArmorCalculations(DamageSource source, float damage, CallbackInfoReturnable<Float> callback)
    {
        if (source instanceof EntityMaceDamageSource && !source.isUnblockable())
        {
            EntityMaceDamageSource mace = (EntityMaceDamageSource) source;
            float breachPercent = mace.getPercentage();
            float breachBypassedDamage = damage * breachPercent;

            float nonBreachDamage = damage - breachBypassedDamage;
            float nonBreachPostArmorDamage = CombatRules.getDamageAfterAbsorb(nonBreachDamage, (float)this.getTotalArmorValue(), (float)this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            /* Only damage armor by the damage that doesn't bypass. */
            this.damageArmor(nonBreachPostArmorDamage);

            float resultDamage = breachBypassedDamage + nonBreachPostArmorDamage;

            callback.setReturnValue(resultDamage);
        }
    }
}
