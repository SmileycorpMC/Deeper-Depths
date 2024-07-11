package net.smileycorp.deeperdepths.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.common.CommonProxy;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.smileycorp.deeperdepths.common.items.DeeperDepthsItems;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid= Constants.MODID)
public class ClientProxy extends CommonProxy {

    @Override
    public void handleAnimationPacket(int entityId, int index) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null) {
            IAnimatedEntity entity = (IAnimatedEntity) player.world.getEntityByID(entityId);
            if (entity != null) {
                if (index == -1) {
                    entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                } else {
                    entity.setAnimation(entity.getAnimations()[index]);
                }
                entity.setAnimationTick(0);
            }
        }
    }
    
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.STONE, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_BLOCK, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CUT_COPPER, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CHISELED_COPPER, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_GRATE, new MetaStateMapper());
        for (Item item : DeeperDepthsItems.ITEMS) if (item instanceof IMetaItem) {
            if (((IMetaItem) item).getMaxMeta() > 0) for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++)
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(Constants.MODID, ((IMetaItem) item).byMeta(i)));
            else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Constants.MODID, item.getRegistryName().getResourcePath()));
        }
    }
    
}
