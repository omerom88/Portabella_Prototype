package ioio.examples.hello_service.GuitarActivity;

import android.util.Log;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ioio.examples.hello_service.Recording.AudioFormat.*;

/**
 * This class will be used as a buffer for all the guitar playing of the app (mostly for recording).
 * Created by Tomer on 02/09/2016.
 */
public class PlayingGuitarBuffer {

    private List<PlayingSegment> buffer;
    private AudioFormat outPutAudioFormat;
    private int bufferSize;

    private static final int WRITING_THRESHOLD = 100000;

    public PlayingGuitarBuffer(int index, String fileName, String path, short[] sample) {
        buffer = new LinkedList<PlayingSegment>();
        this.outPutAudioFormat = new WavFileFormat(fileName + "_" + index, path, sample);
    }

    public synchronized void writeToBuffer(short[] shortArray, int playbackRate, float volume) {
        buffer.add(new PlayingSegment(shortArray, playbackRate, volume));
        bufferSize += (shortArray.length * 2);
    }

    public synchronized List<PlayingSegment> readFromBuffer() {
        List<PlayingSegment> tempBuff = new LinkedList<PlayingSegment>(buffer);
        buffer.clear();
        bufferSize = 0;
        return tempBuff;
    }

    public synchronized void writeToFile() {
        if (bufferSize > WRITING_THRESHOLD){
            outPutAudioFormat.writeFile(this);
        }
    }

    public int calcShortsPerTime(int timeInMillis, short[] sample) {
        return outPutAudioFormat.calcShortsPerTime(timeInMillis, sample);
    }

    public void deleteFile() {
        outPutAudioFormat.deleteTempFile();
    }

    public File getOutPutFile() {
        return outPutAudioFormat.getFile();
    }

    public String getOutPutFileType() {
        return outPutAudioFormat.getOutPutFileType();
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public class PlayingSegment {
        short[] shortArray;
        int playbackRate;
        float volume;

        public PlayingSegment(short[] shortArray, int playbackRate, float volume) {
            this.shortArray = shortArray;
            this.playbackRate = playbackRate;
            this.volume = volume;
        }

        public short[] getShortArray() {
            return shortArray;
        }

        public void setShortArray(short[] shortArray) {
            this.shortArray = shortArray;
        }

        public int getPlaybackRate() {
            return playbackRate;
        }

        public void setPlaybackRate(int playbackRate) {
            this.playbackRate = playbackRate;
        }

        public float getVolume() {
            return volume;
        }

        public void setVolume(float volume) {
            this.volume = volume;
        }
    }
}
