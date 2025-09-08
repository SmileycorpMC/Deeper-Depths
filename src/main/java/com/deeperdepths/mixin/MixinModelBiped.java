package com.deeperdepths.mixin;

import com.deeperdepths.common.items.ItemSpyglass;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {
    
    @Shadow
    public ModelRenderer bipedRightArm;
    @Shadow
    public ModelRenderer bipedLeftArm;
    @Shadow
    public ModelBiped.ArmPose leftArmPose;
    @Shadow
    public ModelBiped.ArmPose rightArmPose;
    @Shadow
    public ModelRenderer bipedHead;
    
    @Inject(method = "setRotationAngles", at = @At("HEAD"))
    public void setRotationAngles$HEAD(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        if (!(player.getActiveItemStack().getItem() instanceof ItemSpyglass)) return;
        leftArmPose = ModelBiped.ArmPose.EMPTY;
        rightArmPose = ModelBiped.ArmPose.EMPTY;
    }
    
    @Inject(method = "setRotationAngles", at = @At("TAIL"))
    public void setRotationAngles$TAIL(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        if (!(player.getActiveItemStack().getItem() instanceof ItemSpyglass)) return;
        boolean isRight = player.getActiveHand() == EnumHand.MAIN_HAND ^ player.getPrimaryHand() == EnumHandSide.LEFT;
        ModelRenderer arm = isRight ? bipedRightArm : bipedLeftArm;
        arm.rotateAngleX = MathHelper.clamp(bipedHead.rotateAngleX - 1.9198622f - (player.isSneaking() ? 0.2617994f : 0), -2.4f, 3.3f);
        arm.rotateAngleY = bipedHead.rotateAngleY + (isRight ? -0.2617994f : 0.2617994f);
    }
    
}
