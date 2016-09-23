package ioio.examples.hello_service.Recording.AudioFormat;

import android.content.Context;

import java.io.File;

import ioio.examples.hello_service.GuitarActivity.PlayingGuitarBuffer;

/**
 * Created by Tomer on 15/09/2016.
 */
public abstract class AudioFormat {

    protected File file;

    public abstract void writeFile(PlayingGuitarBuffer buffer);

    public abstract int calcShortsPerTime(int timeInMillis, short[] wavByteArray);

    public void deleteTempFile() {
        file.delete();
    }

    public File getFile() {
        return file;
    }

    public abstract String getOutPutFileType();

    public abstract void writeDataToFile(byte[] outputArray);

    public abstract void reWriteHeaders();
}
