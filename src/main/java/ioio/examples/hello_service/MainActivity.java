package ioio.examples.hello_service;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ioio.examples.hello_service.GuitarActivity.CordManager;
import ioio.examples.hello_service.GuitarActivity.GuitarActivity;

public class MainActivity extends Activity {

    private static final int[] BUTTONS = {R.id.E_LOW, R.id.A, R.id.D, R.id.G, R.id.B, R.id.E_HIGH};
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";


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
        setContentView(R.layout.activity_main);
    };

    public void playGuitar(View view) {
        Intent intent = new Intent(this, GuitarActivity.class);
        startActivity(intent);
    }
//        setContentView(R.layout.guitar_layout);
//        LOG_TAG = this.getClass().getSimpleName();
//        LinearLayout[] layouts = new LinearLayout[6];
//        for (int i = 0; i < layouts.length; i++) {
//            layouts[i] = (LinearLayout) findViewById(BUTTONS[i]);
//        }
//        //////////////// the gesture  /////////////////
//        cordManager = CordManager.getInstance(getApplicationContext());
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            display.getSize(size);
//            CordManager.setHeight(size.y);
//        } else {
//            CordManager.setHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
//        }
//        /////////////// string layouts  ///////////////
//        LinearLayout strummingLayout = (LinearLayout) findViewById(R.id.guitarLayout);
//        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(layouts);
//        strummingLayout.setOnTouchListener(activitySwipeDetector);
//
//
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(mBroadcastStringAction);
//        final Intent intent = new Intent(MainActivity.this, HelloIOIOService2.class);
//        startService(intent);
//    }
//
//
//
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        int[] lastSarig = {-1,-1,-1,-1,-1,-1};
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(mBroadcastStringAction)) {
//
//                for (int i = 0; i <= 5; i++){
//                    String strM = "meitar" + Integer.toString(i);
//                    String strS = "srigim" + Integer.toString(i);
//                    String strB = "Velbridge" + Integer.toString(i);
//                    retMeitar[i] = intent.getFloatExtra(strM, 0);
//                    retSrigim[i] = intent.getIntExtra(strS, -1);
//                    retVelBridge = intent.getFloatExtra(strB, 0);
//                    Log.e("____retVel____", Float.toString(retVelBridge));
//                    if (retVelBridge != 0f & lastSarig[i] != retSrigim[i]){
//                        CordManager.restartTask(i, 1, retVelBridge*10000, retSrigim[i]);
//                    }
//                    lastSarig[i] = retSrigim[i];
//                }
//            }
//        }

    @Override
    public void onResume() {
        super.onResume();
//        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        cordManager.cancelAllTasks();
//        unregisterReceiver(mReceiver);
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