package com.portabella.app.Hardware;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;


/**
 * Created by omerrom on 06/05/2017.
 */
public class MicroReciver {
    private Context contex;
    private UsbSerialPort port;
    private UsbManager manager;

    public MicroReciver(Context contex, UsbSerialPort port, UsbManager manager) {
        this.contex = contex;
        this.port = port;
        this.manager = manager;
    }

    public void init() {
        if (port == null) {
            Toast.makeText(contex,"port null", Toast.LENGTH_LONG).show();
        } else {
            UsbDeviceConnection connection = manager.openDevice(port.getDriver().getDevice());
            if (connection == null) {
                Toast.makeText(contex,"Opening device failed", Toast.LENGTH_LONG).show();
            }
            try {
                port.open(connection);
                port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Toast.makeText(contex,e.getMessage(), Toast.LENGTH_LONG).show();
                try {
                    port.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                port = null;
                return;
            }
        }

        try {
            port.setDTR(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}