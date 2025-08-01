package com.deeperdepths.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWindCharge extends ModelBase
{
    private final ModelRenderer core;
    private final ModelRenderer wind1;
    private final ModelRenderer wind2;

    public ModelWindCharge()
    {
        textureWidth = 64;
        textureHeight = 32;

        core = new ModelRenderer(this);
        core.setRotationPoint(0.0F, 24.0F, 0.0F);
        core.cubeList.add(new ModelBox(core, 0, 0, -2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F, false));

        wind1 = new ModelRenderer(this);
        wind1.setRotationPoint(0.0F, 24.0F, 0.0F);
        wind1.cubeList.add(new ModelBox(wind1, 40, 8, -3.0F, -4.0F, -3.0F, 6, 4, 6, 0.0F, false));

        wind2 = new ModelRenderer(this);
        wind2.setRotationPoint(0.0F, 24.0F, 0.0F);
        wind2.cubeList.add(new ModelBox(wind2, 17, 18, -4.0F, -4.0F, -4.0F, 8, 4, 8, 0.0F, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        core.render(scale);
        wind1.render(scale);
        wind2.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float spinMeRightRound = ageInTicks * (float)Math.PI * 0.1F;
        this.core.rotateAngleY = spinMeRightRound;
        this.wind1.rotateAngleY = -spinMeRightRound;
        this.wind2.rotateAngleY = this.wind1.rotateAngleY;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}