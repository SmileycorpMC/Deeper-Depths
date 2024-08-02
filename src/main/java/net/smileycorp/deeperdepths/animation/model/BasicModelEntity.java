package net.smileycorp.deeperdepths.animation.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;

public abstract class BasicModelEntity extends ModelBase {
    private float movementScale = 1.0F;
    public abstract Iterable<BasicModelPart> getAllParts();

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {

    }


    public void chainSwing(BasicModelPart[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);

        for(int index = 0; index < boxes.length; ++index) {
            boxes[index].rotateAngleY += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }

    }

    public void chainWave(BasicModelPart[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);

        for(int index = 0; index < boxes.length; ++index) {
            boxes[index].rotateAngleX += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }

    }

    public void chainFlap(BasicModelPart[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);

        for(int index = 0; index < boxes.length; ++index) {
            boxes[index].rotateAngleZ += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }

    }


    public void walk(BasicModelPart box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        box.walk(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    public void flap(BasicModelPart box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        box.flap(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    public void swing(BasicModelPart box, float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        box.swing(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    public void bob(BasicModelPart box, float speed, float degree, boolean bounce, float f, float f1) {
        box.bob(speed, degree, bounce, f, f1);
    }

    public float moveBox(float speed, float degree, boolean bounce, float f, float f1) {
        return bounce ?(float) -Math.abs(Math.sin(f * speed) * f1 * degree) :  (float) Math.sin(f * speed) * f1 * degree - f1 * degree;
    }

    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
        return (float) Math.cos(swing * speed * this.movementScale + offset * (float)boxIndex) * swingAmount * degree * this.movementScale;
    }

    private float calculateChainOffset(double rootOffset, BasicModelPart... boxes) {
        return (float)(rootOffset * Math.PI / (double)(2 * boxes.length));
    }

    public float getMovementScale() {
        return this.movementScale;
    }

    public void setMovementScale(float movementScale) {
        this.movementScale = movementScale;
    }

    public void resetDefaultpose() {
        this.getAllParts().forEach((BasicModelPart::resetToDefaultPose));
    }

    public void updateDefaultPose() {
        this.getAllParts().forEach((BasicModelPart::updateDefaultPose));
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

    }

    public void animate(IAnimatedEntity entity) {

    }


    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);


        if(entitylivingbaseIn instanceof IAnimatedEntity) {
            this.resetDefaultpose();
            this.animate((IAnimatedEntity) entitylivingbaseIn);
        }

    }

    public void faceTarget(float yaw, float pitch, float rotationDivisor, BasicModelPart... boxes) {
        float actualRotationDivisor = rotationDivisor * (float)boxes.length;
        float yawAmount = yaw / 57.295776F / actualRotationDivisor;
        float pitchAmount = pitch / 57.295776F / actualRotationDivisor;
        BasicModelPart[] var8 = boxes;
        int var9 = boxes.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            BasicModelPart box = var8[var10];
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }

    }



    public void progressRotation(BasicModelPart model, float progress, float rotX, float rotY, float rotZ, float divisor) {
        model.rotateAngleX += progress * (rotX - model.defaultRotationX) / divisor;
        model.rotateAngleY += progress * (rotY - model.defaultRotationY) / divisor;
        model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / divisor;
    }

    public void progressRotationPrev(BasicModelPart model, float progress, float rotX, float rotY, float rotZ, float divisor) {
        model.rotateAngleX += progress * rotX / divisor;
        model.rotateAngleY += progress * rotY / divisor;
        model.rotateAngleZ += progress * rotZ / divisor;
    }

    public void progressPosition(BasicModelPart model, float progress, float x, float y, float z, float divisor) {
        model.rotationPointX += progress * (x - model.defaultPositionX) / divisor;
        model.rotationPointY += progress * (y - model.defaultPositionY) / divisor;
        model.rotationPointZ += progress * (z - model.defaultPositionZ) / divisor;
    }

    public void progressPositionPrev(BasicModelPart model, float progress, float x, float y, float z, float divisor) {
        model.rotationPointX += progress * x / divisor;
        model.rotationPointY += progress * y / divisor;
        model.rotationPointZ += progress * z / divisor;
    }
}
