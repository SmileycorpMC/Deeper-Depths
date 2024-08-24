package net.smileycorp.deeperdepths.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.deeperdepths.animation.AnimationMessage;
import net.smileycorp.deeperdepths.common.Constants;

public class NetworkHandler {
    
    public static SimpleNetworkWrapper network;
    
    public static void init() {
        int packetId = 0;
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
        network.registerMessage(AnimationMessage.Handler.class, AnimationMessage.class, packetId++, Side.SERVER);
        network.registerMessage(ParticleMessage.Handler.class, ParticleMessage.class, packetId++, Side.CLIENT);
    }
    
}
