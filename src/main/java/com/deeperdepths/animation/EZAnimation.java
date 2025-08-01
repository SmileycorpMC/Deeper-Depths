package com.deeperdepths.animation;

public class EZAnimation {
    @Deprecated
    private int id;
    private int duration;

    private EZAnimation(int duration) {
        this.duration = duration;
    }

    @Deprecated
    public static EZAnimation create(int id, int duration) {
        EZAnimation animation = EZAnimation.create(duration);
        animation.id = id;
        return animation;
    }


    public static EZAnimation create(int duration) {
        return new EZAnimation(duration);
    }

    @Deprecated
    public int getID() {
        return this.id;
    }

    public int getDuration() {
        return this.duration;
    }
}
