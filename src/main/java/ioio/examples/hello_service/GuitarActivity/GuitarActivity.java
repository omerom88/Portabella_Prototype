package ioio.examples.hello_service.GuitarActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import ioio.examples.hello_service.AnimationClass;
import ioio.examples.hello_service.HelloIOIOService2;
import ioio.examples.hello_service.MenuActivityGif;
import ioio.examples.hello_service.R;
import ioio.examples.hello_service.Recording.RecordPlayerActivity;

/**
 * Created by Tomer on 21/08/2016.
 */
public class GuitarActivity extends Activity {

    public static float[] retMeitar = {0f,0f,0f,0f,0f,0f};
    public static int[] retSrigim = {-1,-1,-1,-1,-1,-1};
    public static LinearLayout baseGuitarLayout;
    public static File recordOutput;

    private IntentFilter mIntentFilter;
    private String LOG_TAG = null;
    private static String fileName;
    private static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    private static AnimationClass animationDrawableStartRec;
    private static AnimationClass animationDrawableMenu;

    public static final int[] NOTES_LAYOUTS = {R.id.E_LOW, R.id.A, R.id.D, R.id.G, R.id.B, R.id.E_HIGH};
    public static final int[] ROCK_NOTES = {R.raw.e_string_low, R.raw.a_string, R.raw.d_string,
            R.raw.gstring, R.raw.b_string, R.raw.e_string_hi};
    public static final int[] REG_NOTES = {R.raw.estringlow, R.raw.astring, R.raw.dstring,
            R.raw.gstring, R.raw.bstring, R.raw.estringhi};
    public static final int[] BLUES_NOTES = {R.raw.elowstringblues, R.raw.astringblues, R.raw.dstringblues,
            R.raw.gstringblues, R.raw.bstringblues, R.raw.ehighstringblues};
    public static int[] initNotes = REG_NOTES;
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
        baseGuitarLayout = (LinearLayout) findViewById(R.id.guitarLayout);

        LOG_TAG = this.getClass().getSimpleName();
        LinearLayout[] layouts = new LinearLayout[CordManager.NUM_OF_MEITARS];
            for (int i = 0; i < layouts.length; i++) {
            layouts[i] = (LinearLayout) findViewById(NOTES_LAYOUTS[i]);
        }
        //////////////// the gesture  /////////////////
        CordManager.init(this, getApplicationContext(), initNotes);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            CordManager.setWidth(size.x);
        } else {
            CordManager.setWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
        }
        /////////////// string layouts  ///////////////

        final ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(layouts, this);
        baseGuitarLayout.setOnTouchListener(activitySwipeDetector);


        // rec button animation
        final ImageView mImageViewRecording = (ImageView) findViewById(R.id.imageview_animated_recording);
        animationDrawableStartRec = new AnimationClass((AnimationDrawable)mImageViewRecording.getBackground());
        mImageViewRecording.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.e("START ACTION_DOWN", "!!");
                        if (!CordManager.isRecording()) {
                            animationDrawableStartRec.start();
                            animationDrawableStartRec.checkIfAnimationDone();
                            return true;
                        }
                    }
                    case MotionEvent.ACTION_UP: {
                        if (!CordManager.isRecording()) {
                            if (animationDrawableStartRec.isAnimationFlag()) {
                                animationDrawableStartRec.setAnimationFlag(false);
                                CordManager.startRecording();
                            } else {
                                animationDrawableStartRec.restartAnimation();
                            }
                        } else {
                            animationDrawableStartRec.restartAnimation();
                            CordManager.stopRecording();
                            activitySwipeDetector.clear();
                            openSaveFileDialog();
                        }
                        //TODO: when leave button
                    }
                    return false;
                }
                return false;
            }
        });


        // menu button animation
        final ImageView mImageViewMenu = (ImageView) findViewById(R.id.imageview_animated_menu);
        animationDrawableMenu = new AnimationClass((AnimationDrawable)mImageViewMenu.getBackground());
        mImageViewMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.e("START ACTION_DOWN", "!!");
                        animationDrawableMenu.start();
                        animationDrawableMenu.checkIfAnimationDone();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        Log.e("START ACTION_UP", "!!");
                        if (animationDrawableMenu.isAnimationFlag()) {
                            Log.e("START isAnimationFlag", "!!");
                            animationDrawableMenu.setAnimationFlag(false);
                            Intent intent = new Intent(GuitarActivity.this, MenuActivityGif.class);
                            int res = 2;
                            startActivityForResult(intent, res);
                        }
                        animationDrawableMenu.restartAnimation();
                    }
                    return false;
                }
                return false;
            }
        });

        //////// start ioio activity //////////
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        final Intent intent = new Intent(GuitarActivity.this, HelloIOIOService2.class);
        startService(intent);
    }

    public void openSaveFileDialog() {
        CordManager.pauseAllTasks();
        Log.e("Screen Orientation: ", "" + getResources().getConfiguration().orientation);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to name your new recording?");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileName = input.getText().toString();
                recordOutput = CordManager.saveFile(fileName);
                openPlaySongDialog(fileName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CordManager.cancelRecord();
                dialog.cancel();
            }
        });

//        builder.show();


        Dialog dialog = builder.setView(new View(this)).create();
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void openPlaySongDialog(final String recordName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to play it now?");
        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GuitarActivity.this, RecordPlayerActivity.class);
                intent.putExtra("recordName", recordName);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
                    float retVelBridge = intent.getFloatExtra(strB, 0);
//                    Log.e("____retVel____", Float.toString(retVelBridge));
                    if (retVelBridge != 0f & lastSarig[i] != retSrigim[i]){
                        CordManager.restartTask(i, 1, retVelBridge *10000, retSrigim[i]);
                    }
                    lastSarig[i] = retSrigim[i];
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult: ", "" + requestCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
        animationDrawableMenu.selectDrawable(0);
        animationDrawableStartRec.selectDrawable(0);
//        animationDrawableMenu.setVisible(false, false);
//        animationDrawableStartRec.setVisible(false, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CordManager.pauseAllTasks();
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