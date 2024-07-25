package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.deeperdepths.common.integration.RaidsIntegration;

public class PotionBadOmen extends PotionDeeperDepths {
    
    protected PotionBadOmen() {
        super("Bad_Omen", true, 0x0B6138);
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
    }
    
}