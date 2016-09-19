package ioio.examples.hello_service;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import ioio.examples.hello_service.GuitarActivity.GuitarActivity;

/**
 * Created by omerrom on 18/09/16.
 */
public class LoadingActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final ImageView mImageViewMoving = (ImageView) findViewById(R.id.imageview_animated_moving);
        mImageViewMoving.setVisibility(View.INVISIBLE);
        final ImageView mImageViewOpening = (ImageView) findViewById(R.id.imageview_animated_opening);
        AnimationDrawable animationDrawableOpen = (AnimationDrawable)mImageViewOpening.getBackground();
        AnimationDrawable animationDrawableMove = (AnimationDrawable)mImageViewMoving.getBackground();
        animationDrawableOpen.start();
        checkIfAnimationOpenDone(animationDrawableOpen, animationDrawableMove, mImageViewMoving);

    }

    private void checkIfAnimationOpenDone(AnimationDrawable anim, final AnimationDrawable animationDrawableMove, final ImageView mImageViewMoving){
        final AnimationDrawable a = anim;
//        final AnimationDrawable b = animationDrawableMove;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationOpenDone(a, animationDrawableMove, mImageViewMoving);
                } else {
//                    Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
                    mImageViewMoving.setVisibility(View.VISIBLE);
                    animationDrawableMove.start();
                    checkIfAnimationMoveDone(animationDrawableMove);
                }
            }
        }, timeBetweenChecks);
    }
//    private void getOutOfit(){
//        while (loadingMoveCounter)
//    }

    private void checkIfAnimationMoveDone(AnimationDrawable anim){

        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationMoveDone(a);
                } else {
//                    Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoadingActivity.this, GuitarActivity.class);
                    startActivity(intent);
                }
            }
        }, timeBetweenChecks);
    }

}
