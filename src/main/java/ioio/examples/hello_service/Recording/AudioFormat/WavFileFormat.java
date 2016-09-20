package ioio.examples.hello_service.Recording.AudioFormat;

import android.util.Log;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ioio.examples.hello_service.GuitarActivity.PlayingGuitarBuffer;

/**
 * Created by Tomer on 15/09/2016.
 */
public class WavFileFormat extends AudioFormat {

    private RandomAccessFile outFile;
    private String fileName;
    private String path;
    private boolean removedHeadersFromBuffer = false;

    private long chunkSize;

    private static final int HEADER_SIZE = 44;
    private static final int INT_SIZE_IN_BYTES = 4;
    private static final int SHORT_SIZE_IN_BYTES = 2;
    private static final int BIT_SIZE = 8;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_CHANNELS = 1;
    private static final String RIFF = "RIFF";
    private static final String WAVE = "WAVE";
    private static final String FMT = "fmt ";
    private static final String DATA = "data";
    private static final String WAV = ".wav";
    private static final byte[] SUB_CHUNK_SIZE = intToByteArray((int) 16);
    private static final byte[] FORMAT = shortToByteArray((short)1);
    private static final byte[] CHANNELS = shortToByteArray((short)DEFAULT_CHANNELS);
    private static final byte[] SAMPLE_RATE = intToByteArray((int) DEFAULT_SAMPLE_RATE);
    private static final byte[] BYTE_RATE = intToByteArray((DEFAULT_SAMPLE_RATE * BIT_SIZE * DEFAULT_CHANNELS) / 8);
    private static final byte[] BLOCK_ALIGN = shortToByteArray((short)2);
    private static final byte[] BIT_PER_SAMPLE = shortToByteArray((short)16);

    // write out the wav file
    public WavFileFormat(String fileName, String path) {
        try {
//            Log.e("WavFileFormat", "in WavFileFormat");
            this.fileName = fileName;
            this.path = path;
            outFile = new RandomAccessFile(new File(path + fileName + WAV), "rw");
            outFile.write(new byte[HEADER_SIZE]);
            outFile.close();
            chunkSize = HEADER_SIZE;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ===========================
    // CONVERT BYTES TO JAVA TYPES
    // ===========================
        public static byte[] intToByteArray(int num) {
            return new byte[] {
                    (byte) (num & 0xFF),
                    (byte) ((num >> 8) & 0xFF),
                    (byte) ((num >> 16) & 0xFF),
                    (byte) ((num >> 24) & 0xFF)
            };
        }
        // convert a short to a byte array
        public static byte[] shortToByteArray(short num) {
            return new byte[] {
                    (byte)(num & 0xff),
                    (byte)((num >> 8) & 0xff)
            };
        }

    private void reWriteHeaders(File file) {
        // write the wav file per the wav file format
        try {
//            Log.e("reWriteHeaders", "in reWriteHeaders");
            outFile = new RandomAccessFile(file, "rw");
//            outFile = new DataOutputStream(new FileOutputStream(path + fileName + WAV));
            outFile.writeBytes(RIFF);                 // 00 - RIFF
            outFile.write(intToByteArray((int) chunkSize - RIFF.length() - INT_SIZE_IN_BYTES), 0, INT_SIZE_IN_BYTES);     // 04 - how big is the rest of this file?
            outFile.writeBytes(WAVE);                 // 08 - WAVE
            outFile.writeBytes(FMT);                 // 12 - fmt
            outFile.write(intToByteArray(16), 0, INT_SIZE_IN_BYTES); // 16 - size of this chunk
            outFile.write(FORMAT, 0, SHORT_SIZE_IN_BYTES);        // 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
            outFile.write(CHANNELS, 0, SHORT_SIZE_IN_BYTES);  // 22 - mono or stereo? 1 or 2?  (or 5 or ???)
            outFile.write(intToByteArray(DEFAULT_SAMPLE_RATE), 0, INT_SIZE_IN_BYTES);        // 24 - samples per second (numbers per second)
            outFile.write(intToByteArray((DEFAULT_SAMPLE_RATE * BIT_SIZE * 2) / 8), 0, INT_SIZE_IN_BYTES);      // 28 - bytes per second
            outFile.write(BLOCK_ALIGN, 0, SHORT_SIZE_IN_BYTES);    // 32 - # of bytes in one sample, for all channels
            outFile.write(BIT_PER_SAMPLE, 0, SHORT_SIZE_IN_BYTES); // 34 - how many bits in a sample(number)?  usually 16 or 24
            outFile.writeBytes(DATA);                 // 36 - data
            outFile.write(intToByteArray((int) chunkSize - HEADER_SIZE), 0, INT_SIZE_IN_BYTES);      // 40 - how big is this data chunk
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void writeFile(PlayingGuitarBuffer buffer) {
        File file = new File(path + fileName +  WAV);
//        Log.e("writeFile: ", "" + file.getAbsolutePath());
//        Log.e("writeFile: ", "" + file.length());
        try {
            outFile = new RandomAccessFile(file, "rw");
            outFile.seek(file.length());
            for (PlayingGuitarBuffer.PlayingSegment seg : buffer.readFromBuffer()) {
                byte[] bytes = new byte[seg.getShortArray().length * 2];
//                ByteBuffer byteBuf = ByteBuffer.allocate(2 * seg.getShortArray().length);
//                int i = 0;
//                Log.e("getShortArray: ", "" + seg.getShortArray()[i]);

                ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(seg.getShortArray());
                for (int i = 0; i < bytes.length; i += 2) {
                    short audioSample = (short) (((bytes[i + 1] & 0xff) << 8) | (bytes[i] & 0xff));
                    audioSample = (short) (audioSample * 1 * 0.5);
                    bytes[i] = (byte) audioSample;
                    bytes[i+1] = (byte) (audioSample >> 8);
                }
//                while (seg.getShortArray().length > i) {
//                    byteBuf.putShort(seg.getShortArray()[i]);
//                    i++;
//                }
                if (!removedHeadersFromBuffer) {
                    byte[] tempBytes = bytes.clone();
                    bytes = new byte[tempBytes.length - HEADER_SIZE];
                    System.arraycopy(tempBytes, HEADER_SIZE, bytes, 0, tempBytes.length - HEADER_SIZE);
                    removedHeadersFromBuffer = true;
                }
                outFile.write(bytes);
                chunkSize += bytes.length;
//                Log.e("writeFile: ", "" + file.length());
            }
            outFile.close();
            reWriteHeaders(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("hi :)");
        System.out.println(("SUB_CHUNK_SIZE: "+ Arrays.toString(SUB_CHUNK_SIZE) + ""));
        System.out.println(("SUB_CHUNK_SIZE: "+ Arrays.toString(FORMAT) + ""));
        System.out.println(("SUB_CHUNK_SIZE: "+ Arrays.toString(CHANNELS) + ""));
        System.out.println(("SUB_CHUNK_SIZE: "+ Arrays.toString(SAMPLE_RATE) + ""));

        FileInputStream fileInputStream;
        File file = new File("C:\\Users\\Tomer\\Desktop\\1.wav");
        byte[] bFile = new byte[(int) file.length()];
        byte[] testFile = new byte[(int) file.length()];
        PlayingGuitarBuffer buffer = new PlayingGuitarBuffer("test", "C:\\Users\\Tomer\\Desktop\\");
        try {
            //convert file into array of bytes

            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
//            for (int i = 0; i < bFile.length; i++) {
//                bFile[i] = (byte) i;
//            }
            short[] shortArray = new short[bFile.length / 2];
            ByteBuffer.wrap(bFile).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArray);
            buffer.writeToBuffer(shortArray, 0, 0);
            buffer.writeToFile();

            fileInputStream = new FileInputStream(new File("C:\\Users\\Tomer\\Desktop\\test.wav"));
            fileInputStream.read(testFile);
            fileInputStream.close();

            System.out.println("hi :)");
//
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bFile.length; i++) {
            if (bFile[i] != testFile[i]) {
                int x = 5;
            }
        }
    }
}
