package com.deeperdepths.common.network;

import com.deeperdepths.animation.AnimationMessage;
import com.deeperdepths.common.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    
    public static SimpleNetworkWrapper network;
    
    public static void init() {
        int packetId = 0;
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
        network.registerMessage(AnimationMessage.Handler.class, AnimationMessage.class, packetId++, Side.SERVER);
        network.registerMessage(ParticleMessage.Handler.class, ParticleMessage.class, packetId++, Side.CLIENT);
    }
    
}
