package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;

//wardens love toes
public class EntityAIWardenRoar extends EntityAIBase {

    private final EntityWarden warden;
    private int lastRoar;
    private EntityLivingBase target;

    public EntityAIWardenRoar(EntityWarden warden) {
        this.warden = warden;
        setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (warden.getAttackTarget() == null || warden.getState() != EntityWarden.EnumState.IDLE) return false;
        int anger = warden.getAnger();
        return anger > 80 && anger < 100;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return warden.getState() == EntityWarden.EnumState.ROARING;
    }

    @Override
    public void startExecuting() {
        warden.setState(EntityWarden.EnumState.ROARING, 84);
        target = warden.getAttackTarget();
        roar();
    }

    @Override
    public void updateTask() {
        warden.getLookHelper().setLookPositionWithEntity(target, warden.getHorizontalFaceSpeed(), warden.getVerticalFaceSpeed());
        if (warden.ticksExisted - lastRoar > 25) roar();
    }

    private void roar() {
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_ROAR, 3, 1);
        lastRoar = warden.ticksExisted;
    }

    @Override
    public void resetTask() {
        warden.increaseAnger(target, 40);
        warden.setAttackTarget(target);
    }

    @Override
    public boolean isInterruptible() {
        return warden.getState() != EntityWarden.EnumState.ROARING;
    }

}
