package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

//wardens love toes
public class EntityAIWardenSniff extends EntityAIBase {

    private final EntityWarden warden;
    private int lastSniff = 0;
    private int sniffCooldown;

    public EntityAIWardenSniff(EntityWarden warden) {
        this.warden = warden;
        setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        return warden.getAttackTarget() == null && warden.getState() == EntityWarden.EnumState.IDLE
                && warden.ticksExisted + sniffCooldown - lastSniff <= 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return warden.getState() == EntityWarden.EnumState.SNIFFING;
    }

    @Override
    public void startExecuting() {
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_SNIFF, 5, 1);
        warden.setState(EntityWarden.EnumState.SNIFFING, 84);
    }

    @Override
    public void resetTask() {
        warden.getNavigator().clearPath();
        lastSniff = warden.ticksExisted;
        sniffCooldown = 100 + warden.getRNG() .nextInt(100);
        //find the stinkiest nearby toes
        EntityLivingBase entity = warden.world.getClosestPlayer(warden.posX, warden.posY, warden.posZ, 20, this::canSmell);
        if (entity == null) {
            double distance = 24 * 24;
            for (EntityLivingBase entity1 : warden.world.getEntitiesWithinAABB(EntityLivingBase.class, warden.getEntityBoundingBox().grow(7), this::canSmell)) {
                double distance1 = warden.getDistanceSq(entity1);
                if (distance1 < distance){
                    entity = entity1;
                    distance = distance1;
                }
            }
        }
        if (entity != null) warden.increaseAnger(entity, 35);
    }

    //stinky toes
    private boolean canSmell(Entity entity) {
        if (!warden.canTarget(entity)) return false;
        double dx = warden.posX - entity.posX;
        double dy = warden.posY - entity.posY;
        double dz = warden.posZ - entity.posZ;
        return dx * dx + dz + dz < 6 * 6 && dy * dx < 20 * 20;
    }

    @Override
    public boolean isInterruptible() {
        return warden.getState() != EntityWarden.EnumState.SNIFFING;
    }

}
