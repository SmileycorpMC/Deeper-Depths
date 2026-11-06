package com.deeperdepths.client.entity;

import com.deeperdepths.client.entity.layers.LayerCopperGolemEyes;
import com.deeperdepths.client.entity.layers.LayerCopperGolemHeldItem;
import com.deeperdepths.client.entity.model.ModelCopperGolem;
import com.deeperdepths.common.Constants;
import com.deeperdepths.common.entities.EntityCopperGolem;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderCopperGolem extends RenderLiving<EntityCopperGolem>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID + ":textures/entities/copper_golem/normal.png");

    public RenderCopperGolem(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, new ModelCopperGolem(), 0.8F);
        this.addLayer(new LayerCopperGolemEyes(this));
        this.addLayer(new LayerCopperGolemHeldItem(this, 0.5F));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCopperGolem entity) { return TEXTURE; }
}
