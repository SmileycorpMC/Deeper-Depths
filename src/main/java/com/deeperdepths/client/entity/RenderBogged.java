package com.deeperdepths.client.entity;

import com.deeperdepths.client.entity.layers.LayerBoggedMoss;
import com.deeperdepths.client.entity.layers.LayerBoggedMushrooms;
import com.deeperdepths.client.entity.model.ModelBoggedMushrooms;
import com.deeperdepths.common.Constants;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBogged extends RenderSkeleton
{
    private static final ResourceLocation BOGGED_SKELETON_TEXTURES = new ResourceLocation(Constants.MODID + ":textures/entities/bogged/bogged.png");
    private final ModelBoggedMushrooms modelBoggedMushrooms = new ModelBoggedMushrooms();

    public RenderBogged(RenderManager p_i47191_1_)
    {
        super(p_i47191_1_);
        this.addLayer(new LayerBoggedMoss(this));
        this.addLayer(new LayerBoggedMushrooms(this, modelBoggedMushrooms));
    }

    protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    { return BOGGED_SKELETON_TEXTURES; }
}