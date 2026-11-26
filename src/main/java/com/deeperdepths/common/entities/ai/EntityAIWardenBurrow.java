package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntityAIWardenBurrow extends EntityAIBase {

    private final EntityWarden warden;

    public EntityAIWardenBurrow(EntityWarden warden) {
        this.warden = warden;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (warden.getBurrowingCooldown() > 0) return false;
        if (ForgeEventFactory.canEntityDespawn(warden) != Event.Result.DENY) return true;
        warden.resetBurrowingCooldown();
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return warden.getState() == EntityWarden.State.BURROWING;
    }

    @Override
    public void startExecuting() {
        warden.setState(EntityWarden.State.BURROWING);
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_DIG, 5, 1);
        warden.getNavigator().clearPath();
    }

    @Override
    public void resetTask() {
        warden.setDead();
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

}
