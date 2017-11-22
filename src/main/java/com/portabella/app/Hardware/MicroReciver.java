package com.portabella.app.Hardware;

import android.hardware.usb.UsbDeviceConnection;

import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.portabella.app.GuitarActivity.GuitarActivity;

import java.io.IOException;

/**
 * Created by omerrom on 06/05/2017.
 */
public class MicroReciver {
    private final String TAG = SerialConsoleActivity.class.getSimpleName();

    public MicroReciver() {
    }

    public void init() {
        if (GuitarActivity.port == null) {
            GuitarActivity.G_but.setText("sport null");
        } else {
            UsbDeviceConnection connection = GuitarActivity.manager.openDevice(GuitarActivity.port.getDriver().getDevice());
            if (connection == null) {
                GuitarActivity.G_but.setText("Opening device failed");
            }
            try {
                GuitarActivity.port.open(connection);
                GuitarActivity.port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                GuitarActivity.G_but.setText(e.getMessage());
                try {
                    GuitarActivity.port.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                GuitarActivity.port = null;
                return;
            }
        }

        try {
            GuitarActivity.port.setDTR(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
//    public static void updateReceivedData(byte[] data) {
//        byte[] by = new byte[1];
//        by[0] = data[data.length-1];
////        final String message = "Read " + data.length + " bytes: \n"
////                + HexDump.dumpHexString(by) + "\n\n";
//        final String st = new String(data);
//        GuitarActivity.but.setText(st);
////        final String s = HexDump.dumpHexString(data);
//    }


//    private final SerialInputOutputManager.Listener mListener =
//            new SerialInputOutputManager.Listener() {
//
//                @Override
//                public void onRunError(Exception e) {
//                    Log.d(TAG, "Runner stopped.");
//                }
//
//                @Override
//                public void onNewData(final byte[] data) {
//                    GuitarActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            updateReceivedData(data);
//                        }
//                    };
//                }
//            };
//}
