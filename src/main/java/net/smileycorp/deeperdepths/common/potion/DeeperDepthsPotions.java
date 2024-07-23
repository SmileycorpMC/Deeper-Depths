package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.monster.EntitySlime;
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
    public static final Potion BAD_OMEN = null;
    public static final Potion TRIAL_OMEN = null;
    public static final Potion OOZING = new PotionOozing("oozing", true, 10092451, EntitySlime.class, 2, false);

    public static final PotionType OOZING_POTION = new PotionType(Constants.MODID + "." + "oozing", new PotionEffect[] { new PotionEffect(OOZING, 3600, 0, false, true)} ).setRegistryName("oozing");

    @SubscribeEvent
    public static void onPotionRegister(RegistryEvent.Register<Potion> event)
    {
        event.getRegistry().register(DeeperDepthsPotions.OOZING);
    }

    @SubscribeEvent
    public static void onPotionTypeRegister(RegistryEvent.Register<PotionType> event)
    {
        event.getRegistry().register(DeeperDepthsPotions.OOZING_POTION);
    }
}
