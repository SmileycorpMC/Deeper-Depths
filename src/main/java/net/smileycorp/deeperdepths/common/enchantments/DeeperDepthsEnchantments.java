package net.smileycorp.deeperdepths.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.deeperdepths.common.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DeeperDepthsEnchantments
{
    public static Enchantment BREACH = new EnchantmentBreach();
    public static Enchantment DENSITY = new EnchantmentDensity();
    public static Enchantment WIND_BURST = new EnchantmentWindBurst();

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        IForgeRegistry<Enchantment> registry = event.getRegistry();
        registry.register(BREACH);
        registry.register(DENSITY);
        registry.register(WIND_BURST);
    }
}