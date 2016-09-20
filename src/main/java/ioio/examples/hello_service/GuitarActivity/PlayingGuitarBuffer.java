package ioio.examples.hello_service.GuitarActivity;

import android.content.Context;

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

    public PlayingGuitarBuffer(String fileName, String path) {
        buffer = new LinkedList<PlayingSegment>();
        this.outPutAudioFormat = new WavFileFormat(fileName, path);
    }

    public synchronized void writeToBuffer(short[] shortArray, int playbackRate, float volume) {
        buffer.add(new PlayingSegment(shortArray, playbackRate, volume));
    }

    public synchronized List<PlayingSegment> readFromBuffer() {
        List<PlayingSegment> tempBuff = new LinkedList<PlayingSegment>(buffer);
        buffer.clear();
        return tempBuff;
    }

    public synchronized void writeToFile() {
        outPutAudioFormat.writeFile(this);
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
