package ioio.examples.hello_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import ioio.examples.hello_service.GuitarActivity.GuitarActivity;
import ioio.examples.hello_service.Recording.RecordPlayerActivity;

public class MainActivity extends Activity {

    private static final int LOADING_REQUEST_CODE = 123456789;
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    SlidingMenu s;

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

        Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        startActivityForResult(intent, LOADING_REQUEST_CODE);
//        final ImageView mImageViewMoving = (ImageView) findViewById(R.id.imageview_animated_moving);
//        mImageViewMoving.setVisibility(View.INVISIBLE);
//        final ImageView mImageViewOpening = (ImageView) findViewById(R.id.imageview_animated_opening);
//        AnimationDrawable animationDrawableOpen = (AnimationDrawable)mImageViewOpening.getBackground();
//        AnimationDrawable animationDrawableMove = (AnimationDrawable)mImageViewMoving.getBackground();
//        animationDrawableOpen.start();
//        checkIfAnimationOpenDone(animationDrawableOpen, animationDrawableMove, mImageViewMoving);

    }
//    private void checkIfAnimationOpenDone(AnimationDrawable anim, final AnimationDrawable animationDrawableMove, final ImageView mImageViewMoving){
//        final AnimationDrawable a = anim;
////        final AnimationDrawable b = animationDrawableMove;
//        int timeBetweenChecks = 300;
//        Handler h = new Handler();
//        h.postDelayed(new Runnable() {
//            public void run() {
//                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
//                    checkIfAnimationOpenDone(a, animationDrawableMove, mImageViewMoving);
//                } else {
////                    Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
//                    mImageViewMoving.setVisibility(View.VISIBLE);
//                    animationDrawableMove.start();
//                    checkIfAnimationMoveDone(animationDrawableMove);
//                }
//            }
//        }, timeBetweenChecks);
//    }
////    private void getOutOfit(){
////        while (loadingMoveCounter)
////    }
//
//    private void checkIfAnimationMoveDone(AnimationDrawable anim){
//
//        final AnimationDrawable a = anim;
//        int timeBetweenChecks = 300;
//        Handler h = new Handler();
//        h.postDelayed(new Runnable(){
//            public void run(){
////                Toast.makeText(getApplicationContext(), "COUNTER: " + loadingMoveCounter, Toast.LENGTH_SHORT).show();
//                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
//                    checkIfAnimationMoveDone(a);
//                } else{
//                    Intent intent = new Intent(MainActivity.this, GuitarActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }, timeBetweenChecks);
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this, GuitarActivity.class);
        startActivity(intent);
    }

    public void playGuitar(View view) {
        Intent intent = new Intent(this, GuitarActivity.class);
        startActivity(intent);
    }

    public void startRecActivity(View view) {
        Intent intent = new Intent(this, RecordPlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

//        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        cordManager.pauseAllTasks();
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