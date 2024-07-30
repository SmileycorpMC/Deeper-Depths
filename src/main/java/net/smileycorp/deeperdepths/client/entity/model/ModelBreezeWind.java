package net.smileycorp.deeperdepths.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.animation.model.BasicModelPart;
import net.smileycorp.deeperdepths.animation.model.EZModelAnimator;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;

@SideOnly(Side.CLIENT)
public class ModelBreezeWind extends ModelBreeze
{

    public EZModelAnimator animator_2;

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

        this.animator_2 = EZModelAnimator.create();
        super.updateDefaultPose();

    }

    @Override
    public Iterable<BasicModelPart> getAllParts() {
        return ImmutableList.of(tornadoTop, tornadoMiddle, tornadoBottom);
    }


    @Override
    public void animate(IAnimatedEntity entity) {
        //Always include
        animator_2.update(entity);

        //when wanting to declare an animation, start with this
        animator_2.setAnimation(EntityBreeze.ANIMATION_SHOOT);
        //Now the below code must equal to the duration set in the Entity class
        //EXAMPLE
        //when declaring a start keyfram put whatever you want to move within the given duration
        animator_2.startKeyframe(20);

        //animator.rotate(head, (float) Math.toRadians(-30), (float) Math.toRadians(-30), 0);
        /** Animating Tornado Parts isn't working? Might be due to funky inheriting in `ModelBreezeWind`, but `setRotationAngles` still works with it! */
        animator_2.rotate(tornadoTop, (float) Math.toRadians(-30), 0, 0);
        //always declare an end key frame when you are done putting moving boxes
        animator_2.endKeyframe();
        // for static key frames, such as holding a pose for that time use the below method
        animator_2.setStaticKeyframe(5);
        //lastly always best to end the animation with a reset key frame
        //this just gives it time to snap back to original pose without it being instant
        animator_2.resetKeyframe(5);
        //our time equals 30 which is how lone the ANIMATION_SHOOT is in the entity file
        System.out.println("We're playing animations here! But it's not Rotating!");

        super.animate(entity);
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
    public void setRotationAngle(BasicModelPart modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}