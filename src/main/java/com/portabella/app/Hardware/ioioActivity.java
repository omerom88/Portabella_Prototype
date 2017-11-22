//package com.portabella.app.Hardware;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//
//import com.portabella.app.R;
//
///**
// * Created by omerrom on 28/07/16.
// */
//public class ioioActivity extends Activity {
//
//    private IntentFilter mIntentFilter;
//    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
//    private String LOG_TAG = null;
//    private float retPresure;
//    private float retleftX;
//
//
//
//
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ioio_ne);
//        LOG_TAG = this.getClass().getSimpleName();
//
//
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(mBroadcastStringAction);
//        final Intent intent = new Intent(ioioActivity.this, HelloIOIOService2.class);
//        startService(intent);
//
//    }
//
//    public void onResume() {
//        super.onResume();
//        registerReceiver(mReceiver, mIntentFilter);
//    }
//
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(mBroadcastStringAction)) {
//                float meitar0 = intent.getFloatExtra("meitar0", 0);
//                float meitar1 = intent.getFloatExtra("meitar1", 0);
//                float meitar2 = intent.getFloatExtra("meitar2", 0);
//                float meitar3 = intent.getFloatExtra("meitar3", 0);
//                float meitar4 = intent.getFloatExtra("meitar4", 0);
//                float meitar5 = intent.getFloatExtra("meitar5", 0);
//
//
//                int srigim0 = intent.getIntExtra("srigim0", 0);
//                int srigim1 = intent.getIntExtra("srigim1", 0);
//                int srigim2 = intent.getIntExtra("srigim2", 0);
//                int srigim3 = intent.getIntExtra("srigim3", 0);
//                int srigim4 = intent.getIntExtra("srigim4", 0);
//                int srigim5 = intent.getIntExtra("srigim5", 0);
//
//
//                if (meitar0 != 0) {
//                    retleftX = srigim0;
//                    retPresure = meitar0;
//                }
//
//                if (meitar1 != 0) {
//                    retleftX = srigim1;
//                    retPresure = meitar1;
//                }
//                if (meitar2 != 0) {
//                    retleftX = srigim2;
//                    retPresure = meitar2;
//                }
//                if (meitar3 != 0) {
//                    retleftX = srigim3;
//                    retPresure = meitar3;
//                }
//                if (meitar4 != 0) {
//                    retleftX = srigim4;
//                    retPresure = meitar4;
//                }
//                if (meitar5 != 0) {
//                    retleftX = srigim5;
//                    retPresure = meitar5;
//                }
//
//            }
//        }
//    };
//
//    public float getPressure() {
//        return retPresure;
//    }
//
//    public int getFrat() {
//        return (int) retleftX;
//    }
//
//    @Override
//    protected void onPause() {
//        unregisterReceiver(mReceiver);
//        super.onPause();
//    }
//
//}
