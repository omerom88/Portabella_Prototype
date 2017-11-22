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
//public class swipeDetect implements View.OnTouchListener {
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
//                for (int i = 0; i < 6; i++){
//                    if (pointX > layoutLeftList[i] + 20 && pointX < layoutRightList[i] - 20){
//                        GuitarActivity.play[i] = true;
//                    }
//                }
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
//                for (int i=0; i < 6; i++)
//                {
//                    GuitarActivity.play[i] = true;
//                }
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
//
////////////////////////////////////////////////////////////    old       ///////////////////////////
//
////        if (downXmenu > layoutLeft + 20 && downXmenu < layoutright - 20) {
////            if (GuitarActivity.play[0]) {
////                GuitarActivity.play[0] = false;
////                GuitarActivity.mPlayer.play(GuitarActivity.soundStartId, vel, vel, 0, 0, (float) Math.pow(semiTone, 1));
////            }
////        }
////        else if (downXmenu > layoutLeft && downXmenu < layoutright) {
////            GuitarActivity.play[0] = true;
////        }
////
////        if (downXmenu > layout2Left + 20 && downXmenu < layout2right - 20) {
////            if (GuitarActivity.play[1]) {
////                GuitarActivity.play[1] = false;
////                GuitarActivity.mPlayer.play(GuitarActivity.soundStartId2, vel, vel, 0, 0, (float) Math.pow(semiTone, 2));
////            }
////        }
////        else if (downXmenu > layout2Left && downXmenu < layout2right) {
////            GuitarActivity.play[1] = true;
////        }
////        if (downXmenu > layout3Left + 20 && downXmenu < layout3right - 20) {
////            if (GuitarActivity.play[2]) {
////                GuitarActivity.play[2] = false;
////                GuitarActivity.mPlayer.play(GuitarActivity.soundStartId3, vel, vel, 0, 0, (float) Math.pow(semiTone, 3));
////            }
////        }
////        else if (downXmenu > layout3Left && downXmenu < layout3right) {
////            GuitarActivity.play[2] = true;
////        }
////        if (downXmenu > GuitarActivity.layout4.getLeft() + 20 && downXmenu < layout4right - 20) {
////            if (GuitarActivity.play[3]) {
////                GuitarActivity.play[3] = false;
////                GuitarActivity.mPlayer.play(GuitarActivity.soundStartId4, vel, vel, 0, 0, (float) Math.pow(semiTone, 4));
////            }
////        }
////        else if (downXmenu > GuitarActivity.layout4.getLeft() && downXmenu < layout4right) {
////            GuitarActivity.play[3] = true;
////        }
////        if (downXmenu > GuitarActivity.layout5.getLeft() + 20 && downXmenu < layout5right - 20) {
////            if (GuitarActivity.play[4]) {
////                GuitarActivity.play[4] = false;
////                GuitarActivity.mPlayer.play(GuitarActivity.soundStartId5, vel, vel, 0, 0, (float) Math.pow(semiTone, 5));
////            }
////        } else if (downXmenu > GuitarActivity.layout5.getLeft() && downXmenu < layout5right) {
////            GuitarActivity.play[4] = true;
////        }
////        if (downXmenu > GuitarActivity.layout6.getLeft() + 20 && downXmenu < layout6right - 20) {
////            if (GuitarActivity.play[5]) {
////                GuitarActivity.play[5] = false;
////                GuitarActivity.mPlayer.play(GuitarActivity.soundStartId6, vel, vel, 0, 0, (float) Math.pow(semiTone, 6));
////            }
////        } else if (downXmenu > GuitarActivity.layout6.getLeft() && downXmenu < layout6right) {
////            GuitarActivity.play[5] = true;
////        }
//
//
//
//
//
////        // if new pointer - put him im map
////        if (!pointerMap.containsKey(pointerId)) {
////            try {
////                pointX = event.getX(pointerId);
////                Queue<Float> pointXQueue =  new LinkedList<Float>();;
////                pointXQueue.add(pointX);
////                pointerMap.put(pointerId, pointXQueue);
//////                Log.d("@@@@ MAP ", pointerMap.toString());
////            } catch (IllegalArgumentException ex){}
////        }
////        else
////        {
//////            Log.d("@@@@ MAP ", pointerMap.toString());
////            Queue<Float> pointXQueueTemp = (Queue<Float>)pointerMap.get(pointerId);
////            try {
////                pointX = event.getX(pointerId);
////                pointXQueueTemp.add(pointX);
//////                pointerMap.put(pointerId, pointXQueueTemp);
//////                Log.d("~~~  Queue   ~~~~", pointXQueueTemp.toString());
////            } catch (IllegalArgumentException ex){}
//
//
//
////    public void checkPointerSamples(MotionEvent event)
////    {
////            try {
////                pointX = event.getX();
////                pointXQueueTemp.add(pointX);
////            }catch (IllegalArgumentException ex){}
////
////            // check if pointer got two samples - clean and check if sting in between
////            if (pointXQueueTemp.size() == 2)
////            {
//////                Log.d("%%   queue    %%",pointXQueueTemp.toString());
////                firstSamplePointerCheck = (float)pointXQueueTemp.poll();
////                secondeSamplePointerCheck = (float)pointXQueueTemp.poll();
//////                Log.d("%%   first    %%",firstSamplePointerCheck + "");
//////                Log.d("%%   seconde    %%",secondeSamplePointerCheck + "");
////                float vel = (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker,pointerId)) - 600) / 3400;
////                // means that you came from left
////                if (firstSamplePointerCheck < secondeSamplePointerCheck)
////                {
////                    if (secondeSamplePointerCheck - firstSamplePointerCheck >= 110) {
////                        // for loop on all meitarim
////                        for (int i = 0; i < 6; ++i) {
//////                        Log.d("~~~~layoutrighti~~~~", layoutRightList[i]+"");
////                            if (inRange(layoutRightList[i] - 20, firstSamplePointerCheck, secondeSamplePointerCheck)) {
////                                Log.d("~~~~   left i   ~~~~", i + "");
////                                if (GuitarActivity.play[i]) {
////                                    GuitarActivity.play[i] = false;
////                                    // check if velocity is enough to play
////                                    if (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId)) > 600) {
////                                        GuitarActivity.mPlayer.play(soundStartList[i], vel, vel, 0, 0, (float) Math.pow(semiTone, GuitarActivity.retSrigim[i]));
////                                    }
////                                }
////                            }
////                        }
////                    }
////                }
////                // means that you came from right
////                else
////                {
////                    if (firstSamplePointerCheck - secondeSamplePointerCheck >= 110) {
////                        // for loop on all meitarim
////                        for (int i = 0; i < 6; ++i) {
////                            if (inRange(layoutLeftList[i] + 20, secondeSamplePointerCheck, firstSamplePointerCheck)) {
////                                Log.d("~~~~   right i   ~~~~", i + "");
////                                if (GuitarActivity.play[i]) {
////                                    GuitarActivity.play[i] = false;
////                                    // check if velocity is enough to play
////                                    if (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId)) > 600) {
////                                        GuitarActivity.mPlayer.play(soundStartList[i], vel, vel, 0, 0, (float) Math.pow(semiTone, GuitarActivity.retSrigim[i]));
////                                    }
////                                }
////                            }
////                        }
////                    }
////                }
////            }
//////            // means pointer have only one sample - add the queue to the map
//////            else
//////            {
////////                Log.d("%%   pointx    %%",pointX + "");
//////                pointerMap.put(pointerId,pointXQueueTemp);
//////            }
////
////    }
//
//
////    public boolean inRange(float x, float leftBound, float rightBound)
////    {
////        if (x > leftBound && x < rightBound){
////            return true;
////        }
////        return false;
////    }