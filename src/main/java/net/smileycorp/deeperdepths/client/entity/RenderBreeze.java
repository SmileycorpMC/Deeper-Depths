package net.smileycorp.deeperdepths.client.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.client.entity.layers.LayerBoggedMoss;
import net.smileycorp.deeperdepths.client.entity.layers.LayerBreezeWind;
import net.smileycorp.deeperdepths.client.entity.model.ModelBreeze;
import net.smileycorp.deeperdepths.client.entity.model.ModelBreezeWind;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderBreeze extends RenderLiving<EntityBreeze> {

    private static final ResourceLocation BREEZE = new ResourceLocation(Constants.MODID + ":textures/entities/breeze/breeze.png");
    private final ModelBreezeWind layerModel = new ModelBreezeWind();

    public RenderBreeze(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, new ModelBreeze(), 0.8F);
        this.addLayer(new LayerBreezeWind(this, layerModel));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBreeze entity) {
        return BREEZE;
    }
}
