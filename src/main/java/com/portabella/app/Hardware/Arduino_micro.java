//package com.portabella.app.Hardware;
//
//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbDeviceConnection;
//import android.hardware.usb.UsbEndpoint;
//import android.hardware.usb.UsbInterface;
//import android.hardware.usb.UsbManager;
//import android.os.Bundle;
//
//import com.portabella.app.GuitarActivity.GuitarActivity;
//
//import java.util.HashMap;
//
///**
// * Created by omerrom on 01/05/17.
// */
//
//public class Arduino_micro extends Activity {
//    UsbManager manager;
//    UsbManager mUsbManager;
//    PendingIntent mPermissionIntent;
//    UsbDevice device;
//    UsbInterface intf;
//    UsbDeviceConnection connection;
//    UsbEndpoint endpoint;
//    UsbEndpoint endpointin;
//
//    //USB
//    final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
//    HashMap<String, UsbDevice> deviceList;
//    boolean sender = false;
//    boolean receiver = false;
//    int sendern =100;
//    byte[] bytes = new byte[]{5};
//    byte[] reply = new byte[]{0};
//    int TIMEOUT = 1000;
//    private boolean forceClaim = true;
//
//
//    void setup(){
//        manager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
//        mUsbManager = (UsbManager)  this.getSystemService(Context.USB_SERVICE);
//        deviceList = manager.getDeviceList();
//
//        connect();
//        connect2();
//
//
//    }
//
//    void connect(){
//
//        manager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
//        deviceList = manager.getDeviceList();
//        for (String keydevicename : deviceList.keySet()) {
//            device = deviceList.get(keydevicename);
//        }
//        mUsbManager.requestPermission(device, mPermissionIntent);
//    }
//
//
//    void connect2(){
//
//        intf = device.getInterface(1);
//        endpoint = intf.getEndpoint(0);
//        endpointin = intf.getEndpoint(1);
//        connection = mUsbManager.openDevice(device);
//        connection.claimInterface(intf, forceClaim);
//        connection.controlTransfer(0x21, 0x22, 0x1, 0, null, 0, 0);
//        receiver = true;
//    }
//
//    void sending(){
//
//        if(sender == true){
//
//            new Thread(new Runnable() {
//                public void run() {
//                    connection.bulkTransfer(endpoint, bytes, bytes.length, TIMEOUT);
//                    sender = false;
//
//                }
//            }).start();
//        }}
//
//
//
//    void reading(){
//
//        if(receiver == true){
//
//            new Thread(new Runnable() {
//                public void run() {
//                    connection.bulkTransfer(endpointin, reply, reply.length, TIMEOUT);
//                    String temp = new String(reply);
//                    GuitarActivity.writeLock.lock();
//                    GuitarActivity.TheString = GuitarActivity.TheString + temp;
//                    GuitarActivity.writeLock.unlock();
//                }
//            }).start();
//        }}
//
//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized (this) {
//                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        if(device != null){
//                            //call method to set up device communication
//                            reading();
//                        }
//                    }
//                    else {
//                        //Log.d(TAG, "permission denied for device " + device);
//                    }
//                }
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.getSystemService(Context.USB_SERVICE);
//        final String ACTION_USB_PERMISSION =
//                "com.android.example.USB_PERMISSION";
//
//        setup();
//        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        this.registerReceiver(mUsbReceiver, filter);
//
//    }
//
//
//}
