package ioio.examples.hello_service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by omerrom on 11/08/16.
 */
public class Recorder {

    final int BUFFER_SIZE = 4096;  //change it to the size we want, can be a field
    ByteArrayOutputStream ByteoutputStream;
    InputStream inputStream;


    public Recorder(InputStream in){
        inputStream = in;
        ByteoutputStream = new ByteArrayOutputStream();
    }


    private void saveToFile(String path, byte[] buff){  //need to check if now we writing to the buffer - if then think about some lock or something
        int i;
        try {
            OutputStream outputStream = new FileOutputStream(path);
            ByteoutputStream.writeTo(outputStream);
            while ((i = inputStream.read(buff, 0, buff.length)) > 0) {
                ByteoutputStream.write(buff, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playFromFile(String path){}


}
