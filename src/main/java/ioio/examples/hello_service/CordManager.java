package ioio.examples.hello_service;

import android.content.Context;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by Tomer on 27/07/2016.
 */
public class CordManager {
    private static CordManager cordManager;
    final static int NUM_OF_ITERATIONS = 1000;
    private static final int NUM_OF_MEITARS = 6;
    private static final int MAX_BRIDG_PRESSURE = 1000;
    private static final int MIN_BRIDG_PRESSURE = 0;
    private static Cord[] cords = new Cord[NUM_OF_MEITARS];
    private static final int[] NOTES = {R.raw.e_string_low, R.raw.a_string, R.raw.d_string, R.raw.g_string,R.raw.b_string,
            R.raw.e_string_hi};
    private static Task[] tasks = new Task[6];
    private static float height;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private CordManager(Context context){
        for (int i = 0; i < NUM_OF_MEITARS; i++) {
            cords[i] = new Cord(context, NOTES[i], NUM_OF_ITERATIONS);
            tasks[i] = new Task(i);
            tasks[i].start();
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
//        tasks[index] = new Task(index, pressure, velocityX, yPos);
//        tasks[index].start();
        tasks[index].setAndStart(pressure, velocityX, yPos);
    }

    public static void cancelTask(int index) {
        if (tasks[index] != null) {
            try {
                tasks[index].cancelTask(index);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setHeight(float heightLayout) {
        height = heightLayout;
    }

    public static class Task extends Thread {
        private int curIndex;
        private Cord cord;
        private float pressure = 0f;
        private float velocityX;
        private float yPos;
        private volatile boolean running = true;
        private volatile boolean new_task = false;
        public int counter = 0;

        private static final float MIN_VELOCITY = 0;
        private static final int VELOCITY_NORMALIZE_CONSTANT = 10000;
        private static final float MAX_PRESSURE = 1f;
        private static final float MIN_PRESSURE = 0.9f;


        public Task(int index) {
            this.cord = cords[index];
            this.curIndex = index;
            running = true;
        }

        @Override
        public void run() {
            while (true) {
                if (running) {
                    new_task = false;
                    cord.stopTrack();
                    int currIndex = 0;
                    if (setProperties()) {
                        cord.initEqualizer(cord.getEqFreq());
                        float currVolume = cord.getStartVolume();

                        for (int i = 0; i < cord.getPartOne(); i++) {
                            if (!running || new_task) {
                                break;
                            }

                            cord.playIteration(currIndex, this.curIndex, currVolume);
                            currIndex += cord.getBufferAddPerIteration();
                            currVolume = cord.calcVolume(currVolume, MainActivity.retMeitar[this.curIndex], true);
                            float presh = MainActivity.retMeitar[this.curIndex];
                            if (!BridgPressure(presh)){
                                break;
                            }

                        }
                    }
                    if (!new_task) {
                        running = false;
                    }
                }
            }
        }

        public Boolean BridgPressure(float presh){

            if (presh == 0.0){
                presh = 1000;
            }
            else if (presh < 0.3){
                presh = 8;
            }
            else{
//                Log.e("presh_before:        ", Float.toString(presh));
                presh = (float)(((MAX_BRIDG_PRESSURE - MIN_BRIDG_PRESSURE) * (presh - 0.08) / (0.5 - 0.08)) + MIN_BRIDG_PRESSURE);
//                Log.e("presh:  ", Float.toString(presh));
            }

            if (counter > presh){
                counter = 0;
                //currVolume = 0;
                return false;
            }
            counter++;
            return true;
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
                cord.setProperties(normVelocity * normPressure, calcEqFreq(yPos));
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

        public boolean isRunning() {
            return running;
        }
    }

    public static void pauseUnRunningTasks() {
        for (Task task : tasks) {
            if (!task.isRunning()) {
                task.interrupt();
            }
        }
    }
}
