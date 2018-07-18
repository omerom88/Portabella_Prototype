package com.portabella.app.GuitarActivity;

import android.content.Context;
import android.widget.Toast;

import com.hoho.android.usbserial.util.SerialInputOutputManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by omerrom on 04/06/2018.
 */


public class ArduinoListener implements SerialInputOutputManager.Listener {

    @Inject
    EventBus eventBus;

    private ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(1, 10, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private Context context;

    ArduinoListener(Context context){
        this.context = context;
        eventBus.register(this);
    }

    @Override
    public void onNewData(final byte[] bytes) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                updateReceivedData(bytes);
            }
        });
    }

    @Override
    public void onRunError(Exception e) {
        Toast.makeText(context," Runner stopped ", Toast.LENGTH_SHORT).show();
    }


    //** arduino recive data
    private void updateReceivedData(byte[] data) {
        String inputString = new String(data);
        if (inputString.length() > 1) {
            List<String> items = Arrays.asList(inputString.split("[\\s,]+"));
            for (int i = 0; i < items.size(); i++) {
                eventBus.post(new WritingEvent(items.get(i)));
            }
        }
    }
}
