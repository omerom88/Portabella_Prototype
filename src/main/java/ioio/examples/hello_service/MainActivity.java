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
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private static final int[] BUTTONS = {R.id.E_LOW, R.id.A, R.id.D, R.id.G, R.id.B, R.id.E_HIGH};
    private static CordManager cordManager;
    private IntentFilter mIntentFilter;
    private String LOG_TAG = null;
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    public static float[] retMeitar = {0f,0f,0f,0f,0f,0f};
    public static int[] retSrigim = {-1,-1,-1,-1,-1,-1};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LOG_TAG = this.getClass().getSimpleName();
        LinearLayout[] layouts = new LinearLayout[6];
        for (int i = 0; i < layouts.length; i++) {
            layouts[i] = (LinearLayout) findViewById(BUTTONS[i]);
        }
        //////////////// the gesture  /////////////////
        cordManager = CordManager.getInstance(getApplicationContext());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
        }
        CordManager.setHeight(size.y);
        /////////////// string layouts  ///////////////
        LinearLayout strummingLayout = (LinearLayout) findViewById(R.id.mainLayout);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(layouts, this);
        strummingLayout.setOnTouchListener(activitySwipeDetector);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        final Intent intent = new Intent(MainActivity.this, HelloIOIOService2.class);
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

                for (int i = 0; i <= 5; i++){
                    String strM = "meitar" + Integer.toString(i);
                    String strS = "srigim" + Integer.toString(i);
                    retMeitar[i] = intent.getFloatExtra(strM, 0);
                    retSrigim[i] = intent.getIntExtra(strS, -1);
                }

//                float meitar0 = intent.getFloatExtra("meitar0", 0);
//                float meitar1 = intent.getFloatExtra("meitar1", 0);
//                float meitar2 = intent.getFloatExtra("meitar2", 0);
//                float meitar3 = intent.getFloatExtra("meitar3", 0);
//                float meitar4 = intent.getFloatExtra("meitar4", 0);
//                float meitar5 = intent.getFloatExtra("meitar5", 0);

//
//                int srigim0 = intent.getIntExtra("srigim0", 0);
//                int srigim1 = intent.getIntExtra("srigim1", 0);
//                int srigim2 = intent.getIntExtra("srigim2", 0);
//                int srigim3 = intent.getIntExtra("srigim3", 0);
//                int srigim4 = intent.getIntExtra("srigim4", 0);
//                int srigim5 = intent.getIntExtra("srigim5", 0);


//                if (meitar0 != 0) {
//                    retSrigim[0] = srigim0;
//                    retMeitar[0] = meitar0;
////                    Log.i(LOG_TAG, "retMeitar0:   "  + Float.toString(retMeitar));
////                    Log.i(LOG_TAG, "retSrigim0:   "  + Integer.toString(retSrigim));
//                }
//                else {
//                    retSrigim[0] = 0;
//                }
//
//                if (meitar1 != 0) {
//                    retSrigim[1] = srigim1;
//                    retMeitar[1] = meitar1;
////                    Log.i(LOG_TAG, "retMeitar1:   "  + Float.toString(retMeitar));
////                    Log.i(LOG_TAG, "retSrigim1:   "  + Integer.toString(retSrigim));
//                }
//                if (meitar2 != 0) {
//                    retSrigim[2] = srigim2;
//                    retMeitar[2] = meitar2;
////                    Log.i(LOG_TAG, "retMeitar2:   "  + Float.toString(retMeitar));
////                    Log.i(LOG_TAG, "retSrigim2:   "  + Integer.toString(retSrigim));
//                }
//                if (meitar3 != 0) {
//                    retSrigim[3] = srigim3;
//                    retMeitar[3] = meitar3;
////                    Log.i(LOG_TAG, "retMeitar3:   "  + Float.toString(retMeitar));
////                    Log.i(LOG_TAG, "retSrigim3:   "  + Integer.toString(retSrigim));
//                }
//                if (meitar4 != 0) {
//                    retSrigim[4] = srigim4;
//                    retMeitar[4] = meitar4;
////                    Log.i(LOG_TAG, "retMeitar4:   "  + Float.toString(retMeitar));
////                    Log.i(LOG_TAG, "retSrigim4:   "  + Integer.toString(retSrigim));
//                }
//                if (meitar5 != 0) {
//                    retSrigim[5] = srigim5;
//                    retMeitar[5] = meitar5;
////                    Log.i(LOG_TAG, "retMeitar5:   "  + Float.toString(retMeitar));
////                    Log.i(LOG_TAG, "retSrigim5:   "  + Integer.toString(retSrigim));
//                }
//
            }
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        CordManager.pauseUnRunningTasks();
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
