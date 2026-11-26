package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.entities.EntityWarden;
import com.deeperdepths.config.EntityConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

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
        return warden.getState() == EntityWarden.State.IDLE && warden.ticksExisted - lastSniff - sniffCooldown >= 0
                && warden.getAngerTarget() != null && warden.getAttackTarget() == null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return warden.getState() == EntityWarden.State.SNIFFING;
    }

    @Override
    public void startExecuting() {
        warden.getNavigator().clearPath();
        warden.playSound(DeeperDepthsSoundEvents.WARDEN_SNIFF, 5, 1);
        warden.setState(EntityWarden.State.SNIFFING);
    }

    @Override
    public void resetTask() {
        warden.getNavigator().clearPath();
        lastSniff = warden.ticksExisted;
        sniffCooldown = EntityConfig.wardenSniffCooldownMin + EntityConfig.wardenSniffCooldownMin < EntityConfig.wardenSniffCooldownMax ?
                warden.getRNG().nextInt(EntityConfig.wardenSniffCooldownMax - EntityConfig.wardenSniffCooldownMin) : 0;
        EntityLivingBase entity = getClosestEntity(EntityPlayer.class);
        if (entity == null) entity = getClosestEntity(EntityLivingBase.class);
        if (entity != null) warden.increaseAnger(entity, EntityConfig.wardenAngerGainSniff);
    }

    //find the stinkiest nearby toes
    private <T extends EntityLivingBase> T getClosestEntity(Class<T> clazz) {
        T entity = null;
        double horizontal = EntityConfig.wardenSniffHorizontalRange;
        double vertical = EntityConfig.wardenSniffHorizontalRange;
        double distance = horizontal * horizontal * 2 + vertical * vertical;
        for (T entity1 : warden.world.getEntitiesWithinAABB(clazz, new AxisAlignedBB(warden.posX - horizontal, warden.posY - vertical,
                warden.posZ - horizontal, warden.posX + horizontal, warden.posY + vertical, warden.posZ + horizontal), this::canSmell)) {
            double distance1 = warden.getDistanceSq(entity1);
            if (distance1 < distance){
                entity = entity1;
                distance = distance1;
            }
        }
        return entity;
    }

    //stinky toes
    private boolean canSmell(Entity entity) {
        if (!EntityWarden.canTarget(entity)) return false;
        double dx = warden.posX - entity.posX;
        double dy = warden.posY - entity.posY;
        double dz = warden.posZ - entity.posZ;
        return dx * dx + dz + dz < 6 * 6 && dy * dy < 20 * 20;
    }

    @Override
    public boolean isInterruptible() {
        return warden.getState() != EntityWarden.State.SNIFFING;
    }

}
