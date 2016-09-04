package ioio.examples.hello_service.GuitarActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * This class will be used as a buffer for all the guitar playing of the app (mostly for recording).
 * Created by Tomer on 02/09/2016.
 */
public class PlayingGuitarBuffer {

    private List<PlayingSegment> buffer;

    PlayingGuitarBuffer() {
        buffer = new LinkedList<PlayingSegment>();
    }

    public synchronized void writeToBuffer(List<Byte> byteList, int playbackRate, float volume) {
        buffer.add(new PlayingSegment(byteList, playbackRate, volume));
    }

    public synchronized List<PlayingSegment> readFromBuffer() {
        List<PlayingSegment> tempBuff = new LinkedList<PlayingSegment>(buffer);
        buffer.clear();
        return tempBuff;
    }

    private class PlayingSegment {
        List<Byte> byteList;
        int playbackRate;
        float volume;

        public PlayingSegment(List<Byte> byteList, int playbackRate, float volume) {
            this.byteList = byteList;
            this.playbackRate = playbackRate;
            this.volume = volume;
        }

        public List<Byte> getByteList() {
            return byteList;
        }

        public void setByteList(List<Byte> byteList) {
            this.byteList = byteList;
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
