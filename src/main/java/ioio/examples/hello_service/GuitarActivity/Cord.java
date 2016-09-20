package ioio.examples.hello_service.GuitarActivity;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by omerrom on 12/07/16.
 */
public class Cord implements Runnable {
    private Task task;
    private int index;
    protected short[] sample, revSample;
    private AudioTrack audioTrack;
    private Equalizer equalizer;
    final static int DEFAULT_RATE = 44100;
    private static final int MILI_CONVERTOR = 1000;
    public static final int MAX_FREQ = 400 * MILI_CONVERTOR;
    private static final double PERCENTAGE_PART_ONE = 1;
    private static final double PERCENTAGE_PART_TWO = 0;
    private int bufferAddPerIteration = 0;
    private int partOne, numOfIterations = CordManager.NUM_OF_ITERATIONS;
    private short minEQLevel, maxEQLevel, bandNumMaxFreq;
    private PlayingGuitarBuffer buffer;

    private static Context context;
    private static final float[] RATE_ARRAY = new float[13];
    private static final int PRESSURE_CONST = 4000;
    private static final float MIN_PRESSURE = 0.001f;

    static {
        for (int i = 0; i < RATE_ARRAY.length; i++) {
            RATE_ARRAY[i] = (float) Math.pow(2, i / (float) 12);
            Log.e("RATE_ARRAY" + i + ": ", "" + RATE_ARRAY[i]);
        }
    }

    public Cord(int index, Context context, int wav, int numOfIterations) {
        Cord.context = context;
        this.task = new Task();
        this.index = index;
        this.partOne = (int)((double)numOfIterations * PERCENTAGE_PART_ONE);
        int minBufferSize = AudioTrack.getMinBufferSize(DEFAULT_RATE,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, DEFAULT_RATE,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize,
                AudioTrack.MODE_STREAM);
        setCord(wav);
    }

    public float calcVolume(float currVolume, float pressure, boolean withIOIO) {
        if (withIOIO) {
            if (pressure == 0.0) {
                pressure = MIN_PRESSURE;
            }
            currVolume -= ((float)1/40000);//(400 * getPressureLog(pressure)));
//            Log.e("vol: ",Float.toString(currVolume));
        }
        return currVolume;
    }

    private float getPressureLog(float pressure) {
        return (float) Math.log(pressure);
    }

    private static int calcPitch(int frat) {
//        Log.e("pitch: ", "" + (int) (DEFAULT_RATE * RATE_ARRAY[frat]));
        return (int) (DEFAULT_RATE * RATE_ARRAY[frat]);
    }

    public void initEqualizer(int eqFreq) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            for (short i = 0; i < equalizer.getNumberOfBands(); i++) {
                if (i <= bandNumMaxFreq) {
                    double mult = getBandPrecentage(i + 1, bandNumMaxFreq + 1, eqFreq);
//                    Log.e("properties: eqFreq", "" + eqFreq);
//                    Log.e("prop: setBandLevel", "" + (short)(minEQLevel + ((maxEQLevel - minEQLevel) * mult)));
                    equalizer.setBandLevel(i, (short)(minEQLevel + ((maxEQLevel - minEQLevel) * mult)));
                } else {
                    equalizer.setBandLevel(i, maxEQLevel);
                }
            }
        }
    }

    private double getBandPrecentage(int bandNum, int bandNumMaxFreq, int freq) {
        double percentage = Math.pow(((double) (bandNum) / (bandNumMaxFreq)), 2) * (1 - ((double) freq / MAX_FREQ));
        return Math.min(2 * percentage, 1);
    }

    public int getPartOne() {
        return partOne;
    }

    public void stopTrack() {
        if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack.stop();
        }
    }

    public void playIteration(int currIndex, int curSarig, float currVolume) {
        int playbackRate = Cord.calcPitch(GuitarActivity.retSrigim[curSarig] + 1);
        audioTrack.setPlaybackRate(playbackRate);
//       check the 10* ___, its something with the equlizer.
        audioTrack.setStereoVolume(10*currVolume, 10*currVolume);
//                setVolume(audioTrack, currVolume);
        play(currIndex);
        if (index == 0) {
            buffer.writeToBuffer(Arrays.copyOfRange(sample, currIndex, currIndex + bufferAddPerIteration),
                    playbackRate, currVolume);
            buffer.writeToFile();
        }
    }

    public int getBufferAddPerIteration() {
        return bufferAddPerIteration;
    }

    private void play(int start) {
        audioTrack.play();
        int writeSize =  audioTrack.write(sample, start, bufferAddPerIteration);
    }

    private void play(short[] music, int start, int end) {
        audioTrack.play();
        int writeSize =  audioTrack.write(music, start, end);
    }

    private static byte[] convertStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final int BUFFER_SIZE = 4096;
        byte[] buff = new byte[BUFFER_SIZE];
        int i;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }
        return baos.toByteArray();
    }

    public void pauseTask() {
        if (task != null) {
            try {
                task.pauseTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized void resume(float pressure, float velocityX, float yPos) {
        task.setAndStart(pressure, velocityX, yPos);
    }

    public void setCord(int wav) {
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
        if (index == 0) {
            buffer = new PlayingGuitarBuffer("/" + System.currentTimeMillis(), context.getFilesDir().getPath());
        }
    }

    @Override
    public void run() {
        task.start();
    }

    public class Task extends Thread {
        private float pressure = 0f;
        private float velocityX;
        private float yPos;
        private volatile boolean running = true;
        private volatile boolean new_task = false;
        private float startVolume;
        private int eqFreq;
        public int counter = 0;

        private static final float MIN_VELOCITY = 0;
        private static final int VELOCITY_NORMALIZE_CONSTANT = 10000;
        private static final float MAX_PRESSURE = 1f;
        private static final float MIN_PRESSURE = 0.7f;
        private static final int MAX_BRIDG_PRESSURE = 1000;
        private static final int MIN_BRIDG_PRESSURE = 0;


        public Task() {
            running = true;
        }

        @Override
        public void run() {
            while(true) {
                synchronized(this) {
                    while(!running) {
                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (running) {
                    new_task = false;
                    stopTrack();
                    int currIndex = 0;
                    if (setProperties()) {
                        initEqualizer(eqFreq);
                        float currVolume = startVolume;
//                        Log.e("getStartVolume: ", "" + currVolume);
                        for (int i = 0; i < getPartOne(); i++) {
                            if (!running || new_task) {
                                break;
                            }

                            playIteration(currIndex, index, currVolume);
                            currIndex += getBufferAddPerIteration();
                            currVolume = calcVolume(currVolume, GuitarActivity.retMeitar[index], true);
                            float presh = GuitarActivity.retMeitar[index];

//                            Log.e("presh_before:        ", presh + "");
                            if (!BridgPressure(presh, index)) {
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

        public Boolean BridgPressure(float presh, int index){
//            Log.e("presh_before:        ", presh + "");

            if (presh == 0.0){
                presh = 1000;
            }
            else if (presh < 0.1){
                presh = 8;
            }
            else{
//                Log.e("INDEX",index + "");
                if (index == 100)
                {
                    presh = (float)(((MAX_BRIDG_PRESSURE - MIN_BRIDG_PRESSURE) * (presh - 0.01) / (0.4 - 0.01)) + MIN_BRIDG_PRESSURE);
                }
                else {
                    presh = (float) (((MAX_BRIDG_PRESSURE - MIN_BRIDG_PRESSURE) * (presh - 0.01) / (0.6 - 0.01)) + MIN_BRIDG_PRESSURE);
                }
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
            this.new_task = true;
            resumeThread();
        }

        private boolean setProperties() {
            float normVelocity = Math.abs(velocityX / VELOCITY_NORMALIZE_CONSTANT);
            if (normVelocity > MIN_VELOCITY) {
//                Log.e("pressure", "" + pressure);
                float normPressure = MIN_PRESSURE + (MAX_PRESSURE - MIN_PRESSURE) * pressure;
//                Log.e("normPressure", "" + normPressure);
                this.startVolume = normVelocity * normPressure;
                this.eqFreq = calcEqFreq(yPos);
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

        public void pauseTask() throws InterruptedException {
            stopTrack();
            this.running = false;
            this.new_task = false;
        }

        /**
         * calculating the wanted equalizer freq using the distance of the Y axis event from
         * the middle of the screen (reltive distance from the middle).
         * @param currY the Y axis of the touch event.
         * @return
         */
        private int calcEqFreq(float currY) {
//        Log.e("in", "onFling, e1.getY(): " + (int) (Cord.MAX_FREQ * Math.abs((height / 2) - currY) / height));
            return (int) (2 * Cord.MAX_FREQ * Math.abs((CordManager.getHeight() / 2) - currY) / CordManager.getHeight());
        }

        public boolean isRunning() {
            return running;
        }

        synchronized void resumeThread() {
            running = true;
            notify();
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }
}
