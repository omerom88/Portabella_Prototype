package ioio.examples.hello_service.GuitarActivity;

import android.content.Context;

import ioio.examples.hello_service.R;

/**
 * Created by Tomer on 27/07/2016.
 */
public class CordManager {
    private static CordManager cordManager;
    public final static int NUM_OF_ITERATIONS = 100;
    public static final int NUM_OF_MEITARS = 6;
    public static Cord[] cords = new Cord[NUM_OF_MEITARS];
//    public static int[] REG_NOTES = {0,0,0,0,0,0};
    private static float height;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    public static void init(Context context, int[] NOTES){
//        REG_NOTES[0] = R.raw.estringlow;
//        REG_NOTES[1] = R.raw.astring;
//        REG_NOTES[2] = R.raw.dstring;
//        REG_NOTES[3] = R.raw.gstring;
//        REG_NOTES[4] = R.raw.bstring;
//        REG_NOTES[5] = R.raw.estringhi;
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cords[i] = new Cord(i, context, NOTES[i], NUM_OF_ITERATIONS);
//            tasks[i] = new Task(i);
            cords[i].run();
            cords[i].pauseTask();
                    //{R.raw.estringlow, R.raw.astring, R.raw.dstring, R.raw.gstring,R.raw.bstring,
                    //    R.raw.estringhi};
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

    public static void setNewCords(int[] notesArray) {
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cords[i].setCord(notesArray[i]);
        }
    }
}
