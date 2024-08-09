package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.deeperdepths.common.Constants;


@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsPotions
{
    public static final Potion BAD_OMEN = new PotionBadOmen();
    public static final Potion TRIAL_OMEN = new PotionDeeperDepths("trial_omen", false, 0x16A6A6, 2);
    public static final Potion OOZING = new PotionOozing("oozing", true, 10092451, EntitySlime.class, 2, false);
    public static final Potion INFESTED = new PotionInfested("infested", true, 9214860, EntitySilverfish.class, 10, 1, 2, false);
    public static final Potion WEAVING = new PotionWeaving("weaving", true, 7891290, Blocks.WEB, 2, 3, 1);
    public static final Potion WIND_CHARGED = new PotionWindCharged("wind_charged", true, 12438015);

    public static final PotionType OOZING_POTION = new PotionType(Constants.MODID + "." + "oozing", new PotionEffect[] { new PotionEffect(OOZING, 3600, 0)} ).setRegistryName("oozing");
    public static final PotionType INFESTED_POTION = new PotionType(Constants.MODID + "." + "infested", new PotionEffect[] { new PotionEffect(INFESTED, 3600, 0)} ).setRegistryName("infested");
    public static final PotionType WEAVING_POTION = new PotionType(Constants.MODID + "." + "weaving", new PotionEffect[] { new PotionEffect(WEAVING, 3600, 0)} ).setRegistryName("weaving");
    public static final PotionType WIND_CHARGED_POTION = new PotionType(Constants.MODID + "." + "wind_charged", new PotionEffect[] { new PotionEffect(WIND_CHARGED, 3600, 0)} ).setRegistryName("wind_charged");

    @SubscribeEvent
    public static void onPotionRegister(RegistryEvent.Register<Potion> event)
    {
        event.getRegistry().register(BAD_OMEN);
        event.getRegistry().register(TRIAL_OMEN);
        event.getRegistry().register(DeeperDepthsPotions.OOZING);
        event.getRegistry().register(DeeperDepthsPotions.INFESTED);
        event.getRegistry().register(DeeperDepthsPotions.WEAVING);
        event.getRegistry().register(DeeperDepthsPotions.WIND_CHARGED);
    }

    @SubscribeEvent
    public static void onPotionTypeRegister(RegistryEvent.Register<PotionType> event)
    {
        event.getRegistry().register(DeeperDepthsPotions.OOZING_POTION);
        event.getRegistry().register(DeeperDepthsPotions.INFESTED_POTION);
        event.getRegistry().register(DeeperDepthsPotions.WEAVING_POTION);
        event.getRegistry().register(DeeperDepthsPotions.WIND_CHARGED_POTION);
    }
}