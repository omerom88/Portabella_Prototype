package com.portabella.app.GuitarActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.portabella.app.Hardware.MicroReciver;
import com.portabella.app.R;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//// OAuth client 747262479409-3isjkbgjvp1el2o6pgfrjqt4aarj059m.apps.googleusercontent.com


/**
 * Created by Tomer on 21/08/2016.
 */



public class GuitarActivity extends Activity {

    public final String CHORD_ID = "1000";
    private final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public int[][] cellsMatrix = {{-1, -1, -1, -1}, {-1, -1, -1, -1}, {-1, -1, -1, -1}, {-1, -1, -1, -1}, {-1, -1, -1, -1}, {-1, -1, -1, -1}};
    public UsbSerialPort port;
    public UsbManager manager;
    public UsbSerialDriver driver;
    private SerialInputOutputManager mSerialIoManager;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    public BroadcastReceiver bReceiver;


    @OnClick(R.id.ledFromArduino)
    public void ledClick() {
        turnOnLeds(CHORD_ID); // TODO: 04/06/2018 CHORD ID is the string to tell the arduino which led to open
    }

    @BindView(R.id.textFromArduino)
    TextView textFromArduino;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (port != null) {
            try {
                port.close();
            } catch (IOException e) {
                // Ignore.
            }
            port = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        //** connect to port and comunicate with arduino pro micro
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        ProbeTable customTable = new ProbeTable();
        customTable.addProduct(9025, 32823, CdcAcmSerialDriver.class);
        UsbSerialProber prober = new UsbSerialProber(customTable);
        List<UsbSerialDriver> drivers = prober.findAllDrivers(manager);
        if (drivers.isEmpty()) {
            Toast.makeText(GuitarActivity.this, " No Arduino ", Toast.LENGTH_SHORT).show();
            return;
        }
        driver = drivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            Toast.makeText(GuitarActivity.this, " no Connection ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!manager.hasPermission(driver.getDevice())) {
            String ACTION_USB_PERMISSION = "com.blecentral.USB_PERMISSION";
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(), mPermissionIntent);
        } else {
            port = driver.getPorts().get(0);
            MicroReciver mr = new MicroReciver(this, port, manager);
            mr.init();
            onDeviceStateChange();
        }

    }

    /**
     * Helper functions to open connection with arduino
     */
    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (port != null) {
            mSerialIoManager = new SerialInputOutputManager(port, new ArduinoListener(this));
            mExecutor.submit(mSerialIoManager);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guitar_layout);
        ButterKnife.bind(this);
        openBrodcastReciver();

    }


    private void openBrodcastReciver() {
        //** open reciver to get data from arduino
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                assert action != null;
                if (action.equalsIgnoreCase(ACTION_USB_DETACHED)) {
                    LinearLayout rl = (LinearLayout) findViewById(R.id.guitarLayout);
                    rl.setBackgroundResource(R.drawable.realscreen4);

                }
                if (action.equalsIgnoreCase(ACTION_USB_ATTACHED)) {
                    LinearLayout rl = (LinearLayout) findViewById(R.id.guitarLayout);
                    rl.setBackgroundResource(R.drawable.realisticscreen);

                }
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_DETACHED);
        registerReceiver(bReceiver, filter);
    }

    public void turnOnLeds(String chord) {
        if (port != null) {
            try {
                port.write(chord.getBytes(), 1000);
                mSerialIoManager.writeAsync(chord.getBytes());
                // TODO: 07/06/2018 check which one better
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void writeDataFromArduino(WritingEvent event) {
        textFromArduino.setText(event.getText());
    }



}




