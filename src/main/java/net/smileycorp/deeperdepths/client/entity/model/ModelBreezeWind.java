package net.smileycorp.deeperdepths.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.ibm.icu.text.Normalizer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.animation.model.BasicModelEntity;
import net.smileycorp.deeperdepths.animation.model.BasicModelPart;
import net.smileycorp.deeperdepths.animation.model.EZModelAnimator;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;

@SideOnly(Side.CLIENT)
public class ModelBreezeWind extends ModelBreeze
{
    /**
     * We're going to make this extend the animation core
     */

    public final BasicModelPart tornadoTopConnect;
    public final BasicModelPart tornadoMiddleConnect;
    public final BasicModelPart tornadoBottomConnect;

    public ModelBreezeWind()
    {
        textureWidth = 128;
        textureHeight = 128;


        tornadoTopConnect = new BasicModelPart(this);
        tornadoTopConnect.setRotationPoint(0.0F, 7.0F, 0.0F);
       this.tornadoTop.addChild(tornadoTopConnect);
        tornadoTopConnect.cubeList.add(new ModelBox(tornadoTopConnect, 0, 0, -8.0F, -4.0F, -8.0F, 16, 8, 16, 0.0F, false));
        tornadoTopConnect.cubeList.add(new ModelBox(tornadoTopConnect, 78, 6, -5.0F, -4.0F, -5.0F, 10, 8, 10, 0.0F, false));
        tornadoTopConnect.cubeList.add(new ModelBox(tornadoTopConnect, 48, 10, -3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F, false));

        tornadoMiddleConnect = new BasicModelPart(this);
        tornadoMiddleConnect.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.tornadoMiddle.addChild(tornadoMiddleConnect);
        tornadoMiddleConnect.cubeList.add(new ModelBox(tornadoMiddleConnect, 0, 24, -5.0F, 4.0F, -5.0F, 10, 6, 10, 0.0F, false));
       tornadoMiddleConnect.cubeList.add(new ModelBox(tornadoMiddleConnect, 19, 28, -3.0F, 4.0F, -3.0F, 6, 6, 6, 0.0F, false));

        tornadoBottomConnect = new BasicModelPart(this);
       this.tornadoBottom.addChild(tornadoBottomConnect);
        tornadoBottomConnect.setRotationPoint(0.0F, 7.0F, 0.0F);
        tornadoBottomConnect.cubeList.add(new ModelBox(tornadoBottomConnect, 0, 55, -3.0F, 10.0F, -3.0F, 6, 7, 6, 0.0F, false));

    }


    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
    }

    /** Do not do `super.render(...)`, or the wind texture will be overlaid atop the Head and Rod parts! */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        tornadoTopConnect.render(scale);
        tornadoMiddleConnect.render(scale);
        tornadoBottomConnect.render(scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    }


    /** Makes `ModelBreeze` handle all animations. */
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float spinMeRightRound = ageInTicks * (float)Math.PI * 0.15F;
        this.tornadoTop.rotationPointX = MathHelper.cos(spinMeRightRound) * 1.0F * 0.6F;
        this.tornadoTop.rotationPointZ = MathHelper.sin(spinMeRightRound) * 1.0F * 0.6F;

        this.tornadoMiddle.rotationPointX = MathHelper.sin(spinMeRightRound) * 0.8F * 0.5F;
        this.tornadoMiddle.rotationPointZ = MathHelper.cos(spinMeRightRound) * 0.8F * 0.5F;

        this.tornadoBottom.rotationPointX = MathHelper.cos(spinMeRightRound) * 0.6F * 0.4F;
        this.tornadoBottom.rotationPointZ = MathHelper.sin(spinMeRightRound) * 0.6F * 0.4F;
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

    }

    /** Used by Blockbench. */
    public void setRotationAngle(BasicModelPart modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}