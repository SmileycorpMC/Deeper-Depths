package com.deeperdepths.common.network;

import com.deeperdepths.animation.AnimationMessage;
import com.deeperdepths.common.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    
    public static SimpleNetworkWrapper network;
    
    public static void init() {
        int packetId = 0;
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
        network.registerMessage(AnimationMessage.Handler.class, AnimationMessage.class, packetId++, Side.SERVER);
        network.registerMessage(ParticleMessage.Handler.class, ParticleMessage.class, packetId++, Side.CLIENT);
        registerOmniMessage(SpawnerConfigMessage::process, SpawnerConfigMessage.class, packetId++);
    }

    private static <T extends IMessage> void registerOmniMessage(IMessageHandler<? super T, IMessage> handler, Class<T> message, int id) {
        network.registerMessage(handler, message, id, Side.CLIENT);
        network.registerMessage(handler, message, id, Side.SERVER);
    }
    
}
