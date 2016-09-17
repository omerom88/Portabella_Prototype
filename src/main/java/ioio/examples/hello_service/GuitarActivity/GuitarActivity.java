package ioio.examples.hello_service.GuitarActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ioio.examples.hello_service.HelloIOIOService2;
import ioio.examples.hello_service.MenuActivityGif;
import ioio.examples.hello_service.R;
import ioio.examples.hello_service.Recording.Record;

/**
 * Created by Tomer on 21/08/2016.
 */
public class GuitarActivity extends Activity {

    private static final int[] BUTTONS = {R.id.E_LOW, R.id.A, R.id.D, R.id.G, R.id.B, R.id.E_HIGH};
    private static CordManager cordManager;
    private IntentFilter mIntentFilter;
    private String LOG_TAG = null;
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    public static float[] retMeitar = {0f,0f,0f,0f,0f,0f};
    public static int[] retSrigim = {-1,-1,-1,-1,-1,-1};
    public static float retVelBridge = 0f;
    public static int clickCounter = 0;


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
        setContentView(R.layout.guitar_layout);


        LOG_TAG = this.getClass().getSimpleName();
        LinearLayout[] layouts = new LinearLayout[CordManager.NUM_OF_MEITARS];
            for (int i = 0; i < layouts.length; i++) {
            layouts[i] = (LinearLayout) findViewById(BUTTONS[i]);
        }
        //////////////// the gesture  /////////////////
        CordManager.init(getApplicationContext());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            CordManager.setHeight(size.y);
        } else {
            CordManager.setHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        }
        /////////////// string layouts  ///////////////
        LinearLayout strummingLayout = (LinearLayout) findViewById(R.id.guitarLayout);
        ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(layouts);
        strummingLayout.setOnTouchListener(activitySwipeDetector);

//        ///////// menu ////////
//        final SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setFadeEnabled(true);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.activity_menu2);
//        menu.setBehindWidth(500);
//        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
//            @Override
//            public void onOpen() {
//                Button setBut = (Button)findViewById(R.id.settinButton);
//                setBut.setOnClickListener(new Button.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        menu.toggle();
//                    }
//                });
//            }
//        });
        final ImageView mImageViewSelector = (ImageView) findViewById(R.id.imageview_animated_selector);
//        mImageViewSelector.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ((AnimationDrawable) mImageViewSelector.getBackground()).start();
//                return false;
//            }
//
//        });

        mImageViewSelector.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("!!!!!!!!!!!!!", " --------");
                AnimationDrawable anim = (AnimationDrawable) mImageViewSelector.getBackground();
                anim.start();
//                while (anim.isRunning()) {}
//                final Intent intent = new Intent(GuitarActivity.this, MenuActivityGif.class);
//                startActivity(intent);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("finish:", "--");
                        final Intent intent = new Intent(GuitarActivity.this, MenuActivityGif.class);
                        startActivity(intent);
                        return false;
                    }
                });


                return false;
            }
        });

        //////// record button /////////
        final Record record = Record.getInstance(this);
//        final Button recBut = (Button)findViewById(R.id.recButton);
//        recBut.setOnClickListener(new RadioButton.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickCounter++;
//                if (clickCounter % 2 == 1){
//                    Log.e(" ", "first click");
//                    recBut.setBackgroundResource(R.drawable.roundbuttonred);
//                    //odd case - recording
//                    record.start(); //think of how to do it if the duration and stuff are defined in rec menu
//                }
//                else{
//                    Log.e(" ", "second click");
//                    recBut.setBackgroundResource(R.drawable.roundbutton);
//                    //even case - finish recording
//                    finishRec();
//                }
//            }
//        });


        //////// start ioio activity //////////
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        final Intent intent = new Intent(GuitarActivity.this, HelloIOIOService2.class);
        startService(intent);
    }

    private void finishRec(){
        //open the box that said what to do with it - save listen or share
        //to see how we can rich this function in case record time is done
        //to decide what heppend when the rec is done - default and user choice in the menu
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        int[] lastSarig = {-1,-1,-1,-1,-1,-1};
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastStringAction)) {

                for (int i = 0; i <= 5; i++){
                    String strM = "meitar" + Integer.toString(i);
                    String strS = "srigim" + Integer.toString(i);
                    String strB = "Velbridge" + Integer.toString(i);
                    retMeitar[i] = intent.getFloatExtra(strM, 0);
                    retSrigim[i] = intent.getIntExtra(strS, -1);
                    retVelBridge = intent.getFloatExtra(strB, 0);
//                    Log.e("____retVel____", Float.toString(retVelBridge));
                    if (retVelBridge != 0f & lastSarig[i] != retSrigim[i]){
                        CordManager.restartTask(i, 1, retVelBridge*10000, retSrigim[i]);
                    }
                    lastSarig[i] = retSrigim[i];
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CordManager.cancelAllTasks();
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