package net.smileycorp.deeperdepths.common.integration;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class RaidsIntegration {
    
    public static void tickBadOmen(EntityPlayerMP player, int amplifier) {
        /*World world = player.world;
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) return;
        if (!Raid.isVillage(world, player.getPosition())) return;
        Raid raid = WorldDataRaids.getData((WorldServer) world).getRaidAt(player.getPosition());
        if (raid == null || raid.getBadOmenLevel() < raid.getMaxBadOmenLevel()) {
            if (RaidConfig.raidCenteredOnPlayer) RaidOmenTracker.setRaidStart(player);
            player.addPotionEffect(new PotionEffect(RaidsContent.RAID_OMEN, 600, amplifier));
            player.removePotionEffect(DeeperDepthsPotions.BAD_OMEN);
        }*/
    }
    
    public static boolean hasBadOmen(EntityPlayer player) {
        return /*player.isPotionActive(RaidsContent.RAID_OMEN)*/ false;
    }
    
    public static int getBadOmenLevel(EntityPlayer player) {
        return /*player.getActivePotionEffect(RaidsContent.RAID_OMEN).getAmplifier()*/ 0;
    }
    
    public static void removeBadOmen(EntityPlayer player) {
        //player.removePotionEffect(RaidsContent.RAID_OMEN);
    }
    
}
