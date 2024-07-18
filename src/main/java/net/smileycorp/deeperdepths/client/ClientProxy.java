package net.smileycorp.deeperdepths.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.client.blocks.*;
import net.smileycorp.deeperdepths.common.CommonProxy;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.smileycorp.deeperdepths.common.blocks.IBlockProperties;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialSpawner;
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileTrialSpawner.class, new TileEntityMobSpawnerRenderer());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.STONE, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.STONE_WALL, new WallStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_BLOCK, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CUT_COPPER, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CHISELED_COPPER, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_GRATE, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_BULB, new CopperBulbStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.WAXED_COPPER_BULB, new CopperBulbStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.STONE_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.DOUBLE_STONE_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CUT_COPPER_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.DOUBLE_CUT_COPPER_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.TRIAL_POT, new DirectionalStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.VAULT, new VaultStateMapper());
        ModelLoader.setCustomMeshDefinition(DeeperDepthsItems.OMINOUS_BOTTLE, stack -> new ModelResourceLocation(Constants.locStr("ominous_bottle")));
        for (Item item : DeeperDepthsItems.ITEMS) if (item instanceof IMetaItem &! (item instanceof ItemBlock &&
                ((IBlockProperties)((ItemBlock) item).getBlock()).usesCustomItemHandler())) {
            if (((IMetaItem) item).getMaxMeta() > 0) for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++) {
                ModelResourceLocation loc = new ModelResourceLocation(Constants.locStr(((IMetaItem) item).byMeta(i)));
                ModelLoader.setCustomModelResourceLocation(item, i, loc);
            }
            else {
                ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName().toString());
                ModelLoader.setCustomModelResourceLocation(item, 0, loc);
            }
        }
        for (int i = 0; i < DeeperDepthsBlocks.STONE_WALL.getMaxMeta(); i++) ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(DeeperDepthsBlocks.STONE_WALL), i,
                    new ModelResourceLocation(Constants.locStr(DeeperDepthsBlocks.STONE_WALL.byMeta(i)), "inventory"));
    }
    
}
