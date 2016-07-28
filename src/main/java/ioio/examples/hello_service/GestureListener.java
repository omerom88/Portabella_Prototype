package ioio.examples.hello_service;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Tomer on 27/07/2016.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    CordManager cordManager;

    private static final int VELOCITY_NORMALIZE_CONSTANT = 15000;
    private static final float MAX_PRESSURE = 1f;
    private static final float MIN_PRESSURE = 0.9f;
    private static float height;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("in", "onFling, index: " + e1.getSource());
        Log.e("in", "onFling, e1.getY(): " + e1.getY());
        Log.e("in", "onFling, e1.getPressure(): " + e1.getPressure());
        Log.e("in", "onFling, velocityX: " + Math.abs(velocityX / VELOCITY_NORMALIZE_CONSTANT));
        float pressure = MIN_PRESSURE + (MAX_PRESSURE - MIN_PRESSURE) * e1.getPressure();
        cordManager = CordManager.getInstance(null);
        cordManager.getTask(e1.getSource()).runTask(Math.abs(velocityX / VELOCITY_NORMALIZE_CONSTANT) * pressure, calcEqFreq(e1.getY()));
        return true;
    }

    public static void setHeight(float heightLayout) {
        height = heightLayout;
    }

    /**
     * calculating the wanted equalizer freq using the distance of the Y axis event from
     * the middle of the screen (reltive distance from the middle).
     * @param currY the Y axis of the touch event.
     * @return
     */
    private int calcEqFreq(float currY) {
//        Log.e("in", "onFling, e1.getY(): " + (int) (Cord.MAX_FREQ * Math.abs((height / 2) - currY) / height));
        return (int) (2 * Cord.MAX_FREQ * Math.abs((height / 2) - currY) / height);
    }
}
