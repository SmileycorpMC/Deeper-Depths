package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import com.deeperdepths.config.EntityConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.smileycorp.atlas.api.util.DirectionUtils;

public class EntityAIWardenSonicBoom extends EntityAIBase {

    private final EntityWarden warden;
    private int startTime = -1;

    public EntityAIWardenSonicBoom(EntityWarden warden) {
        this.warden = warden;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = warden.getAttackTarget();
        return target != null && warden.getSonicBoomCooldown() == 0 && warden.getState() == EntityWarden.State.IDLE
                && isInRange(target);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return warden.getState() == EntityWarden.State.SONIC_BOOM;
    }

    private boolean isInRange(EntityLivingBase target) {
        double dx = target.posX - warden.posX;
        double dy = target.posY - warden.posY;
        double dz = target.posZ - warden.posZ;
        return dx * dx + dz * dz <= EntityConfig.wardenSonicBoomHorizontalRange * EntityConfig.wardenSonicBoomHorizontalRange
                && dy * dy <= EntityConfig.wardenSonicBoomVerticalRange * EntityConfig.wardenSonicBoomVerticalRange;
    }

    @Override
    public void startExecuting() {
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_SONIC_CHARGE,  3, 1);
        warden.setState(EntityWarden.State.SONIC_BOOM);
        startTime = warden.ticksExisted;
    }

    @Override
    public void updateTask() {
        if (startTime < 0 || warden.ticksExisted - startTime < 34) return;
        startTime = -1;
        EntityLivingBase target = warden.getAttackTarget();
        if (target == null) return;
        if (!isInRange(target)) return;
        Vec3d dir = DirectionUtils.getDirectionVecXZ(new Vec3d(warden.posX, warden.posY + warden.getEyeHeight() / 2f, warden.posZ),
                target.getPositionEyes(1));
        //spawn particles

        warden.playSound(DeeperDepthsSoundEvents.WARDEN_SONIC_BOOM, 3, 1);
        target.attackEntityFrom(Constants.causeSonicBoomDamage(warden), (float) EntityConfig.wardenSonicBoomDamage);
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(target, warden,
                (float) EntityConfig.wardenSonicBoomKnockback * (1 - (float)target.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue()), dir.x, dir.z);
        float strength = event.getStrength();
        target.addVelocity(strength * event.getRatioX(), (float) EntityConfig.wardenSonicBoomKnockbackYRatio * dir.y * strength, strength * event.getRatioZ());
    }

    @Override
    public void resetTask() {
        startTime = -1;
        warden.resetSonicBoomCooldown();
    }

    @Override
    public boolean isInterruptible() {
        return warden.getState() != EntityWarden.State.SONIC_BOOM;
    }

}
