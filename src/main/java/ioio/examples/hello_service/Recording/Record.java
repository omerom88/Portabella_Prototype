package ioio.examples.hello_service.Recording;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Tomer on 02/09/2016.
 */
public class Record {

    private static Record rec;
    private static Context appContext;
    private static boolean hasInit = false;
    private static customCountDownTimer countDownTimer;

    private final static int SEC_TO_MILLIS = 1000;

    Record(Context appContext) {
        Record.appContext = appContext;
    }

    public static Record getInstance(Context appContext) {
        if (!hasInit) {
            rec = new Record(appContext);
            countDownTimer = new customCountDownTimer(new RecOptions());
            hasInit = true;
        }
        return rec;
    }

    private static class RecOptions {
        private long duration = DEFAULT_DUARTION; // duration of the record in millis.
        private int numOfLoops = DEFAULT_NUM_OF_LOOPS;

        public static final long DEFAULT_DUARTION = 60 * 1000;
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
        if (hasInit) {
            countDownTimer = new customCountDownTimer(duration * SEC_TO_MILLIS,
                    new RecOptions(duration * SEC_TO_MILLIS, numOfLoops));
            countDownTimer.start();
            countDownTimer.setRecording(true);
            return true;
        }
        return false;
    }

    public boolean start(){
        return start(RecOptions.DEFAULT_DUARTION, RecOptions.DEFAULT_NUM_OF_LOOPS);
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
