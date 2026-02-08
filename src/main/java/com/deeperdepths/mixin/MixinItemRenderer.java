package com.deeperdepths.mixin;

import com.deeperdepths.common.items.ItemExplorerMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Shadow @Final private Minecraft mc;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/MapItemRenderer;renderMap(Lnet/minecraft/world/storage/MapData;Z)V", shift = At.Shift.AFTER), method = "renderMapFirstPerson(Lnet/minecraft/item/ItemStack;)V")
    public void deeperdepths$renderMapFirstPerson(ItemStack stack, CallbackInfo callback) {
        if (!stack.hasTagCompound()) return;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("DDStructure")) return;
        MapData data = ((ItemMap) stack.getItem()).getMapData(stack, mc.world);
        NBTTagCompound structure = nbt.getCompoundTag("DDStructure");
        ItemExplorerMap.Type type = ItemExplorerMap.Type.get(structure.getByte("type"));
        int scale = 1 << data.scale;
        byte x = (byte) (((float) (structure.getInteger("x") - structure.getDouble("centerX")) / (float) scale) * 2f + 0.5f);
        byte z = (byte) (((float) (structure.getInteger("z") - structure.getDouble("centerZ")) / (float) scale) * 2f + 0.5f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x / 2f + 64, z / 2f + 64, -0.02f);
        GlStateManager.scale(4, 4, 3);
        GlStateManager.translate(-0.125, 0.125, 0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        mc.getTextureManager().bindTexture(type.getTexture());
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        float y = -0.001f * (1 + data.mapDecorations.size());
        bufferbuilder.pos(-1, 1, y).tex(0, 1).endVertex();
        bufferbuilder.pos(1, 1, y).tex(1, 1).endVertex();
        bufferbuilder.pos(1, -1,y).tex(1, 0).endVertex();
        bufferbuilder.pos(-1, -1, y).tex(0, 0).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
    }

}
