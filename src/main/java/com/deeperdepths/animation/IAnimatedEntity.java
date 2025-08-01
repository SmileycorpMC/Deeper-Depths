package com.deeperdepths.animation;

public interface IAnimatedEntity {
    //Empty Model Container
    EZAnimation NO_ANIMATION = EZAnimation.create(0);


    //Return the current tick of the model
    int getAnimationTick();

    //Sets the current model tick to the value
    void setAnimationTick(int tick);

    //Get the current Animation being played
    EZAnimation getAnimation();

    //Set the current Animation
    void setAnimation(EZAnimation animation);


    //Return an array of the setup Animations
    EZAnimation[] getAnimations();
}
