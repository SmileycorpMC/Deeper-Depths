package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.integration.RaidsIntegration;

public class PotionBadOmen extends PotionDeeperDepths {
    
    protected PotionBadOmen() {
        super("bad_omen", true, 0x0B6138, 0);
    }
    
    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (!(entity instanceof EntityPlayerMP)) return;
        if (((EntityPlayerMP) entity).isSpectator()) return;
        if (Loader.isModLoaded("raids")) RaidsIntegration.tickBadOmen((EntityPlayerMP) entity, amplifier);

        if (entity.world.getTotalWorldTime() % getParticleSpawnRate(entity) == 0L && !entity.world.isRemote)
        { spawnParticles(entity); }
    }

    /** The actual method to spawn particles. Make sure to use `((WorldServer)entity.world).spawnParticle` when setting up! */
    public void spawnParticles(EntityLivingBase entity)
    {
        double x = entity.posX + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat());
        double y = entity.posY + (entity.height / 2) + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat());
        double z = entity.posZ + (entity.getRNG().nextFloat() - entity.getRNG().nextFloat());

        DeeperDepths.proxy.spawnParticle(0, entity.world, x, y, z, 0, 0, 0);
    }
}
