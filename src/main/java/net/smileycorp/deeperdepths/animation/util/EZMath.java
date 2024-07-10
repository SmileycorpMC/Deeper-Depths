package net.smileycorp.deeperdepths.animation.util;

import net.smileycorp.deeperdepths.animation.EZAnimation;

public class EZMath {
    public static float cullAnimationTick(int tick, float amplitude, EZAnimation animation, float partialTick, int startOffset) {
        return cullAnimationTick(tick, amplitude, animation, partialTick, startOffset, animation.getDuration() - startOffset);
    }

    public static float cullAnimationTick(int tick, float amplitude, EZAnimation animation, float partialTick, int startOffset, int endAt) {
        float i = clamp(tick + partialTick - startOffset, 0, endAt);
        float f = (float) Math.sin((i / (float) (endAt)) * Math.PI) * amplitude;
        return EZMath.smin(f, 1.0F, 0.1F);
    }

    public static float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float) ((Math.cos(limbSwing * speed + offset) * degree * limbSwingAmount) * (inverse ? -1 : 1));
    }

    public static float smin(float a, float b, float k) {
        float h = Math.max(k - Math.abs(a - b), 0.0F) / k;
        return Math.min(a, b) - h * h * k * (1.0F / 4.0F);
    }


    public static float clamp(double value, double min, double max) {
        return (float) Math.max(min, Math.min(max, value));
    }
}
