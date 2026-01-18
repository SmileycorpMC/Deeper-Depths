package com.deeperdepths.mixin;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.items.ItemExplorerMap;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelMesher.class)
public abstract class MixinItemModelMesher {

    @Shadow @Final private ModelManager modelManager;

    @Inject(at=@At("HEAD"), method = "getItemModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/renderer/block/model/IBakedModel;", cancellable = true)
    private void deeperdepths$getItemModel(ItemStack stack, CallbackInfoReturnable<IBakedModel> callback) {
        if (!(stack.getItem() instanceof ItemMap)) return;
        ItemExplorerMap.Type type = ItemExplorerMap.getType(stack);
        if (type == null) return;
        callback.setReturnValue(modelManager.getModel(new ModelResourceLocation(Constants.locStr(ItemExplorerMap.Type.getName(type.ordinal())))));
    }

}
