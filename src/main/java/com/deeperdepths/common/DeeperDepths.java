package com.deeperdepths.common;

import com.deeperdepths.common.world.DDOreGen;
import com.deeperdepths.common.world.DDRegisterStructures;
import com.deeperdepths.common.world.DDWorldGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class DeeperDepths {
    
    private static Logger logger = LogManager.getLogger(Constants.NAME);
    
    @SidedProxy(clientSide = Constants.CLIENT, serverSide = Constants.SERVER)
    public static CommonProxy proxy;
    
    public static final CreativeTabs CREATIVE_TAB = new DeeperDepthsTab();

    @Mod.Instance
    public static DeeperDepths instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);

        //registers the World Generator for all structures that we add
        GameRegistry.registerWorldGenerator(new DDWorldGen(), 1);
        GameRegistry.registerWorldGenerator(new DDOreGen(), 99);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
        //Handles structure registration
        DDRegisterStructures.handleStructureRegistries();

    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
    
    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event){
        proxy.serverStart(event);
    }

    public static void info(Object message) {
        logger.info(message);
    }
    
    public static void error(Object message, Throwable e) {
        logger.error(message, e);
        e.printStackTrace();
    }

    /**
     * Make sure Unseen finishes this before going public using this animation system!
     * Reference this for examples
     * https://github.com/Jboymercs/test-ai-3/tree/main/animation/example
     * @param message
     * @param <MSG>
     */
    public static <MSG extends IMessage> void sendMSGToAll(MSG message) {
        //TODO Still searching for proper method
        //  for(EntityPlayerMP playerMP : Minecraft.getMinecraft().) {
        //  sendNonLocal(message, playerMP);
        //  }
        //network.sendToAll(message);
    }
}
