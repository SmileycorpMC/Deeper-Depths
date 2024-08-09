package net.smileycorp.deeperdepths.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.potion.PotionEffect;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to handle Weaving affecting movement speed through Webs.
 */
@Mixin(Entity.class)
public class MixinEntity
{
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public boolean isInWeb;

    /** Places this directly before web movement is handles in `Entity.class`, basically hijacking it */
    @Inject(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;isInWeb:Z", shift = At.Shift.BEFORE))
    public void modifyWebMovementSpeed(MoverType type, double x, double y, double z, CallbackInfo ci)
    {
        Entity entity = (Entity) (Object) this;
        if (this.isInWeb)
        {
            if (entity instanceof EntityLivingBase)
            {
                PotionEffect regenerationEffect = ((EntityLivingBase)entity).getActivePotionEffect(DeeperDepthsPotions.WEAVING);
                if (regenerationEffect != null)
                {
                    /* Set to false, so the normal Web Movement code doesn't need to be ran. */
                    this.isInWeb = false;

                    double webSpeedModifier = 0.5;
                    x *= webSpeedModifier;
                    /* Jumping is supposed to be far weaker in Webs. Normal code has it about 5 times weaker than the movement speed. */
                    y *= webSpeedModifier / 5;
                    z *= webSpeedModifier;
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                }
            }
        }
    }
}