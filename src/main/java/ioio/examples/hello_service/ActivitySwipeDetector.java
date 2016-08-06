package ioio.examples.hello_service;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tomer on 01/08/2016.
 */
public class ActivitySwipeDetector implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private static LinearLayout[] layouts = new LinearLayout[6];
    static final int MIN_DISTANCE = 10;
    private float downX, downY, upX, upY;
    private VelocityTracker mVelocityTracker = null;

    public ActivitySwipeDetector(LinearLayout[] layouts){
        ActivitySwipeDetector.layouts = layouts;
    }

    public void onRightSwipe(MotionEvent event){
        Log.i(logTag, "RightToLeftSwipe!");
        mVelocityTracker.computeCurrentVelocity(1000);
        float velocityX = mVelocityTracker.getXVelocity();
        int start = getRightString(getStringByPosition(downX));
        int end = getLeftString(getStringByPosition(upX));
//        Log.e("start", "" + start);
//        Log.e("end", "" + end);
        for (int i = start; i >= end; i--) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CordManager.restartTask(i, event.getPressure(), velocityX, event.getY());
        }
    }

    public void onLeftSwipe(MotionEvent event){
        Log.i(logTag, "LeftToRightSwipe!");
        mVelocityTracker.computeCurrentVelocity(1000);
        float velocityX = mVelocityTracker.getXVelocity();
        int start = getLeftString(getStringByPosition(downX));
        int end = getRightString(getStringByPosition(upX));
//        Log.e("start", "" + start);
//        Log.e("end", "" + end);
        for (int i = start; i <= end; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CordManager.restartTask(i, event.getPressure(), velocityX, event.getY());
        }
    }

    public void onDownSwipe(MotionEvent event){
        Log.i(logTag, "onTopToBottomSwipe!");
    }

    public void onUpSwipe(MotionEvent event){
        Log.i(logTag, "onBottomToTopSwipe!");
    }

    public boolean onTouch(View v, MotionEvent event) {
        int pointerId = event.getPointerId(event.getActionIndex());
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                if(mVelocityTracker == null) {
                // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                     mVelocityTracker.clear();
                }
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                mVelocityTracker.addMovement(event);
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if(Math.abs(deltaX) > MIN_DISTANCE){
                        // left or right
                        if(deltaX > 0) { this.onRightSwipe(event); return true; }
                        if(deltaX < 0) { this.onLeftSwipe(event); return true; }
                    }
                    else {
                        Log.i(logTag, "Horizontal Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                        return false; // We don't consume the event
                    }
                }
                // swipe vertical?
                else
                {
                    if(Math.abs(deltaY) > MIN_DISTANCE){
                        // top or down
                        if(deltaY < 0) { this.onDownSwipe(event); return true; }
                        if(deltaY > 0) { this.onUpSwipe(event); return true; }
                    }
                    else {
                        Log.i(logTag, "Vertical Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                        return false; // We don't consume the event
                    }
                }
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                return true;
            }

            case MotionEvent.ACTION_CANCEL: {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                return true;
            }
        }
        return false;
    }

    private static int getStringByPosition(float x) {
        for (int i = 0; i < layouts.length; i++) {
//            Log.e("x: ", "" + x);
//            Log.e("right: ", "" + layouts[i].getRight());
//            Log.e("left: ", "" + layouts[i].getLeft());
            if (x >= layouts[i].getLeft() && x <= layouts[i].getRight()) {
//                Log.e("i: ", "" + i);
                return i;
            }
        }
        return -1;
    }

    private static float getMiddleOfLayout(LinearLayout layout) {
        return (layout.getRight() + layout.getLeft()) / 2;
    }

    private int getRightString(int stringByPosition) {
        if (stringByPosition == -1) {
            stringByPosition = layouts.length - 1;
        } else if (stringByPosition < getMiddleOfLayout(layouts[stringByPosition])) {
            stringByPosition--;
        }
        return stringByPosition;
    }

    private int getLeftString(int stringByPosition) {
        if (stringByPosition == -1) {
            stringByPosition = 0;
        } else if (stringByPosition > getMiddleOfLayout(layouts[stringByPosition])) {
            stringByPosition++;
        }
        return stringByPosition;
    }
}