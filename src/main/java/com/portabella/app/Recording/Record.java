package com.portabella.app.Recording;

import android.os.CountDownTimer;
import android.util.Log;

import com.portabella.app.GuitarActivity.Cord;
import com.portabella.app.GuitarActivity.CordManager;
import com.portabella.app.GuitarActivity.GuitarActivity;
import com.portabella.app.Recording.AudioFormat.AudioFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.portabella.app.GuitarActivity.PlayingGuitarBuffer;
import com.portabella.app.Recording.AudioFormat.WavFileFormat;

/**
 * Created by Tomer on 02/09/2016.
 */
public class Record {

    private static Record rec;
    private static GuitarActivity activity;
    private static boolean hasInit = false;
    private static customCountDownTimer countDownTimer;
    private static PlayingGuitarBuffer[] buffer;

    private final static int SEC_TO_MILLIS = 1000;

    private Record(GuitarActivity activity) {
        Record.activity = activity;
    }

    public static Record getInstance(GuitarActivity activity) {
        if (!hasInit) {
            rec = new Record(activity);
            countDownTimer = new customCountDownTimer(new RecOptions());
            hasInit = true;
            buffer = new PlayingGuitarBuffer[CordManager.NUM_OF_MEITARS];
        }
        return rec;
    }

    public void addSample(int index, short[] sample) {
        buffer[index] = new PlayingGuitarBuffer(index, "/" + System.currentTimeMillis(),
                activity.getFilesDir().getPath(), sample);
    }

    public void writeToBuffer(int index, short[] shorts, int playbackRate, float currVolume) {
        buffer[index].writeToBuffer(shorts, playbackRate, currVolume);
    }

    public void writeToFile(int index) {
        buffer[index].writeToFile();
    }

    public int calcShortsPerTime(int index, int timeInMillis, short[] sample) {
        return buffer[index].calcShortsPerTime(timeInMillis, sample);
    }

    public void cancel() {
        Log.e("Record: ", "cancel");
        for (PlayingGuitarBuffer pgb : buffer) {
            pgb.readFromBuffer();
            pgb.deleteFile();
        }
        countDownTimer.cancelRec();
    }

    public File saveFile(String fileName) throws OutOfMemoryError{
        AudioFormat outPutAudioFormat = null;
        try {
            List<ArrayList<Short>> tempSamples = new ArrayList<ArrayList<Short>>();
            int maxSize = 0;
            short[] shortArray = new short[1];
            for (int i = 0; i < CordManager.NUM_OF_MEITARS; i++) {
                shortArray = Cord.readSampleInShort(new FileInputStream(buffer[i].getOutPutFile()), false);
                ArrayList<Short> shorts = new ArrayList<Short>();
                for (short value : shortArray) {
                    shorts.add(value);
                }
                tempSamples.add(shorts);
                Log.e("outputArray size: ", "" + shorts.size());
                if (maxSize < shorts.size()) {
                    maxSize = shorts.size();
                }
            }


//            File outputFile = createNewFile(fileName);
//            OutputStream out = new FileOutputStream(outputFile);
            short[] output = new short[maxSize];
            for(int j = 0; j < output.length; j++){
                output[j] = mixSounds(getColumn(tempSamples, j));
            }
            Log.e("outputArray size: ", "" + output.length);
            byte[] outputArray = WavFileFormat.shortArrayToBytesArray(output, 1);
            String path = activity.getFilesDir().getPath();
            outPutAudioFormat = new WavFileFormat(fileName, path, shortArray);
            outPutAudioFormat.writeDataToFile(outputArray);
            outPutAudioFormat.reWriteHeaders();
            Log.e("outputArray size: ", "" + outputArray.length);
            cancel();
//            out.write(outputArray);
//            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            cancel();
            throw e;
        }
        return outPutAudioFormat != null ? outPutAudioFormat.getFile() : null;
    }

    public static short[] getColumn(List<ArrayList<Short>> array, int index){
        short[] column = new short[array.size()]; // Here I assume a rectangular 2D array!
        for(int i = 0; i < column.length; i++){
            ArrayList<Short> list = array.get(i);
            if (list.size() > index) {
                column[i] = list.get(index);
            }
        }
        return column;
    }

    private short mixSounds(short[] shortFromAllSamples) {
        float newSample = 0.0f;
        for(int i = 0; i < shortFromAllSamples.length; i++){
            newSample += (shortFromAllSamples[i] / 32768.0f);
        }
        newSample *= 0.8;
        // reduce the volume a bit:
        // hard clipping
        if (newSample > 1.0f) {
            newSample = 1.0f;
        }
        if (newSample < -1.0f) {
            newSample = -1.0f;
        }
        return (short)(newSample * 32768.0f);
    }

    private static class RecOptions {
        private long duration = DEFAULT_DURATION; // duration of the record in millis.
        private int numOfLoops = DEFAULT_NUM_OF_LOOPS;

        public static final long DEFAULT_DURATION = 60 * 60 * 1000;
        public static final int DEFAULT_NUM_OF_LOOPS = 1;

        public RecOptions() {
        }

        public RecOptions(long duration, int numOfLoops) {
            this.duration = duration;
            this.numOfLoops = numOfLoops;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public int getNumOfLoops() {
            return numOfLoops;
        }

        public void setNumOfLoops(int numOfLoops) {
            this.numOfLoops = numOfLoops;
        }
    }

    public boolean start(long duration, int numOfLoops){
        Log.e("onStart: ", "" + duration);
        if (hasInit) {
            countDownTimer = new customCountDownTimer(duration * SEC_TO_MILLIS,
                    new RecOptions(duration * SEC_TO_MILLIS, numOfLoops));
            countDownTimer.start();
            Log.e("onStart: ", "setRecording");
            countDownTimer.setRecording(true);
            return true;
        }
        return false;
    }

    public boolean start(){
        return start(RecOptions.DEFAULT_DURATION, RecOptions.DEFAULT_NUM_OF_LOOPS);
    }

    public boolean resume() {
        if (hasInit && !isRecording()) {
            countDownTimer.resume();
            return true;
        }
        return false;
    }

    public boolean pause() {
        if (hasInit && isRecording()) {
            countDownTimer.pause();
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (hasInit) {
            countDownTimer.cancelRec();
            return true;
        }
        return false;
    }

    public boolean isRecording() {
        return countDownTimer.isRecording();
    }

    private static class customCountDownTimer extends CountDownTimer {

        private RecOptions op;
        private boolean isRecording = false;
        private long millisRemaining =  0;
        private final static double ON_TICK_INTERVAL_PERCENTAGE = 0.01;

        public customCountDownTimer(RecOptions op) {
            super(op.duration, 0);
            this.op = op;
        }

        public customCountDownTimer(long millisRemaining, RecOptions op) {
            super(millisRemaining, (long)(millisRemaining * ON_TICK_INTERVAL_PERCENTAGE));
            this.op = op;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            millisRemaining = millisUntilFinished;
            Log.e("onTick: ", "" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if(op.getNumOfLoops() > 1) {
                Log.e("onFinish: ", "still going");
                op.setNumOfLoops(op.getNumOfLoops() - 1);
                countDownTimer = new customCountDownTimer(op);
                this.start();
            } else {
                Log.e("onFinish: ", "Finished :)");
                countDownTimer.setRecording(false);
                activity.openSaveFileDialog();
            }
        }

        public void resume() {
            countDownTimer = new customCountDownTimer(millisRemaining, op);
            countDownTimer.start();
            countDownTimer.setRecording(true);
        }

        public void pause() {
            Log.e("pause: ", isRecording + "");
            if (hasInit) {
                if (isRecording) {
                    countDownTimer.cancel();
                } else {
                    throw new IllegalStateException("CountDownTimerPausable is already" +
                            "in pause state, start counter before pausing it.");
                }
                isRecording = false;
            }
        }

        public void cancelRec(){
            if(isRecording) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                this.millisRemaining = 0;
                isRecording = false;
            }
        }

        public boolean isRecording() {
            return isRecording;
        }

        public void setRecording(boolean recording) {
            isRecording = recording;
        }
    }

}
