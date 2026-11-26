package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntityAIWardenEmerge extends EntityAIBase {

    private final EntityWarden warden;

    public EntityAIWardenEmerge(EntityWarden warden) {
        this.warden = warden;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return warden.getState() == EntityWarden.EnumState.EMERGING;
    }

    @Override
    public void startExecuting() {
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_EMERGE, 5, 1);
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

}
