package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.entities.EntityWarden;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIWardenInvestigateDisturbance extends EntityAIBase {

    private final EntityWarden warden;
    private int updateTicks = 0;

    public EntityAIWardenInvestigateDisturbance(EntityWarden warden) {
        this.warden = warden;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return warden.getAttackTarget() == null && warden.getDisturbance() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        Vec3d disturbance = warden.getDisturbance();
        return disturbance != null && warden.getDistanceSq(disturbance.x, disturbance.y, disturbance.z) > 1;
    }

    @Override
    public void startExecuting() {
        Vec3d disturbance = warden.getDisturbance();
        warden.getNavigator().tryMoveToXYZ(disturbance.x, disturbance.y, disturbance.z, 0.5);
        updateTicks = 10;
    }

    @Override
    public void updateTask() {
        if (updateTicks-- > 0) return;
        Vec3d disturbance = warden.getDisturbance();
        warden.getNavigator().tryMoveToXYZ(disturbance.x, disturbance.y, disturbance.z, 0.5);
        updateTicks = 10;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        updateTicks = 0;
        warden.setDisturbance(null);
    }

}
