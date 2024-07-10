package net.smileycorp.deeperdepths.animation.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class BasicModelPart extends ModelRenderer {
    public float defaultRotationX;
    public float defaultRotationY;
    public float defaultRotationZ;
    public float defaultOffsetX;
    public float defaultOffsetY;
    public float defaultOffsetZ;
    public float scaleX;
    public float scaleY;
    public float scaleZ;
    public float defaultPositionX;
    public float defaultPositionY;
    public float defaultPositionZ;
    private BasicModelEntity model;
    public BasicModelPart(ModelBase model)
    {
        this(model, (String)null);
        this.scaleX = 1.0F;
        this.scaleY = 1.0F;
        this.scaleZ = 1.0F;
    }


    public BasicModelPart(ModelBase model, String boxNameIn) {
        super(model, boxNameIn);
        this.scaleX = 1.0F;
        this.scaleY = 1.0F;
        this.scaleZ = 1.0F;

    }


    public void resetToDefaultPose() {
        this.rotateAngleX = this.defaultRotationX;
        this.rotateAngleY = this.defaultRotationY;
        this.rotateAngleZ = this.defaultRotationZ;
        this.rotationPointX = this.defaultPositionX;
        this.rotationPointY = this.defaultPositionY;
        this.rotationPointZ = this.defaultPositionZ;
    }


    public void updateDefaultPose() {
        this.defaultRotationX = this.rotateAngleX;
        this.defaultRotationY = this.rotateAngleY;
        this.defaultRotationZ = this.rotateAngleZ;
        this.defaultPositionX = this.rotationPointX;
        this.defaultPositionY = this.rotationPointY;
        this.defaultPositionZ = this.rotationPointZ;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        //GlStateManager.scale(scaleX, scaleY, scaleZ);
        GL11.glScalef(scaleX, scaleY, scaleZ);
    }

    public void setScaleWithPart(BasicModelPart part, float scaleX, float scaleY, float scaleZ) {
        part.setScale(scaleX, scaleY, scaleZ);
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    public void walk(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        this.rotateAngleX += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    public void flap(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        this.rotateAngleZ += this.calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    public void swing(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        this.rotateAngleY += this.calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
    }


    public void bob(float speed, float degree, boolean bounce, float f, float f1) {
        float movementScale = 1.0F;
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float)(Math.sin((double)(f * speed)) * (double)f1 * (double)degree - (double)(f1 * degree));
        if (bounce) {
            bob = (float)(-Math.abs(Math.sin((double)(f * speed)) * (double)f1 * (double)degree));
        }

        this.rotationPointY += bob;
    }


    public void transitionTo(BasicModelPart to, float timer, float maxTime) {
        this.rotateAngleX += (to.rotateAngleX - this.rotateAngleX) / maxTime * timer;
        this.rotateAngleY += (to.rotateAngleY - this.rotateAngleY) / maxTime * timer;
        this.rotateAngleZ += (to.rotateAngleZ - this.rotateAngleZ) / maxTime * timer;
        this.rotationPointX += (to.rotationPointX - this.rotationPointX) / maxTime * timer;
        this.rotationPointY += (to.rotationPointY - this.rotationPointY) / maxTime * timer;
        this.rotationPointZ += (to.rotationPointZ - this.rotationPointZ) / maxTime * timer;
        this.offsetX += (to.offsetX - this.offsetX) / maxTime * timer;
        this.offsetY += (to.offsetY - this.offsetY) / maxTime * timer;
        this.offsetZ += (to.offsetZ - this.offsetZ) / maxTime * timer;
    }



    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float movementScale = 1.0F;
        float rotation = (float) Math.cos(f * speed * movementScale + offset) * degree * movementScale * f1 + weight * f1;
        return invert ? -rotation : rotation;
    }
}
