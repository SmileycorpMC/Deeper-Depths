package com.deeperdepths.client;

import com.deeperdepths.animation.IAnimatedEntity;
import com.deeperdepths.client.blocks.*;
import com.deeperdepths.client.entity.*;
import com.deeperdepths.client.tesr.TESRCopperChest;
import com.deeperdepths.client.tesr.TESRTrialSpawner;
import com.deeperdepths.client.tesr.TESRVault;
import com.deeperdepths.common.CommonProxy;
import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.BlockTrialPot;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.IBlockProperties;
import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import com.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import com.deeperdepths.common.blocks.tiles.TileVault;
import com.deeperdepths.common.entities.*;
import com.deeperdepths.common.items.DeeperDepthsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.item.IMetaItem;

import java.awt.*;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid= Constants.MODID)
public class ClientProxy extends CommonProxy {

    private static float FOV = 1;
    private static float OLD_FOV = 1;
    private static float SCOPE_SCALE = 1;
    
    public static ResourceLocation SPYGLASS_TEXTURE = Constants.loc("textures/misc/spyglass_scope.png");
    
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
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.STONE_WALL, new WallStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_BLOCK, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CUT_COPPER, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CHISELED_COPPER, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_CHAINS, new CopperChainStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.WAXED_COPPER_CHAINS, new CopperChainStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_GRATE, new MetaStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_BULB, new CopperBulbStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.WAXED_COPPER_BULB, new CopperBulbStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.COPPER_LANTERN, new CopperLanternStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.STONE_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.DOUBLE_STONE_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.CUT_COPPER_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.DOUBLE_CUT_COPPER_SLAB, new SlabStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.TRIAL_POT, new StateMap.Builder().ignore(BlockTrialPot.CRACKED).build());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.VAULT, new VaultStateMapper());
        ModelLoader.setCustomStateMapper(DeeperDepthsBlocks.TRIAL_SPAWNER, new TrialSpawnerStateMapper());
        for (Block block : DeeperDepthsBlocks.COPPER_DOORS.values()) ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        for (Block block : DeeperDepthsBlocks.WAXED_COPPER_DOORS.values()) ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        ModelLoader.setCustomMeshDefinition(DeeperDepthsItems.OMINOUS_BOTTLE, stack -> new ModelResourceLocation(Constants.locStr("ominous_bottle")));
        for (Item item : DeeperDepthsItems.ITEMS) if (item instanceof IMetaItem &! (item instanceof ItemBlock &&
                ((IBlockProperties)((ItemBlock) item).getBlock()).usesCustomItemHandler())) {
            if (((IMetaItem) item).getMaxMeta() > 0) for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++)
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(Constants.locStr(((IMetaItem) item).byMeta(i))));
            else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
        }
        for (int i = 0; i < DeeperDepthsBlocks.STONE_WALL.getMaxMeta(); i++) ModelLoader.setCustomModelResourceLocation(net.minecraft.item.Item.getItemFromBlock(DeeperDepthsBlocks.STONE_WALL), i,
                    new ModelResourceLocation(Constants.locStr(DeeperDepthsBlocks.STONE_WALL.byMeta(i)), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileVault.class, new TESRVault());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTrialSpawner.class, new TESRTrialSpawner());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCopperChest.class, new TESRCopperChest());
        net.minecraft.item.Item.getItemFromBlock(DeeperDepthsBlocks.COPPER_CHEST).setTileEntityItemStackRenderer(new TESRCopperChest.Item());
        net.minecraft.item.Item.getItemFromBlock(DeeperDepthsBlocks.WAXED_COPPER_CHEST).setTileEntityItemStackRenderer(new TESRCopperChest.Item());
        RenderingRegistry.registerEntityRenderingHandler(EntityBogged.class, RenderBogged::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBreeze.class, RenderBreeze::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWindCharge.class, RenderWindCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityOminousItemSpawner.class, RenderOminousItemSpawner::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDDPainting.class, RenderDDPainting::new);
    }
    
    public static void addParticle(EnumParticleTypes type, double x, double y, double z, Color color) {
        addParticle(type, x, y, z, color, 0, 0, 0);
    }
    
    public static void addParticle(EnumParticleTypes type, Vec3d pos, Color color, double vel_x, double vel_y, double vel_z) {
        addParticle(type, pos.x, pos.y, pos.z, color, vel_x, vel_y, vel_z);
    }
    
    public static void addParticle(EnumParticleTypes type, double x, double y, double z, Color color, double vel_x, double vel_y, double vel_z) {
        Particle particle = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(type.getParticleID(), x, y, z, vel_x, vel_y, vel_z);
        particle.setRBGColorF((float) color.getRed() / 255f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f);
    }

    @Override
    public void spawnParticle(int particle, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;
        minecraft.effectRenderer.addEffect(Constants.getFactory(particle).createParticle(0, world, posX, posY, posZ, speedX, speedY, speedZ, parameters));
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null) return;
        OLD_FOV = FOV;
        if (player.getItemInUseCount() <= 0 && FOV == 1) return;
        if ((player.getActiveItemStack().getItem() != DeeperDepthsItems.SPYGLASS || mc.gameSettings.thirdPersonView != 0)
                && FOV < 1) FOV = Math.min(FOV + (1 - FOV) * 0.5f, 1);
        else if (mc.gameSettings.thirdPersonView == 0 && FOV > 0.1) FOV = Math.max(FOV + (0.1f - FOV) * 0.5f, 0.1f);
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null) return;
        if (player.getItemInUseCount() <= 0 || player.getActiveItemStack().getItem() != DeeperDepthsItems.SPYGLASS) {
            SCOPE_SCALE = 0.5f;
            return;
        }
        SCOPE_SCALE = SCOPE_SCALE + (1.125f - SCOPE_SCALE) * event.renderTickTime * 0.5f;
    }
    
    @SubscribeEvent
    public static void fovEvent(EntityViewRenderEvent.FOVModifier event) {
        if (FOV >= 1) return;
        event.setFOV((OLD_FOV + (FOV - OLD_FOV) * (float)event.getRenderPartialTicks()) * event.getFOV());
    }
    
    @SubscribeEvent
    public static void preRenderOverlay(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.thirdPersonView != 0) return;
        EntityPlayerSP player = mc.player;
        if (player == null || event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (player.getItemInUseCount() <= 0 || player.getActiveItemStack().getItem() != DeeperDepthsItems.SPYGLASS) return;
        ScaledResolution resolution = event.getResolution();
        double size = Math.min(resolution.getScaledHeight(), resolution.getScaledWidth()) * SCOPE_SCALE;
        double x = (resolution.getScaledWidth() - size) / 2d;
        double y = (resolution.getScaledHeight() - size) / 2d;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        //top
        drawBorder(0, 0, resolution.getScaledWidth(), y);
        //bottom
        drawBorder(0, y + size, resolution.getScaledWidth(), resolution.getScaledHeight());
        //left
        drawBorder(0, y, x, y + size);
        //right
        drawBorder(x + size, y, resolution.getScaledWidth(), y + size);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        mc.getTextureManager().bindTexture(SPYGLASS_TEXTURE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + size, 0).tex(0, 1).endVertex();
        bufferbuilder.pos(x + size, y + size, 0).tex(1, 1).endVertex();
        bufferbuilder.pos(x + size, y, 0).tex(1, 0).endVertex();
        bufferbuilder.pos(x, y, 0).tex(0, 0).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }

    private static void drawBorder(double x0, double y0, double x1, double y1) {
        if (x0 >= x1 || y0 >= y1) return;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x0, y1, 0).color(0, 0, 0, 1f).endVertex();
        bufferbuilder.pos(x1, y1, 0).color(0, 0, 0, 1f).endVertex();
        bufferbuilder.pos(x1, y0, 0).color(0, 0, 0, 1f).endVertex();
        bufferbuilder.pos(x0, y0, 0).color(0, 0, 0, 1f).endVertex();
        tessellator.draw();
    }
    
}
