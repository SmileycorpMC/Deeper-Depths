package net.smileycorp.deeperdepths.animation.model;


import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.animation.EZAnimation;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.animation.util.Transform;

import java.util.HashMap;
import java.util.Iterator;

@SideOnly(Side.CLIENT)
public class EZModelAnimator {
    private int tempTick = 0;
    private int prevTempTick;
    private boolean correctAnimation = false;
    private IAnimatedEntity entity;
    private HashMap<ModelRenderer, Transform> transformMap = new HashMap();
    private HashMap<ModelRenderer, Transform> prevTransformMap = new HashMap();



    public EZModelAnimator() {
    }

    public static EZModelAnimator create() {
        return new EZModelAnimator();
    }

    public IAnimatedEntity getEntity() {
        return this.entity;
    }

    public void update(IAnimatedEntity entity) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
    }

    public boolean setAnimation(EZAnimation animation) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = this.entity.getAnimation() == animation;
        return this.correctAnimation;
    }

    public void startKeyframe(int duration) {
        if (this.correctAnimation) {
            this.prevTempTick = this.tempTick;
            this.tempTick += duration;
        }
    }

    public void setStaticKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe(true);
    }

    public void resetKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe();
    }

    public void rotate(ModelRenderer box, float x, float y, float z) {
        if (this.correctAnimation) {
            this.getTransform(box).addRotation(x, y, z);
        }
    }

    public void move(ModelRenderer box, float x, float y, float z) {
        if (this.correctAnimation) {
            this.getTransform(box).addOffset(x, y, z);
        }
    }

    private Transform getTransform(ModelRenderer box) {
        return (Transform)this.transformMap.computeIfAbsent(box, (b) -> {
            return new Transform();
        });
    }

    public void endKeyframe() {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary) {
        if (this.correctAnimation) {
            int animationTick = this.entity.getAnimationTick();
            if (animationTick >= this.prevTempTick && animationTick < this.tempTick) {
                ModelRenderer box;
                Transform transform;
                if (stationary) {
                    for(Iterator var3 = this.prevTransformMap.keySet().iterator(); var3.hasNext(); box.rotationPointZ += transform.getOffsetZ()) {
                        box = (ModelRenderer) var3.next();
                        transform = (Transform)this.prevTransformMap.get(box);
                        box.rotateAngleX += transform.getRotationX();
                        box.rotateAngleY += transform.getRotationY();
                        box.rotateAngleZ += transform.getRotationZ();
                        box.rotationPointX += transform.getOffsetX();
                        box.rotationPointY += transform.getOffsetY();
                    }
                } else {
                    float tick = ((float)(animationTick - this.prevTempTick) + Minecraft.getMinecraft().getLimitFramerate()) / (float)(this.tempTick - this.prevTempTick);
                    double interval = (double)tick * Math.PI / 2.0;
                    double inc = Math.sin(interval);
                    float dec = 1.0F - (float) inc;

                    Iterator var6;

                    for(var6 = this.prevTransformMap.keySet().iterator(); var6.hasNext(); box.rotationPointZ += dec * transform.getOffsetZ()) {
                        box = (ModelRenderer) var6.next();
                        transform = (Transform)this.prevTransformMap.get(box);
                        box.rotateAngleX += dec * transform.getRotationX();
                        box.rotateAngleY += dec * transform.getRotationY();
                        box.rotateAngleZ += dec * transform.getRotationZ();
                        box.rotationPointX += dec * transform.getOffsetX();
                        box.rotationPointY += dec * transform.getOffsetY();
                    }

                    for(var6 = this.transformMap.keySet().iterator(); var6.hasNext(); box.rotationPointZ += inc * transform.getOffsetZ()) {
                        box = (ModelRenderer) var6.next();
                        transform = (Transform)this.transformMap.get(box);
                        box.rotateAngleX += inc * transform.getRotationX();
                        box.rotateAngleY += inc * transform.getRotationY();
                        box.rotateAngleZ += inc * transform.getRotationZ();
                        box.rotationPointX += inc * transform.getOffsetX();
                        box.rotationPointY += inc * transform.getOffsetY();
                    }
                }
            }

            if (!stationary) {
                this.prevTransformMap.clear();
                this.prevTransformMap.putAll(this.transformMap);
                this.transformMap.clear();
            }

        }
    }
}
