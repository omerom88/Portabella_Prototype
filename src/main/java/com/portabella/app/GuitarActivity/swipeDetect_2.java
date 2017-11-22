package com.portabella.app.GuitarActivity;

import android.os.Handler;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerrom on 21/01/17.
 */
public class swipeDetect_2 implements View.OnTouchListener {
    public static float semiTone = 1.0594631f;
    public static int meitarPlayId;
    public static SparseIntArray meitarPlayMap = new SparseIntArray();
    private VelocityTracker mVelocityTracker = null;
    private Map<Integer, Float> lastPointX = new HashMap<>();
    private long[] startSoundTime = {0, 0, 0, 0, 0, 0};
    private boolean[] crossedMeitar = {false,false,false,false,false,false};
    public static int[] layoutLeftList = new int[6];
    private int touchCounter = 0;

    /**
     * Function to check if finger cross meitar, if meitar isn't play - play and add to meitarPlayMap
     * if meitar is playing - stop first and play again
     * @param pId - pointer of finger
     * @param newPointX - new finger location x coordinate
     * @param major - the radios of the finger touch on screen
     */
    private void checkIfCrossPlay(final int pId, final float newPointX, final float major) {
        if (lastPointX.containsKey(pId)) {
            //** loop on all meitarim
            for (int meitar = 0; meitar < 6; meitar++) {
                if ((layoutLeftList[meitar] < (newPointX + (major / 2)) && layoutLeftList[meitar] > (lastPointX.get(pId) - (major / 2))) ||
                        (layoutLeftList[meitar] < (lastPointX.get(pId) + (major / 2)) && layoutLeftList[meitar] > (newPointX - (major / 2)))) {
                    crossedMeitar[meitar] = true;
                } else {
                    if (crossedMeitar[meitar]) {
                        playSound(pId, meitar);
                    }
                }
            }
        }
    }

    /**
     * find a meitar that crossed and need to be played
     * @param pId - pointer of finger
     */
    private void findCrossedMeitar(final int pId)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //** case it come from pointer up - don't know meitar need to search the right meitar
                for (int meitar = 0; meitar < 6; meitar++) {
                    if (crossedMeitar[meitar]){
//                        Log.e("HERERERE","!");
                        playSound(pId, meitar);
                    }
                }
            }
        }).start();
    }

    /**
     * Function to play sound for a meitar that crossed
     * @param pId - pointer of finger
     * @param meitar - the meitar the crossed
     */
    private void playSound(final int pId, int meitar){

        //** if meitar is alrady playing
        if (meitarPlayMap.indexOfKey(meitar) >= 0) {
            stopSound(meitarPlayMap.get(meitar), meitar);
        }

        //** calc the velocity of the touch
        float vel = (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker, pId) - 600)) / 4600; //600, 3400
//        Log.e("VEL", vel + "");
        //** check the briage matrix for blocking meitar
        int[] accord = checkSarig(meitar);
        if (accord[1] == 2 || accord[1] == 0) {
            //** save the time meitar start playing to see if its finished playing
            startSoundTime[meitar] = System.currentTimeMillis();
            meitarPlayId = GuitarActivity.mPlayer.play(GuitarActivity.soundStartIdList[meitar], vel, vel, 0, 0, (float) Math.pow(semiTone, accord[0]));
        }
        else //** (accord[1] == 1)
        {
            //TODO: check how much milisecond till sound are really play - soundpool.play latency
            //** save the time meitar start playing to see if its finished playing
            startSoundTime[meitar] = System.currentTimeMillis();
            meitarPlayId = GuitarActivity.mPlayer.play(GuitarActivity.soundStartIdList[meitar], vel, vel, 0, 0, (float) Math.pow(semiTone, accord[0]));
            final int finalMeitar = meitar;
            //** case of 1 - stop after 50 milisecond
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopSound(meitarPlayId, finalMeitar);
                }
            }, 50);
        }
        //** add meitar to map
        meitarPlayMap.put(meitar, meitarPlayId);
        GuitarActivity.writeDataToAnalyze("Meitar_" + meitar + "" + " Vel_" + vel + "" + " Accord_" + accord[0] + "" + " Action_" + accord[1] + "");
        crossedMeitar[meitar] = false;

    }

    /**
     * Function to stop meitar in steps (and not from 100 to 0)
     * @param meitarId - meitar id from soundpool
     * @param meitar - meitar that need to stop
     */
    private void stopSound(final int meitarId, final int meitar)
    {
        try {
            Handler handler1 = new Handler();
            final boolean[] finish = {false};
            for (int i = 1; i < 11; i++) {
                final int finalI = i;
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        float vel = (float) (1f - (finalI * 0.1));
                        GuitarActivity.mPlayer.setVolume(meitarId, vel, vel);
                        if (finalI == 10)
                        {
                            finish[0] = true;
                        }
                    }
                }, 10 * i);
            }
            if (finish[0]){
                GuitarActivity.mPlayer.stop(meitarId);
                meitarPlayMap.delete(meitar);
            }
        } catch (Exception e){};



    }


    /**
     * Function to check every round if meitar finished playing - if yes delete it from map
     */
    public void clearIfNotPlaying(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < meitarPlayMap.size(); i++) {
                    int key = meitarPlayMap.keyAt(i);
                    //means the meitar finished playing - need to be removed
                    if (System.currentTimeMillis() > GuitarActivity.soundDurationList[key] + startSoundTime[key]) {
                        meitarPlayMap.delete(key);
                    }
                }
            }
        }).start();
    }


    /**
     * Function to find meitar blooking on bridge
     * @param meitar - meitar
     * @return Array of (sarig, bloock (half/full))
     */
    public int[] checkSarig(int meitar) {
        int[] ret = {0,0};
        synchronized (GuitarActivity.cellsMatrix) {
            for (int sarig = 3; sarig >= 0; sarig--) {
                if (GuitarActivity.cellsMatrix[5 - meitar][sarig] == 2) {
                    ret[0] = sarig + 1;
                    ret[1] = 2; // full block
                    return ret;
                }
                if (GuitarActivity.cellsMatrix[5 - meitar][sarig] == 1){
                    ret[0] = sarig + 1;
                    ret[1] = 1; // half block
                    return ret;
                }
            }
        }
        return ret;
    }


    /**
     * Set array of layouts x coordinate - happend just once
     */
    public void setLayoutLeft(){
        layoutLeftList[0] = GuitarActivity.layout.getLeft();
        layoutLeftList[1] = GuitarActivity.layout2.getLeft();
        layoutLeftList[2] = GuitarActivity.layout3.getLeft();
        layoutLeftList[3] = GuitarActivity.layout4.getLeft();
        layoutLeftList[4] = GuitarActivity.layout5.getLeft();
        layoutLeftList[5] = GuitarActivity.layout6.getLeft();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        touchCounter++;
        if (touchCounter == 1){
            setLayoutLeft();
        }
        //** check if meitr finished his playing and delete it from map
        clearIfNotPlaying();

        //** take touch vars
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        float newPointX = event.getX(pointerIndex);
        final int action = event.getAction();

        //** check touch actions
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                //** init the velocity tracker
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
//                checkIfpressedStop(newPointX, event.getToolMajor(pointerIndex) + 5f);
                //** put first x coordinate in list
                lastPointX.put(pointerId, newPointX);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                //** add movment to velocity tracker
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000, 4000);
//                if (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker,pointerId)) > 400) {
                    checkIfCrossPlay(pointerId, newPointX, event.getToolMajor(pointerIndex)); // + 5f
//                }
//                else{
//                    checkIfCrossStop(pointerId, newPointX, event.getToolMajor(pointerIndex) + 5f);
//                }
//                checkIfpressedStop(newPointX, event.getToolMajor(pointerIndex) + 5f);

                //** put second x coordinate in list to compare to first and see if cross meitar
                lastPointX.put(pointerId, newPointX);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000, 4000);
                //** loop on all pointers
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    pointerId = event.getPointerId(i);
                    pointerIndex = event.findPointerIndex(pointerId);
                    newPointX = event.getX(pointerIndex);
                    if (pointerId == event.getPointerId(i)) {
//                        if (Math.abs(VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId)) > 400) {
                            checkIfCrossPlay(pointerId, newPointX, event.getToolMajor(pointerIndex));// + 5f
//                        }
//                        else{
//                            checkIfCrossStop(pointerId, newPointX, event.getToolMajor(pointerIndex) + 5f);
//                        }
                        lastPointX.put(pointerId, newPointX);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                //reset the last x to be 0
                findCrossedMeitar(pointerId);
                lastPointX.remove(pointerId);
            }
        }
        return true;
    }
}
