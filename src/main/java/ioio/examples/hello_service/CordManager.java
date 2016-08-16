package ioio.examples.hello_service;

import android.content.Context;
import android.media.AudioTrack;

/**
 * Created by Tomer on 27/07/2016.
 */
public class CordManager {
    private static CordManager cordManager;
    final static int NUM_OF_ITERATIONS = 1000;
    private static final int NUM_OF_MEITARS = 6;
    private static Cord[] cords = new Cord[NUM_OF_MEITARS];
    private static final int[] NOTES = {R.raw.e_string_low, R.raw.a_string, R.raw.d_string, R.raw.g_string,R.raw.b_string,
             R.raw.e_string_hi};
    private static Task[] task = new Task[6];
    private static float height;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private CordManager(Context context){
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cords[i] = new Cord(context, NOTES[i], NUM_OF_ITERATIONS);
            task[i] = new Task(i);
            task[i].start();
        }
    }

    /* Static 'instance' method */
    public static CordManager getInstance(Context appContext) {
        if (cordManager == null) {
            cordManager = new CordManager(appContext);
        }
        return cordManager;
    }

    public void cancelAllTasks() {
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cancelTask(i);
        }
    }

    public Cord getCord(int index) {
        return cords[index];
    }

    public static void restartTask(int index, float pressure, float velocityX, float yPos) {
        cancelTask(index);
//        task[index] = new Task(index, pressure, velocityX, yPos);
//        task[index].start();
        task[index].setAndStart(pressure, velocityX, yPos);
    }

    private static void cancelTask(int index) {
        if (task[index] != null) {
            try {
                task[index].cancelTask(index);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setHeight(float heightLayout) {
        height = heightLayout;
    }

    public static class Task extends Thread {
        private Cord cord;
        private float pressure = 0f;
        private float velocityX;
        private float yPos;
        private volatile boolean running = true;
        private volatile boolean new_task = false;

        private static final float MIN_VELOCITY = 0;
        private static final int VELOCITY_NORMALIZE_CONSTANT = 15000;
        private static final float MAX_PRESSURE = 1f;
        private static final float MIN_PRESSURE = 0.9f;

        private int curIndex;

        public Task(int index) {
            this.cord = cords[index];
            this.curIndex = index;
            running = true;
        }

        public Task(int index, float pressure, float velocityX, float yPos) {
            this.cord = cords[index];
            this.pressure = pressure;
            this.velocityX = velocityX;
            this.yPos = yPos;
        }

        @Override
        public void run() {
            while (true) {
                if (running) {
                    new_task = false;
                    cord.stopTrack();
                    int currIndex = 0;
//                    Log.e("currIndex", "" + currIndex);
                    if (setProperties()) {
                        cord.initEqualizer(cord.getEqFreq());
                        float currVolume = cord.getStartVolume();
    //                Log.e("currRate", "" + currRate);
    //                Log.e("music", "" + revSample.length);
//                        Log.e("retPresure", "" + MainActivity.retPresure);
    //                Log.e("retleftX", "" + (int)MainActivity.retleftX);
//                        Log.e("new_task", "" + new_task);
                        for (int i = 0; i < cord.getPartOne(); i++) {
                            if (!running || new_task) {
//                                Log.e("in", "break");
//                                Log.e("in", "break" + running);
                                break;
                            }
                            cord.playIteration(currIndex,this.curIndex);
                            currIndex += cord.getBufferAddPerIteration();

                            currVolume = cord.calcVolume(currVolume, MainActivity.retMeitar[this.curIndex], false);
                        }
                    }
//                    Log.e("in", "END OF TASK");
                    if (!new_task) {
                        running = false;
                    }
                }
            }
        }

        public void setAndStart(float pressure, float velocityX, float yPos) {
            this.pressure = pressure;
            this.velocityX = velocityX;
            this.yPos = yPos;
            this.running = true;
            this.new_task = true;
        }

        private boolean setProperties() {
            float normVelocity = Math.abs(velocityX / VELOCITY_NORMALIZE_CONSTANT);
            if (normVelocity > MIN_VELOCITY) {
//                Log.e("in", "normVelocity > MIN_VELOCITY");
                float normPressure = MIN_PRESSURE + (MAX_PRESSURE - MIN_PRESSURE) * pressure;
                cord.setProperties(Math.abs(velocityX / VELOCITY_NORMALIZE_CONSTANT) * normPressure, calcEqFreq(yPos));
//                Log.e("true in", "setProperties");
                return true;
            } else {
//                Log.e("false in", "setProperties");
                return false;
            }
        }

        private void setVolume(AudioTrack audioTrack, float volume) {
            audioTrack.setStereoVolume(volume, volume);
        }

        public void cancelTask(int index) throws InterruptedException {
            cords[index].stopTrack();
            this.running = false;
            this.new_task = false;
//            join();
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
}
