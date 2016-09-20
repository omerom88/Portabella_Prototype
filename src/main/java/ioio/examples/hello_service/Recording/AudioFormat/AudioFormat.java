package ioio.examples.hello_service.Recording.AudioFormat;

import android.content.Context;

import ioio.examples.hello_service.GuitarActivity.PlayingGuitarBuffer;

/**
 * Created by Tomer on 15/09/2016.
 */
public abstract class AudioFormat {

    public abstract void writeFile(PlayingGuitarBuffer buffer);
}
