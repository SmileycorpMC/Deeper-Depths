package net.smileycorp.deeperdepths.client.entity.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.animation.model.BasicModelPart;

@SideOnly(Side.CLIENT)
public class ModelBreezeWind extends ModelBreeze
{
    public ModelBreezeWind()
    {
        textureWidth = 128;
        textureHeight = 128;

        BasicModelPart tornado1 = new BasicModelPart(this);
        tornado1.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.tornadoTop.addChild(tornado1);
        tornado1.cubeList.add(new ModelBox(tornado1, 0, 0, -8.0F, -4.0F, -8.0F, 16, 8, 16, 0.0F, false));
        tornado1.cubeList.add(new ModelBox(tornado1, 78, 6, -5.0F, -4.0F, -5.0F, 10, 8, 10, 0.0F, false));
        tornado1.cubeList.add(new ModelBox(tornado1, 48, 10, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, false));

        BasicModelPart tornado2 = new BasicModelPart(this);
        tornado2.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.tornadoMiddle.addChild(tornado2);
        tornado2.cubeList.add(new ModelBox(tornado2, 0, 24, -5.0F, 4.0F, -5.0F, 10, 6, 10, 0.0F, false));
        tornado2.cubeList.add(new ModelBox(tornado2, 19, 28, -3.0F, 4.0F, -3.0F, 6, 6, 6, 0.0F, false));

        BasicModelPart tornado3 = new BasicModelPart(this);
        this.tornadoBottom.addChild(tornado3);
        tornado3.setRotationPoint(0.0F, 7.0F, 0.0F);
        tornado3.cubeList.add(new ModelBox(tornado3, 0, 55, -3.0F, 10.0F, -3.0F, 6, 7, 6, 0.0F, false));
    }

    /** Do not do `super.render(...)`, or the wind texture will be overlaid atop the Head and Rod parts! */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        tornadoTop.render(scale);
        tornadoMiddle.render(scale);
        tornadoBottom.render(scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    }

    /** Makes `ModelBreeze` handle all animations. */
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    { super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn); }

    /** Used by Blockbench. */
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}