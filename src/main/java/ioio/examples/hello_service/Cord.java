package ioio.examples.hello_service;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Equalizer;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by omerrom on 12/07/16.
 */
public class Cord {
    private static final float[] RATE_ARRAY = new float[12];
    static {
        for (int i = 0; i < RATE_ARRAY.length; i++) {
            RATE_ARRAY[i] = (float) Math.pow(2, i / (float) 12);
            Log.e("RATE_ARRAY" + i + ": ", "" + RATE_ARRAY[i]);
        }
    }

    protected short[] sample, revSample;
    private AudioTrack audioTrack;
    private Equalizer equalizer;
    private Task task;
    final static int DEFAULT_RATE = 44100;
    private static final int MILI_CONVERTOR = 1000;
    public static final int MAX_FREQ = 400 * MILI_CONVERTOR;
    private static final double PERCENTAGE_PART_ONE = 1;
    private static final double PERCENTAGE_PART_TWO = 0;
    private int numOfIterations;
    private int bufferAddPerIteration = 0;
    private int partOne;//, partTwo;
    private short minEQLevel, maxEQLevel, bandNumMaxFreq;

    public Cord(Context context, int wav, int numOfIterations) {
        this.numOfIterations = numOfIterations;
        this.partOne = (int)((double)numOfIterations * PERCENTAGE_PART_ONE);
    //        this.partTwo = (int)((double)numOfIterations * PERCENTAGE_PART_TWO);
        Log.e("partOne", "" + partOne);
    //        Log.e("partTwo", "" + partTwo);
        int minBufferSize = AudioTrack.getMinBufferSize(DEFAULT_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, DEFAULT_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize,
                AudioTrack.MODE_STREAM);
        InputStream in1 = context.getResources().openRawResource(wav);
        byte[] array = new byte[0];
        try {
            array = convertStreamToByteArray(in1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sample = new short[array.length / 2];
        ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(sample);
        revSample = new short[sample.length];
        System.arraycopy(sample, 0, revSample, 0, sample.length);
        for (int i = 0 ; i < sample.length; i++) {
            revSample[i] = sample[sample.length - i - 1];
        }
        this.bufferAddPerIteration = sample.length / numOfIterations;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            this.equalizer = new Equalizer(0, audioTrack.getAudioSessionId());
            this.equalizer.setEnabled(true);
            this.minEQLevel = equalizer.getBandLevelRange()[0];
            this.maxEQLevel = equalizer.getBandLevelRange()[1];
            this.bandNumMaxFreq = (short) Math.max((int) equalizer.getBand(MAX_FREQ), 0);
        }
    }

    public void runTask(float startVolume, int eqFreq) {
        if (task != null && !task.isCancelled()) {
            task.cancelTask(true);
        }
        task = new Task(startVolume, eqFreq);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    public void checkBridgePressure() {
        if (MainActivity.retPresure > 0.15) {
            runTask(MainActivity.retPresure, 0);
        }
    }

    public void cancelTask() {
        if (task != null && !task.isCancelled()) {
            task.cancelTask(true);
        }
    }

    private void initEqualizer(int eqFreq) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            for (int i = 0; i < equalizer.getNumberOfBands(); i++) {
                if (i <= bandNumMaxFreq) {
                    double mult = getBandPrecentage(i + 1, bandNumMaxFreq + 1, eqFreq);
                    Log.e("properties: eqFreq", "" + eqFreq);
                    Log.e("prop: setBandLevel", "" + (short)(minEQLevel + ((maxEQLevel - minEQLevel) * mult)));
                    equalizer.setBandLevel((short) i, (short)(minEQLevel + ((maxEQLevel - minEQLevel) * mult)));
                } else {
                    equalizer.setBandLevel((short) i, maxEQLevel);
                }
            }
        }
    }

    private double getBandPrecentage(int bandNum, int bandNumMaxFreq, int freq) {
        double percentage = Math.pow(((double) (bandNum) / (bandNumMaxFreq)), 2) * (1 - ((double) freq / MAX_FREQ));
        return Math.min(2 * percentage, 1);
    }

    private class Task extends AsyncTask<Void, Void, Void> {
        float startVolume;
        int eqFreq;
        public Task(float startVolume, int eqFreq) {
            this.startVolume = startVolume;
            this.eqFreq = eqFreq;
        }

        @Override
        protected Void doInBackground(Void... strings) {
            if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                audioTrack.stop();
            }
            initEqualizer(eqFreq);
            int currIndex = 0;
            float currVolume = startVolume;
//            Log.e("currVolume", "" + currVolume);
//            Log.e("currRate", "" + currRate);
//            Log.e("music", "" + revSample.length);
            Log.e("retPresure", "" + MainActivity.retPresure);
            Log.e("retleftX", "" + (int)MainActivity.retleftX);
            for (int i = 0; i < partOne; i++) {
                audioTrack.setPlaybackRate(calcPitch((int)MainActivity.retleftX));
//                setVolume(audioTrack, currVolume);
                play(audioTrack, sample, currIndex, bufferAddPerIteration);
                if (isCancelled()) {
                    break;
                }
                currIndex += bufferAddPerIteration;
                currVolume = calcVolume(i, MainActivity.retPresure, (int) MainActivity.retleftX);
            }
            return null;
        }

        private float calcVolume(int i, float pressure, int frat) {
            return startVolume;
        }

        private int calcPitch(int frat) {
            Log.e("pitch: ", "" + (int) (DEFAULT_RATE * RATE_ARRAY[frat]));
            return (int) (DEFAULT_RATE * RATE_ARRAY[frat]);
        }

        private void setVolume(AudioTrack audioTrack, float volume) {
            audioTrack.setStereoVolume(volume, volume);
        }

        public void cancelTask(boolean mayInterruptIfRunning) {
            audioTrack.stop();
            cancel(mayInterruptIfRunning);
        }
    }

    private void play(AudioTrack audioTrack, short[] music, int start, int end) {
        audioTrack.play();
        int writeSize =  audioTrack.write(music, start, end);
//        Log.e("write size: ", "" + writeSize);
//        Log.e("start", "" + start);
//        Log.e("end", "" + end);
    }

    public static byte[] convertStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final int BUFFER_SIZE = 4096;
        byte[] buff = new byte[BUFFER_SIZE];
        int i;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }
        return baos.toByteArray();
    }
}
