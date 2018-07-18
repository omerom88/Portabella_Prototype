package com.portabella.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.portabella.app.GuitarActivity.GuitarActivity;


public class MainActivity extends Activity {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        int LOADING_REQUEST_CODE = 123456789;
        startActivityForResult(intent, LOADING_REQUEST_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Intent intent = new Intent(MainActivity.this, GuitarActivity.class);
                int GUITAR_REQUEST_CODE = 7436862;
                startActivityForResult(intent, GUITAR_REQUEST_CODE);
                break;

            case RESULT_CANCELED:
                finish();
        }
    }
}