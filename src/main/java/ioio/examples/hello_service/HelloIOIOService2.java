package ioio.examples.hello_service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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


            @Override
            protected void setup() throws ConnectionLostException,
                    InterruptedException {


                for (int k = 11; k >= 0; k--){
                    DigitalOutputObjects[k] = ioio_.openDigitalOutput(DigitalsPinsArry[k]);
                }

                for (int j = 0; j < 12; j++){
                    AnalogInputObjects[j] = ioio_.openAnalogInput(AnalogPinsArry[j]);
                }

            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {

                Intent broadcastIntent = new Intent();
                // i - x, n - y
                for (int i = 0; i <= 11; i++){
                    DigitalOutputObjects[i].write(true);

                    for (int n = 11; n >= 0; n--){
                        SensorValue[n] = AnalogInputObjects[n].read();
                        if(SensorValue[n] > 0.1 & n >= 0 & n <= 5){
//                            String msg = "n: " + n + "  i: " + i + "    sen:   " + SensorValue[n];
//                            Log.i(LOG_TAG,msg);
                            broadcastIntent.putExtra("meitar" + n, SensorValue[n]);
                            broadcastIntent.putExtra("srigim"+ n, i);
                        }
                        Thread.sleep(1);
                        SensorValue[n] = 0;
                    }
                    DigitalOutputObjects[i].write(false);
                }
                broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
                sendBroadcast(broadcastIntent);

            }
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
