package com.portabella.app;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by omerrom on 18/09/16.
 */
public class LoadingActivity extends Activity
{
    public AnimationDrawable animationDrawableOpen;
    public AnimationDrawable animationDrawableMove;
    public ImageView mImageViewMoving;

    private static final int MOVE_LOOP_NUM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.lib.app.R.layout.activity_loading);

        mImageViewMoving = (ImageView) findViewById(com.lib.app.R.id.imageview_animated_moving);
        mImageViewMoving.setVisibility(View.INVISIBLE);
        final ImageView mImageViewOpening = (ImageView) findViewById(com.lib.app.R.id.imageview_animated_opening);
        animationDrawableOpen = (AnimationDrawable)mImageViewOpening.getBackground();
        animationDrawableMove = (AnimationDrawable)mImageViewMoving.getBackground();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        animationDrawableOpen.start();
        checkIfAnimationOpenDone(animationDrawableOpen, this);
    }

    private void checkIfAnimationOpenDone(AnimationDrawable anim, final Activity activity){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationOpenDone(a, activity);
                } else {
                    mImageViewMoving.setVisibility(View.VISIBLE);
                    animationDrawableMove.start();
                    checkIfAnimationMoveDone(animationDrawableMove, MOVE_LOOP_NUM, activity);
                }
            }
        }, timeBetweenChecks);
    }
//    private void getOutOfit(){
//        while (loadingMoveCounter)
//    }

    private void checkIfAnimationMoveDone(AnimationDrawable anim, final int loopNum, final Activity activity){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationMoveDone(a, loopNum, activity);
                } else {
                    Log.d("else: ", "loopNum" );
                    int tempLoopNum = loopNum;
                    tempLoopNum--;
                    if (tempLoopNum == 0) {
//                    Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoadingActivity.this, GuitarActivity.class);
//                        startActivity(intent);
                        activity.setResult(MainActivity.RESULT_OK);
                        Log.d("finish: ", "finish123456" );
                        activity.finish();
                    } else {
                        a.selectDrawable(0);
                        a.stop();
                        a.start();
                        checkIfAnimationMoveDone(a, tempLoopNum, activity);
                    }
                }
            }
        }, timeBetweenChecks);
    }
}
