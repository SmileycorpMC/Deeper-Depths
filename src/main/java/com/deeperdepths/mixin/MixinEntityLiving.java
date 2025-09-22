package com.deeperdepths.mixin;

import com.deeperdepths.common.items.DeeperDepthsItems;
import com.deeperdepths.config.EntityConfig;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {

    @Shadow public abstract void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack);

    public MixinEntityLiving(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @Inject(method = "setEquipmentBasedOnDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/EntityEquipmentSlot;values()[Lnet/minecraft/inventory/EntityEquipmentSlot;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void deeperdepths$setEquipmentBasedOnDifficulty(DifficultyInstance difficulty, CallbackInfo callback, int level, float chance) {
        if (level != 0 && level != 1) return;
        if (EntityConfig.copperArmourChance < 1) return;
        if (rand.nextInt(EntityConfig.copperArmourChance) > 0) return;
        callback.cancel();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) continue;
            if (!getItemStackFromSlot(slot).isEmpty()) continue;
            if (slot != EntityEquipmentSlot.FEET && rand.nextFloat() < chance) break;
            setItemStackToSlot(slot, new ItemStack(DeeperDepthsItems.COPPER_ARMOR.getItem(slot)));
        }
    }
    
}
