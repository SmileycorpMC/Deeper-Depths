package net.smileycorp.deeperdepths.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.animation.model.BasicModelEntity;
import net.smileycorp.deeperdepths.animation.model.BasicModelPart;
import net.smileycorp.deeperdepths.animation.model.EZModelAnimator;
import net.smileycorp.deeperdepths.common.entities.EntityBogged;
import net.smileycorp.deeperdepths.common.entities.EntityBreeze;

@SideOnly(Side.CLIENT)
public class ModelBreeze  extends BasicModelEntity
{
    private final BasicModelPart head;
    private final BasicModelPart headlayer;
    private final BasicModelPart stick1;
    private final BasicModelPart stick2;
    private final BasicModelPart stick3;

    //The Model Animator
    private final EZModelAnimator animator;

    public ModelBreeze() {
        textureWidth = 64;
        textureHeight = 32;

        head = new BasicModelPart(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false));

        headlayer = new BasicModelPart(this);
        headlayer.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(headlayer);
        headlayer.cubeList.add(new ModelBox(headlayer, 32, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.2F, false));

        stick1 = new BasicModelPart(this);
        stick1.setRotationPoint(0.0F, 7.0F, -5.0F);
        setRotationAngle(stick1, 0.3927F, 0.0F, 0.0F);
        stick1.cubeList.add(new ModelBox(stick1, 0, 16, -1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        stick2 = new BasicModelPart(this);
        stick2.setRotationPoint(-4.0F, 7.0F, 4.0F);
        setRotationAngle(stick2, -2.7489F, 0.8727F, 3.1416F);
        stick2.cubeList.add(new ModelBox(stick2, 0, 16, -1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        stick3 = new BasicModelPart(this);
        stick3.setRotationPoint(4.0F, 7.0F, 4.0F);
        setRotationAngle(stick3, -2.7489F, -0.6981F, 3.1416F);
        stick3.cubeList.add(new ModelBox(stick3, 0, 16, -1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        //ALWAYS include this in the bottom, first statement sets this as the default pose
        //that way the animator knows what default is and after each animation will go back to it's original pose
        this.updateDefaultPose();

        this.animator = EZModelAnimator.create();
    }

    /** Important to put all parts that you want animated, I'd just put everything tbh */
    @Override
    public Iterable<BasicModelPart> getAllParts() {
        return ImmutableList.of(head, headlayer, stick1, stick2, stick3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        head.render(f5);
        stick1.render(f5);
        stick2.render(f5);
        stick3.render(f5);
    }

    //this is where you do set animations
    @Override
    public void animate(IAnimatedEntity entity) {
        //Always include
        animator.update(entity);


        //when wanting to declare an animation, start with this
        animator.setAnimation(EntityBreeze.ANIMATION_SHOOT);
        //Now the below code must equal to the duration set in the Entity class
        //EXAMPLE
        //when declaring a start keyfram put whatever you want to move within the given duration
        animator.startKeyframe(10);

        animator.rotate(head, 0, (float) Math.toRadians(-30), 0);
        animator.move(head, 0, -5, 0);
        //always declare an end key frame when you are done putting moving boxes
        animator.endKeyframe();
        // for static key frames, such as holding a pose for that time use the below method
        animator.setStaticKeyframe(10);
        //lastly always best to end the animation with a reset key frame
        //this just gives it time to snap back to original pose without it being instant
        animator.resetKeyframe(10);
        //our time equals 30 which is how lone the ANIMATION_SHOOT is in the entity file

    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        //theres several methods that can be used for living animations that'll rotate different degrees
        //this one is just for head movement or any other parts that want to be individual from the model
        //even if using this function and calling it in the animations it'll still work
        this.faceTarget(netHeadYaw, headPitch, 1, head);
    }

    public void setRotationAngle(BasicModelPart BasicModelPart, float x, float y, float z)
    {
        BasicModelPart.rotateAngleX = x;
        BasicModelPart.rotateAngleY = y;
        BasicModelPart.rotateAngleZ = z;
    }
}