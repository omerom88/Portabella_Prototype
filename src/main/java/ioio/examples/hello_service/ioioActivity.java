package ioio.examples.hello_service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by omerrom on 28/07/16.
 */
public class ioioActivity extends Activity {

    private IntentFilter mIntentFilter;
    private TextView txtL;
    private TextView txtR;
    private TextView txtM;
    private TextView valL;
    private TextView valR;
    private TextView valM;
    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    private float lastVar = 0;
    private String LOG_TAG = null;

    private float retPresure;
    private float retleftY;
    private float retleftX;


//    public ioioActivity(Context context){
//        this.retPresure = 0;
//        this.retleftY = 0;
//        this.retleftX = 0;
//        startIoioListener();
//
//    }
//
//private void startIoioListener() {
//    mIntentFilter = new IntentFilter();
//    mIntentFilter.addAction(mBroadcastStringAction);
//    Intent serviceIntent = new Intent(, HelloIOIOService.class);
//    startService(serviceIntent);
//}



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ioio_ne);
        LOG_TAG = this.getClass().getSimpleName();


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
        final Intent intent = new Intent(ioioActivity.this, HelloIOIOService.class);
        startService(intent);

    }

    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

//        Button but = (Button)findViewById(R.id.button);
//        but.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(intent.getStringExtra("Key"));
//            }
//        });
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastStringAction)) {
                float temp0 = intent.getFloatExtra("presure0", 0);
                float temp1 = intent.getFloatExtra("presure1", 0);
                float temp2 = intent.getFloatExtra("presure2", 0);
                float temp3 = intent.getFloatExtra("presure3", 0);
                float temp4 = intent.getFloatExtra("presure4", 0);
                float temp5 = intent.getFloatExtra("presure5", 0);

                if (temp0 != 0) {
                    retleftX = 6;
                    retPresure = temp0;

                    Log.i(LOG_TAG, "retPresure:   " + Float.toString(retPresure));
                }
//                    float temp = intent.getFloatExtra("float_L46", 0);
//                    float temp2 = intent.getFloatExtra("float_R45", 0);
//                    Log.i(LOG_TAG, "DIV:   " + Float.toString(temp2 / temp));
//                    Log.i(LOG_TAG, "                                ");
//                    //Log.i(LOG_TAG, "RIGHT:   " + Float.toString(temp2));
//
//                    if (45 < temp && temp < 70) {
//                        retleftY = temp - 60f;
//                        lastVar = temp - 60f;
//                    } else {
//                        retleftY = lastVar;
//                    }


//                    valL.setText("DIV:   " + Float.toString(temp2 / temp));
//                    //Log.i(LOG_TAG, "retleftY:   " + Float.toString(retleftX));
//                    valM.setText("presure0:   " + Float.toString(retPresure));
//                    //Log.i(LOG_TAG, "presure0:   " + Float.toString(retPresure));
//                    valR.setText("retleftY:   " + Float.toString(retleftY));
//                    //Log.i(LOG_TAG, "retleftY:   " + Float.toString(retleftY));
//                }

                else if (temp1 != 0) {
                    retleftX = 5;
                    retPresure = temp1;
                }
//                    txtL.setText("float_L44");
//                    valL.setText(intent.getStringExtra("float_L44"));
//                    txtM.setText("presure1");
//                    valM.setText(intent.getStringExtra("presure1"));
//                    txtR.setText("float_R43");
//                    valR.setText(intent.getStringExtra("float_R43"));
//                }

                else if (temp2 != 0) {
                    retleftX = 4;
                    retPresure = temp2;
                }
//                    txtL.setText("float_L42");
//                    valL.setText(intent.getStringExtra("float_L42"));
//                    txtM.setText("presure2");
//                    valM.setText(intent.getStringExtra("presure2"));
//                    txtR.setText("float_R41");
//                    valR.setText(intent.getStringExtra("float_R41"));
//                }
                else if (temp3 != 0) {
                    retleftX = 3;
                    retPresure = temp3;
                }
//                    txtL.setText("float_L40");
//                    valL.setText(intent.getStringExtra("float_L40"));
//                    txtM.setText("presure3");
//                    valM.setText(intent.getStringExtra("presure3"));
//                    txtR.setText("float_R39");
//                    valR.setText(intent.getStringExtra("float_R39"));
//                }
                else if (temp4 != 0) {
                    retleftX = 2;
                    retPresure = temp4;
                }
//                    txtL.setText("float_L38");
//                    valL.setText(intent.getStringExtra("float_L38"));
//                    txtM.setText("presure4");
//                    valM.setText(intent.getStringExtra("presure4"));
//                    txtR.setText("float_R37");
//                    valR.setText(intent.getStringExtra("float_R37"));
//                }
                else if (temp5 != 0) {
                    retleftX = 1;
                    retPresure = temp5;
                }
////                    txtL.setText("float_L36");
////                    valL.setText(intent.getStringExtra("float_L36"));
////                    txtM.setText("presure5");
////                    valM.setText(intent.getStringExtra("presure5"));
////                    txtR.setText("float_R35");
////                    valR.setText(intent.getStringExtra("float_R35"));
////                }
//                else
//                    txtL.setText("NO");
//                    txtM.setText("NO");
//                    txtR.setText("NO");
//            }

            }
        }
    };

    public float getPressure() {
        return retPresure;
    }

    public int getFrat() {
        return (int) retleftX;
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

}
