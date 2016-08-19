package ioio.examples.hello_service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class HelloIOIOService2 extends IOIOService {
    private String LOG_TAG = null;

    private int[] AnalogPinsArry = {31,32,33,34,35,36,37,38,39,40,41,42};
    private int[] DigitalsPinsArry = {22,21,20,19,18,17,16,15,14,13,12,11};

    private AnalogInput[] AnalogInputObjects = new AnalogInput[12];
    private DigitalOutput[] DigitalOutputObjects = new DigitalOutput[12];

    private float[] SensorValue = new float[12];

    @Override
    public void onCreate() {
        super.onCreate();
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new BaseIOIOLooper() {

            ArrayList<Float>[] z = (ArrayList<Float>[])new ArrayList[6];
            public  int lastX = -1;
            public float lastPress = 0f;

            @Override
            protected void setup() throws ConnectionLostException,
                    InterruptedException {


                for (int k = 11; k >= 0; k--){
                    DigitalOutputObjects[k] = ioio_.openDigitalOutput(DigitalsPinsArry[k]);
                }

                for (int j = 0; j < 12; j++){
                    AnalogInputObjects[j] = ioio_.openAnalogInput(AnalogPinsArry[j]);
                }

//                for(int i = 0; i <= 5; i++){
//                    z[i] = new ArrayList<Float>();
//                }

            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {

                Intent broadcastIntent = new Intent();
                // i - x, n - y
                for (int i = 0; i <= 11; i++){
                    DigitalOutputObjects[i].write(true);

                    for (int n = 11; n >= 0; n--){
                        SensorValue[n] = AnalogInputObjects[n].read();
                        if(SensorValue[n] > 0.065 & n >= 0 & n <= 5){
//                            String msg = "n: " + n + "  i: " + i + "    sen:   " + SensorValue[n];
//                            Log.e("SensorValue:         ",Float.toString(SensorValue[n]));
                            broadcastIntent.putExtra("meitar" + n, SensorValue[n]);
                            broadcastIntent.putExtra("srigim" + n, i);
                            broadcastIntent.putExtra("Velbridge" + n, playBridge2(SensorValue[n]));
                        }
                        Thread.sleep(1);
                        SensorValue[n] = 0;
                    }
                    DigitalOutputObjects[i].write(false);
                }
                broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
                sendBroadcast(broadcastIntent);

            }
            public float playBridge2(float press){
                Log.e("p", "press:  " + press +"    lastPress:  "+ lastPress);
                if (press > lastPress + 0.1){
                    lastPress = press;
                    return press;
                }
                else{
                    lastPress = press;
                    return 0;
                }

            }


//            public float playBridge(int meitar, int sarig, float pressure) {
//                int counter = 0;
//
//                // if the pressure is in the same x and y collact the z
//                if (lastX == sarig) {
//                    if (lastPress > pressure + 0.00001) {
//                        lastPress = 0f;
////                        Log.e("big press","2");
//                        lastX = -1;
//                        return 1f;
//                    }
//                    if (lastPress > pressure + 0.0001) {
////                        Log.e("small press","3");
//                        counter++;
//                    } else {
////                        Log.e("not enough press","4");
//                        counter = 0;
//                    }
//
//                    if (counter == 5) {
////                        Log.e("counter at 5","5");
//                        lastPress = 0f;
//                        lastX = -1;
//                        return pressure;
//                    }
//                    lastPress = pressure;
//                    lastX = sarig;
//                }
//                else {
////                    Log.e("different sarig","6");
//                    lastPress = 0f;
//                    lastX = sarig;
//                }
//                Log.e("r","counter: "+Integer.toString(counter)+" lastPress: "+Float.toString(lastPress) +
//                " press: " + Float.toString(pressure) + " lastX: " + Integer.toString(lastX)+ " sarig: " + Integer.toString(sarig));
//                return 0;
//            }

//                // if z have 5 values check the time
//                if (z[meitar].size() == 5) {
//                    velo = 0f;
//                    for (int i=1;i<=4;i++){
//                        velo += z[meitar].indexOf(i);
//                    }
//                    velo = (float)velo / 5;
//                }
//                velo = (float)(velo / 0.5);
//                return (velo);
//                }
//                return 0f; //stam
//            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        return result;


    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(LOG_TAG, "In onBind");
        return null;
    }
}