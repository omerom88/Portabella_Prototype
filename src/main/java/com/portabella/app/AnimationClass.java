package com.portabella.app;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Tomer on 24/09/2016.
 */
public class AnimationClass extends AnimationDrawable {
    private boolean animationFlag = false;
    private boolean animationRunning = false;
    private AnimationDrawable animation;

    public AnimationClass(AnimationDrawable animation) {
        this.animation = animation;
    }

    public boolean isAnimationFlag() {
        return animationFlag;
    }

    public void setAnimationFlag(boolean animationFlag) {
        this.animationFlag = animationFlag;
    }

    public AnimationDrawable getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationDrawable animation) {
        this.animation = animation;
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }

    @Override
    public void start() {
        animation.start();
        animationRunning = true;
    }

    @Override
    public void stop() {
        animation.stop();
        animationRunning = false;
    }

    @Override
    public boolean selectDrawable(int idx) {
        return animation.selectDrawable(idx);
    }

    public void checkIfAnimationDone(){
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (animation.getCurrent() != animation.getFrame(animation.getNumberOfFrames() - 1)) {
                    Log.e("checkIfAnimationDone", "not done");
                    if (isAnimationRunning()) {
                        checkIfAnimationDone();
                    }
                } else {
                    Log.e("checkIfAnimationDone", "done");
                    animationFlag = true;
                }
            }
        }, timeBetweenChecks);
    }

    public void restartAnimation() {
        animation.selectDrawable(0);
        animation.stop();
    }
}
