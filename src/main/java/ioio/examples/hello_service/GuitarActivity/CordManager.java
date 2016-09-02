package ioio.examples.hello_service.GuitarActivity;

import android.content.Context;
import android.util.Log;

import ioio.examples.hello_service.R;

/**
 * Created by Tomer on 27/07/2016.
 */
public class CordManager {
    private static CordManager cordManager;
    final static int NUM_OF_ITERATIONS = 100;
    public static final int NUM_OF_MEITARS = 6;
    public static Cord[] cords = new Cord[NUM_OF_MEITARS];
    private static final int[] NOTES = {R.raw.e_string_low, R.raw.a_string, R.raw.d_string, R.raw.g_string,R.raw.b_string,
            R.raw.e_string_hi};
    private static float height;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    public static void init(Context context){
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cords[i] = new Cord(i, context, NOTES[i], NUM_OF_ITERATIONS);
//            tasks[i] = new Task(i);
            cords[i].run();
            cords[i].pauseTask();
        }
    }

    public static float getHeight() {
        return height;
    }

    public static void cancelAllTasks() {
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cancelTask(i);
        }
    }

    public static Cord getCord(int index) {
        return cords[index];
    }

    public static void restartTask(int index, float pressure, float velocityX, float yPos) {
        cords[index].pauseTask();
        cords[index].resume(pressure, velocityX, yPos);
    }

    public static void cancelTask(int index) {
        if (cords[index] != null) {
            cords[index].pauseTask();
        }
    }

    public static void setHeight(float heightLayout) {
        height = heightLayout;
    }
}
