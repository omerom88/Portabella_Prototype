package ioio.examples.hello_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ioio.examples.hello_service.GuitarActivity.ChooseTheme;

/**
 * Created by omerrom on 17/09/16.
 */
public class MenuActivityGif extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout_gif);

        final View menuView = findViewById(R.id.menu_layout);
        Button metroBut = (Button) findViewById(R.id.stop);
        Button settingBut = (Button) findViewById(R.id.pauseButton);
        Button themeBut = (Button) findViewById(R.id.playButton);

        themeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.setBackground(getResources().getDrawable(R.drawable.menuscreencolor03));
                Intent intent = new Intent(MenuActivityGif.this, ChooseTheme.class);
                Log.e("setOnTouchListener: ", "ACTION_UP");
                int res = 2;
                startActivityForResult(intent, res);
                finish();
            }
        });

//<<<<<<< HEAD
//        metroBut.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getActionMasked()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        menuView.setBackground(getResources().getDrawable(R.drawable.menuscreenrecording01));
//                        try {
//                            Thread.sleep(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    case MotionEvent.ACTION_UP:{
//                        Intent intent = new Intent(MenuActivityGif.this, MetronomActivity.class);
//                        int res = 2;
//                        startActivityForResult(intent,res);
//                        finish();
//                    }
//                }
//                return true;
//=======
        metroBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.setBackground(getResources().getDrawable(R.drawable.menuscreenrecording01));
                Intent intent = new Intent(MenuActivityGif.this, MetronomActivity.class);
                int res = 2;
                startActivityForResult(intent,res);
                finish();

            }
        });

        settingBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.setBackground(getResources().getDrawable(R.drawable.menuscreensettings02));
                Log.e("setOnClickListener: ", "settingBut");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult: ", "Finish");
        if(resultCode == Activity.RESULT_OK){
            Log.e("onActivityResult: ", "Finish");
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
