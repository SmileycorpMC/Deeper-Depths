package net.smileycorp.deeperdepths.common.integration;

import net.minecraft.entity.player.EntityPlayerMP;

public class RaidsIntegration {
    
    public static void tickBadOmen(EntityPlayerMP player, int amplifier) {
      /*  World world = player.world;
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) return;
        if (!Raid.isVillage(world, player.getPosition())) return;
        if (RaidConfig.ominousBottles) {
            Raid raid = WorldDataRaids.getData((WorldServer) world).getRaidAt(player.getPosition());
            if (raid == null || raid.getBadOmenLevel() < raid.getMaxBadOmenLevel()) {
                if (RaidConfig.raidCenteredOnPlayer) RaidOmenTracker.setRaidStart(player);
                player.addPotionEffect(new PotionEffect(RaidsContent.RAID_OMEN, 600, amplifier));
                player.removePotionEffect(RaidsContent.BAD_OMEN);
            }
        }
        else WorldDataRaids.getData((WorldServer) world).createOrExtendRaid(player);*/
    }
    
    
}
