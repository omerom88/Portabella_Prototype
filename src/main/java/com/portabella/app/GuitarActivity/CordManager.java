//package com.portabella.app.GuitarActivity;
//
//import android.content.Context;
//
//import java.io.File;
//
//import com.portabella.app.Recording.Record;
//
///**
// * Created by Tomer on 27/07/2016.
// */
//public class CordManager {
//
//    public final static int NUM_OF_ITERATIONS = 50;
//    public static final int NUM_OF_MEITARS = 6;
//    public static Cord[] cords = new Cord[NUM_OF_MEITARS];
////    public static int[] REG_NOTES = {0,0,0,0,0,0};
//    private static float width;
//    private static Record record;
//    private static GuitarActivity guitarActivity;
//
//    /* A private Constructor prevents any other
//     * class from instantiating.
//     */
//    public static void init(GuitarActivity guitarActivity, Context context, int[] NOTES){
//        CordManager.guitarActivity = guitarActivity;
//        record = Record.getInstance(guitarActivity);
//        for (int i = 0; i < NUM_OF_MEITARS; i++) {
//            cords[i] = new Cord(i, context, NOTES[i], NUM_OF_ITERATIONS);
//            cords[i].run();
//            cords[i].pauseTask();
//        }
//    }
//
//    public static float getWidth() {
//        return width;
//    }
//
//    public static void pauseAllTasks() {
//        for (int i = 0; i < NUM_OF_MEITARS; i++) {
//            if (cords[i] != null) {
//                cords[i].pauseTask();
//            }
//        }
//    }
//
//    public static void restartTask(int index, float pressure, float velocityY, float xPos) {
//        cords[index].pauseTask();
//        cords[index].resume(pressure, velocityY, xPos);
//    }
//
//    public static void setWidth(float widthLayout) {
//        width = widthLayout;
//    }
//
//    public static void setNewCords(int[] notesArray) {
//        for (int i = 0; i < NUM_OF_MEITARS; i++) {
//            cords[i].setCord(notesArray[i]);
//        }
//    }
//
//    public static void startRecording() {
//        for (int i = 0; i < NUM_OF_MEITARS; i++) {
//            record.addSample(i, getSample(i));
//            cords[i].resume(0, 0, 0);
//        }
//        Record.getInstance(guitarActivity).start();
//    }
//
//    public static void stopRecording() {
//        record.stop();
//    }
//
//    public static boolean isRecording() {
//        return record.isRecording();
//    }
//
//    public static int calcShortsPerTime(int index, int timeInMillis, short[] sample) {
//        return record.calcShortsPerTime(index, timeInMillis, sample);
//    }
//
//    public static void writeToBuffer(int index, short[] shorts, int playbackRate, float currVolume) {
//        record.writeToBuffer(index, shorts, playbackRate, currVolume);
//    }
//
//    public static void writeToFile(int index) {
//        record.writeToFile(index);
//    }
//
//    public static short[] getSample(int index) {
//        return cords[index].getSample();
//    }
//
//    public static void cancelRecord() {
//        record.cancel();
//    }
//
//    public static File saveFile(String fileName) throws OutOfMemoryError{
//        return record.saveFile(fileName);
//    }
//
//    public static void restartAllTasks() {
//        for (int i = 0; i < NUM_OF_MEITARS; i++) {
//            if (cords[i].isInit()) {
//                cords[i].restartTask();
//            }
//        }
//    }
//
//    public static void cancelAllTasks() {
//        for (int i = 0; i < NUM_OF_MEITARS; i++) {
//            cords[i].cancelTask();
//        }
//    }
//}
