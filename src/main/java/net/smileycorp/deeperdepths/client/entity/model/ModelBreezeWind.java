package net.smileycorp.deeperdepths.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBreezeWind extends ModelBreeze
{
    private final ModelRenderer tornado1;
    private final ModelRenderer tornado2;
    private final ModelRenderer tornado3;

    public ModelBreezeWind()
    {
        textureWidth = 128;
        textureHeight = 128;

        tornado1 = new ModelRenderer(this);
        tornado1.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.tornadoTop.addChild(tornado1);
        tornado1.cubeList.add(new ModelBox(tornado1, 0, 0, -8.0F, -4.0F, -8.0F, 16, 8, 16, 0.0F, false));
        tornado1.cubeList.add(new ModelBox(tornado1, 78, 6, -5.0F, -4.0F, -5.0F, 10, 8, 10, 0.0F, false));
        tornado1.cubeList.add(new ModelBox(tornado1, 48, 10, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, false));

        tornado2 = new ModelRenderer(this);
        tornado2.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.tornadoMiddle.addChild(tornado2);
        tornado2.cubeList.add(new ModelBox(tornado2, 0, 24, -5.0F, 4.0F, -5.0F, 10, 6, 10, 0.0F, false));
        tornado2.cubeList.add(new ModelBox(tornado2, 19, 28, -3.0F, 4.0F, -3.0F, 6, 6, 6, 0.0F, false));

        tornado3 = new ModelRenderer(this);
        this.tornadoBottom.addChild(tornado3);
        tornado3.setRotationPoint(0.0F, 7.0F, 0.0F);
        tornado3.cubeList.add(new ModelBox(tornado3, 0, 55, -3.0F, 10.0F, -3.0F, 6, 7, 6, 0.0F, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        tornado1.render(scale);
        tornado2.render(scale);
        tornado3.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        float wait = ageInTicks * (float)Math.PI * 0.1F;
        //this.head.rotationPointY = MathHelper.cos(((float)(ageInTicks/6) + ageInTicks) * 0.1F);


        this.tornadoTop.rotationPointY = MathHelper.cos(((float)(ageInTicks/6) + ageInTicks) * 0.1F);
        //theres several methods that can be used for living animations that'll rotate different degrees
        //this one is just for head movement or any other parts that want to be individual from the model
        //even if using this function and calling it in the animations it'll still work
        //this.faceTarget(netHeadYaw, headPitch, 1, head);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}