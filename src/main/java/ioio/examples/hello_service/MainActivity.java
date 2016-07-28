package ioio.examples.hello_service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {

    private static final int[] BUTTONS = {R.id.E_LOW, R.id.A, R.id.D, R.id.G, R.id.B, R.id.E_HIGH};
    private static CordManager cordManager;
    private IntentFilter mIntentFilter;
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    public static float retPresure;
    public static float retleftY;
    public static float retleftX;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //////////////// the gesture  /////////////////
        cordManager = CordManager.getInstance(getApplicationContext());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
        }
        GestureListener.setHeight(size.y);
        /////////////// string buttons  ///////////////
        for (int i = 0; i < BUTTONS.length; i++) {
            LinearLayout stringButton = (LinearLayout) findViewById(BUTTONS[i]);
            assert stringButton != null;
            stringButton.setTag(i);
            stringButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
//                    long startTime = System.currentTimeMillis();
                    int index = (Integer) view.getTag();
                    cordManager.cancelTask(index);
                    cordManager.runTask(index, event);
//                    Log.e("startTime: ", "" + (System.currentTimeMillis() - startTime));
                    return true;
                }
            });
        }

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        final Intent intent = new Intent(MainActivity.this, HelloIOIOService.class);
        startService(intent);
    }

    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastStringAction)) {
                float temp0 = intent.getFloatExtra("presure0", 0f);
                float temp1 = intent.getFloatExtra("presure1", 0f);
                float temp2 = intent.getFloatExtra("presure2", 0f);
                float temp3 = intent.getFloatExtra("presure3", 0f);
                float temp4 = intent.getFloatExtra("presure4", 0f);
                float temp5 = intent.getFloatExtra("presure5", 0f);

                if (temp0 != 0) {
                    retleftX = 5;
                    retPresure = temp0;
                }

                else if (temp1 != 0) {
                    retleftX = 4;
                    retPresure = temp1;
                }
                else if (temp2 != 0) {
                    retleftX = 3;
                    retPresure = temp2;
                }
                else if (temp3 != 0) {
                    retleftX = 2;
                    retPresure = temp3;
                }
                else if (temp4 != 0) {
                    retleftX = 1;
                    retPresure = temp4;
//                }
//                else if (temp5 != 0) {
//                    retleftX = 1;
//                    retPresure = temp5;
                } else {
                    retleftX = 0;
                    retPresure = 0;
                }
//                Log.e("ret: ", "" + retleftX);
//                Log.e("retPresure: ", "" + retPresure);
            }
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        cordManager.cancelAllTasks();
        unregisterReceiver(mReceiver);
        Log.d("", "The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("", "The onDestroy() event");
    }
}
