package net.smileycorp.deeperdepths.common.integration;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;
import net.smileycorp.raids.common.RaidsContent;
import net.smileycorp.raids.common.raid.Raid;
import net.smileycorp.raids.common.raid.RaidOmenTracker;
import net.smileycorp.raids.common.raid.WorldDataRaids;
import net.smileycorp.raids.config.RaidConfig;

public class RaidsIntegration {
    
    public static void tickBadOmen(EntityPlayerMP player, int amplifier) {
        World world = player.world;
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) return;
        if (!Raid.isVillage(world, player.getPosition())) return;
        Raid raid = WorldDataRaids.getData((WorldServer) world).getRaidAt(player.getPosition());
        if (raid == null || raid.getBadOmenLevel() < raid.getMaxBadOmenLevel()) {
            if (RaidConfig.raidCenteredOnPlayer) RaidOmenTracker.setRaidStart(player);
            player.addPotionEffect(new PotionEffect(RaidsContent.RAID_OMEN, 600, amplifier));
            player.removePotionEffect(DeeperDepthsPotions.BAD_OMEN);
        }
    }
    
    public static boolean hasBadOmen(EntityPlayer player) {
        return player.isPotionActive(RaidsContent.BAD_OMEN);
    }
    
    public static int getBadOmenLevel(EntityPlayer player) {
        return player.getActivePotionEffect(RaidsContent.BAD_OMEN).getAmplifier();
    }
    
    public static void removeBadOmen(EntityPlayer player) {
        player.removePotionEffect(RaidsContent.BAD_OMEN);
    }
    
}
