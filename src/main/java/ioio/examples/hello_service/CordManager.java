package ioio.examples.hello_service;

import android.content.Context;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Tomer on 27/07/2016.
 */
public class CordManager {
    private static CordManager cordManager;
    private static Context context;
    final static int NUM_OF_ITERATIONS = 100;
    private static final int NUM_OF_MEITARS = 6;
    private static Cord[] cords = new Cord[NUM_OF_MEITARS];
    private static final int[] NOTES = {R.raw.e_string_low, R.raw.a_string, R.raw.d_string, R.raw.g_string,R.raw.b_string,
             R.raw.e_string_hi};
    GestureListener gl;
    GestureDetector gdt;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private CordManager(Context context){
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cords[i] = new Cord(context, NOTES[i], NUM_OF_ITERATIONS);
        }
        gl = new GestureListener();
        gdt = new GestureDetector(context, gl);
    }

    /* Static 'instance' method */
    public static CordManager getInstance(Context appContext) {
        if (cordManager == null) {
            context = appContext;
            cordManager = new CordManager(context);
        }
        return cordManager;
    }

    public void cancelTask(int index) {
        if (cords[index] != null) {
            cords[index].cancelTask();
        }
    }

    public void cancelAllTasks() {
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cancelTask(i);
        }
    }

    public boolean runTask(int index, MotionEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            event.setSource(index);
        }
        gdt.onTouchEvent(event);
        return true;
    }

    public Cord getTask(int index) {
        return cords[index];
    }
}
