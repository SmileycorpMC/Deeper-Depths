package net.smileycorp.deeperdepths.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.deeperdepths.animation.AnimationMessage;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent event) {
    }
    
    public void init(FMLInitializationEvent event) {
        int packetId = 0;
        DeeperDepths.network.registerMessage(AnimationMessage.Handler.class, AnimationMessage.class, packetId++, Side.SERVER);
    }
    
    public void postInit(FMLPostInitializationEvent event) {
    }
    
    public void serverStart(FMLServerStartingEvent event) {
    }


    public void handleAnimationPacket(int entityId, int index) {

    }
    
}
