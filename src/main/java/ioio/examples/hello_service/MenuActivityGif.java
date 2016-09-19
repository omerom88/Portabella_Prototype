package ioio.examples.hello_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ioio.examples.hello_service.GuitarActivity.ChooseTheme;

/**
 * Created by omerrom on 17/09/16.
 */
public class MenuActivityGif extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout_gif);

        final View menuView = findViewById(R.id.menu_layout);
        Button recBut = (Button)findViewById(R.id.button);
        Button settingBut = (Button)findViewById(R.id.button2);
        Button themeBut = (Button)findViewById(R.id.button3);

        themeBut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        menuView.setBackground(getResources().getDrawable(R.drawable.menuscreencolor03));
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    case MotionEvent.ACTION_UP:{
                        Intent intent = new Intent(MenuActivityGif.this, ChooseTheme.class);
                        int res = 2;
                        startActivityForResult(intent,res);
                        finish();
                        ////////    TODO: open a fregment with themes!!

                    }
                }
            return true;
            }

        });

        recBut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        menuView.setBackground(getResources().getDrawable(R.drawable.menuscreenrecording01));
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    case MotionEvent.ACTION_UP:{

                    }
                }
                return true;
            }

        });

        settingBut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        menuView.setBackground(getResources().getDrawable(R.drawable.menuscreensettings02));
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    case MotionEvent.ACTION_UP: {
//                        Intent intent = new Intent(MenuActivityGif.this, ChooseTheme.class);
//                        startActivity(intent);

                    }
                }
                return true;
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            finish();
        }
    }

    @Override
    protected void onStop() {
        setResult(2);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        setResult(2);
        super.onDestroy();
    }
}
