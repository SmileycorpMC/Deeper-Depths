package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import com.deeperdepths.config.EntityConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIWardenRoar extends EntityAIBase {

    private final EntityWarden warden;
    private EntityLivingBase target;

    public EntityAIWardenRoar(EntityWarden warden) {
        this.warden = warden;
        setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        return warden.getAnger() > 80 && warden.getAttackTarget() == null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return warden.getState() == EntityWarden.State.ROARING;
    }

    @Override
    public void startExecuting() {
        warden.getNavigator().clearPath();
        warden.setState(EntityWarden.State.ROARING);
        target = warden.getAngerTarget();
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_ROAR, 3, 1);
    }

    @Override
    public void updateTask() {
        warden.getLookHelper().setLookPositionWithEntity(target, warden.getHorizontalFaceSpeed(), warden.getVerticalFaceSpeed());
    }

    @Override
    public void resetTask() {
        warden.increaseAnger(target, EntityConfig.wardenAngerGainRoar);
        warden.setAttackTarget(target);
    }

    @Override
    public boolean isInterruptible() {
        return warden.getState() != EntityWarden.State.ROARING;
    }

}
