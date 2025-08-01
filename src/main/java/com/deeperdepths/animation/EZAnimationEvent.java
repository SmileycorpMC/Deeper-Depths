package com.deeperdepths.animation;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EZAnimationEvent<T extends Entity & IAnimatedEntity> extends Event {
    protected EZAnimation animation;
    private T entity;

    EZAnimationEvent(T entity, EZAnimation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity() {
        return this.entity;
    }

    public EZAnimation getAnimation() {
        return this.animation;
    }

    @Cancelable
    public static class Start<T extends Entity & IAnimatedEntity> extends EZAnimationEvent<T> {
        public Start(T entity, EZAnimation animation) {
            super(entity, animation);
        }

        public void setAnimation(EZAnimation animation) {
            this.animation = animation;
        }
    }

    public static class Tick<T extends Entity & IAnimatedEntity> extends EZAnimationEvent<T> {
        protected int tick;

        public Tick(T entity, EZAnimation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick() {
            return this.tick;
        }
    }

}
