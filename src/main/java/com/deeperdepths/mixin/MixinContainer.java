package com.deeperdepths.mixin;

import com.deeperdepths.common.DeeperDepthsStats;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.items.ItemCopperSlab;
import com.deeperdepths.integration.FutureMCIntegration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public abstract class MixinContainer {

    @Shadow public abstract Slot getSlot(int slotId);

    @Inject(at = @At(value = "HEAD"), method = "slotClick")
    public void deeperdepths$slotClick(int id, int dragType, ClickType type, EntityPlayer player, CallbackInfoReturnable<ItemStack> callback) {
        if (!Loader.isModLoaded("futuremc") || id != 1) return;
        if (!FutureMCIntegration.isStonecutter((Container)(Object)this)) return;
        if (type == ClickType.QUICK_CRAFT || type == ClickType.CLONE) return;
        ItemStack stack = getSlot(0).getStack();
        if (!(stack.getItem() instanceof ItemBlock)) return;
        if (((ItemBlock) stack.getItem()).getBlock() != DeeperDepthsBlocks.COPPER_BLOCK) return;
        ItemStack output = getSlot(1).getStack();
        int count = output.getCount();
        if (output.getItem() instanceof ItemCopperSlab) count /= 2;
        count -= 1;
        if (type == ClickType.QUICK_MOVE) count *= stack.getCount();
        if (count > 0) player.addStat(DeeperDepthsStats.DUPED_COPPER, count);
    }

}
