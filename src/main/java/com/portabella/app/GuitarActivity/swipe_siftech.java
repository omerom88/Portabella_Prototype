//package com.portabella.app.GuitarActivity;
//
//import android.support.v4.view.VelocityTrackerCompat;
//import android.view.MotionEvent;
//import android.view.VelocityTracker;
//import android.view.View;
//
///**
// * Created by omerrom on 21/01/17.
// */
//public class swipe_siftech implements View.OnTouchListener {
//    private float downXmenu;
//    private int pointerId;
//    private float pointX;
//    private int layoutLeft, layoutright, layout2Left, layout2right, layout3Left, layout3right, layout4Left, layout4right, layout5Left, layout5right, layout6Left, layout6right;
//    public static float semiTone = 1.0594631f;
//    private VelocityTracker mVelocityTracker = null;
//    private int[] layoutLeftList = new int[6];
//    private int[] layoutRightList = new int[6];
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        pointerId = event.getPointerId(event.getActionIndex());
////        Log.d("action",event.getActionMasked()+"");
//
//        layoutLeft = GuitarActivity.layout.getLeft();
//        layoutLeftList[0] = layoutLeft;
//        layout2Left = GuitarActivity.layout2.getLeft();
//        layoutLeftList[1] = layout2Left;
//        layout3Left = GuitarActivity.layout3.getLeft();
//        layoutLeftList[2] = layout3Left;
//        layout4Left = GuitarActivity.layout4.getLeft();
//        layoutLeftList[3] = layout4Left;
//        layout5Left = GuitarActivity.layout5.getLeft();
//        layoutLeftList[4] = layout5Left;
//        layout6Left = GuitarActivity.layout6.getLeft();
//        layoutLeftList[5] = layout6Left;
//        layoutright = GuitarActivity.layout.getRight();
//        layoutRightList[0] = layoutright;
//        layout2right = GuitarActivity.layout2.getRight();
//        layoutRightList[1] = layout2right;
//        layout3right = GuitarActivity.layout3.getRight();
//        layoutRightList[2] = layout3right;
//        layout4right = GuitarActivity.layout4.getRight();
//        layoutRightList[3] = layout4right;
//        layout5right = GuitarActivity.layout5.getRight();
//        layoutRightList[4] = layout5right;
//        layout6right = GuitarActivity.layout6.getRight();
//        layoutRightList[5] = layout6right;
//
//
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_POINTER_UP:
//                try {
//                    pointX = event.getX(pointerId);
//
//                }
//                catch (IllegalArgumentException ex){}
//
//
//                return true;
//            case MotionEvent.ACTION_DOWN:
//                if (mVelocityTracker == null) {
//                    mVelocityTracker = VelocityTracker.obtain();
//                } else {
//                    mVelocityTracker.clear();
//                }
//                mVelocityTracker.addMovement(event);
//                break;
////                downXmenu = event.getX();
////                play(event);
//
//            case MotionEvent.ACTION_MOVE:
//                mVelocityTracker.addMovement(event);
//                mVelocityTracker.computeCurrentVelocity(1000, 4000);
////                checkPointerSamples(event);
//                downXmenu = event.getX();
//                if (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId)) > 600) {
//                    play(event);
//                }
//                return true;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                downXmenu = event.getX(event.getPointerCount()-1);
//                mVelocityTracker.addMovement(event);
//                mVelocityTracker.computeCurrentVelocity(1000, 4000);
//                downXmenu = event.getX();
//                if (VelocityTrackerCompat.getXVelocity(mVelocityTracker,pointerId) > 600) {
//                    play(event);
//                }
//                return true;
//
//            case MotionEvent.ACTION_UP:
//                return true;
//            case MotionEvent.ACTION_CANCEL:
//                mVelocityTracker.recycle();
//                break;
//        }
//        return true;
//    }
//
//
//    public int checkSarig(int i){
//        synchronized (GuitarActivity.cellsMatrix) {
//            for (int j = 3; j >= 0; j--) {
//                if (GuitarActivity.cellsMatrix[5 - i][j]) {
//                    return j + 1;
//                }
//            }
//        }
//        return 0;
//    }
//
//
//    public void play(MotionEvent event) {
//        float vel = (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker,pointerId)) - 600) / 3400;
//
//        for (int i = 0; i < 6; i++){
//            if (downXmenu > layoutLeftList[i] + 20 && downXmenu < layoutRightList[i] - 20) {
//                if (GuitarActivity.play[i]) {
//                    GuitarActivity.play[i] = false;
//                    int acorde = checkSarig(i);
//                    GuitarActivity.mPlayer.play(GuitarActivity.soundStartIdList[i], vel, vel, 0, 0, (float) Math.pow(semiTone, acorde));
//                }
//            }
//            else if (downXmenu > layoutLeftList[i] && downXmenu < layoutRightList[i]) {
//                GuitarActivity.play[i] = true;
//            }
//        }
//    }
//}
