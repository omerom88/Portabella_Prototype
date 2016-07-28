package ioio.examples.hello_service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class HelloIOIOService extends IOIOService {
    private String LOG_TAG = null;
    private float float_L46;
    private float float_R45;
    private float float_L44;
    private float float_R43;
    private float float_L42;
    private float float_R41;
    private float float_L40;
    private float float_R39;
    private float float_L38;
    private float float_R37;
    private float float_L36;
    private float float_R35;

    @Override
    public void onCreate() {
        super.onCreate();
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
    }

    @Override
	protected IOIOLooper createIOIOLooper() {
		return new BaseIOIOLooper() {
			private AnalogInput in46;
            private AnalogInput in45;
            private AnalogInput in44;
            private AnalogInput in43;
            private AnalogInput in42;
            private AnalogInput in41;
            private AnalogInput in40;
            private AnalogInput in39;
            private AnalogInput in38;
            private AnalogInput in37;
            private AnalogInput in36;
            private AnalogInput in35;


			@Override
			protected void setup() throws ConnectionLostException,
					InterruptedException {
                in46 = ioio_.openAnalogInput(46);
                in45 = ioio_.openAnalogInput(45);
                in44 = ioio_.openAnalogInput(44);
                in43 = ioio_.openAnalogInput(43);
                in42 = ioio_.openAnalogInput(42);
                in41 = ioio_.openAnalogInput(41);
                in40 = ioio_.openAnalogInput(40);
                in39 = ioio_.openAnalogInput(39);
                in38 = ioio_.openAnalogInput(38);
                in37 = ioio_.openAnalogInput(37);
                in36 = ioio_.openAnalogInput(36);
                in35 = ioio_.openAnalogInput(35);

			}

			@Override
			public void loop() throws ConnectionLostException, InterruptedException {

                float temp46 = in46.read();
                float_L46 = temp46*20/(1-temp46);
                float temp45 = in45.read();
                float_R45 = temp45*20/(1-temp45);
                float presure0 = 2 - temp46 - temp45;

                float temp44 = in44.read();
                float_L44 = temp44 * 20 / (1 - temp44);
                float temp43 = in43.read();
                float_R43 = temp43*20/(1-temp43);
                float presure1 = 2 - temp44 - temp43;

                float temp42 = in42.read();
                float_L42 = temp42 * 20 / (1 - temp42);
                float temp41 = in41.read();
                float_R41 = temp41*20/(1-temp41);
                float presure2 = 2 - temp42 - temp41;

                float temp40 = in40.read();
                float_L40 = temp40 * 20 / (1 - temp40);
                float temp39 = in39.read();
                float_R39 = temp39*20/(1-temp39);
                float presure3 = 2 - temp40 - temp39;

                float temp38 = in38.read();
                float_L38 = temp38 * 20 / (1 - temp38);
                float temp37 = in37.read();
                float_R37 = temp37*20/(1-temp37);
                float presure4 = 2 - temp38 - temp37;

                float temp36 = in36.read();
                float_L36 = temp36 * 20 / (1 - temp36);
                float temp35 = in35.read();
                float_R35 = temp35*20/(1-temp35);
                float presure5 = 2 - temp36 - temp35;


                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ioioActivity.mBroadcastStringAction);
                if (presure0 > 0.05) {
//                    broadcastIntent.putExtra("float_L46", float_L46);
//                    broadcastIntent.putExtra("float_R45", float_R45);
                    broadcastIntent.putExtra("presure0", presure0);
                }

                else if (presure1 > 0.05) {
//                    broadcastIntent.putExtra("float_L44", Float.toString(float_L44));
//                    broadcastIntent.putExtra("float_R43", Float.toString(float_R43));
                    broadcastIntent.putExtra("presure1", presure1);
                }

                else if (presure2 > 0.05) {
//                    broadcastIntent.putExtra("float_L42", Float.toString(float_L42));
//                    broadcastIntent.putExtra("float_R41", Float.toString(float_R41));
                    broadcastIntent.putExtra("presure2", presure2);
                }

                else if (presure3 > 0.05) {
//                    broadcastIntent.putExtra("float_L40", Float.toString(float_L40));
//                    broadcastIntent.putExtra("float_R39", Float.toString(float_R39));
                    broadcastIntent.putExtra("presure3", presure3);
                }

                else if (presure4 > 0.05) {
//                    broadcastIntent.putExtra("float_L38", Float.toString(float_L38));
//                    broadcastIntent.putExtra("float_R37", Float.toString(float_R37));
                    broadcastIntent.putExtra("presure4", presure4);
                }
//                if (presure5 > 0.1) {
//                    broadcastIntent.putExtra("float_L36", Float.toString(float_L36));
//                    broadcastIntent.putExtra("float_R35", Float.toString(float_R35));
//                    broadcastIntent.putExtra("presure5", Float.toString(presure5));
//                }
                sendBroadcast(broadcastIntent);


//                Intent broadcastIntent = new Intent();
//                broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
//                String temp = "EMPTY";
//                if (floatTxt != null)
//                    temp = Float.toString(floatTxt);
//                broadcastIntent.putExtra("Data46", temp);
//                sendBroadcast(broadcastIntent);
            }
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int result = super.onStartCommand(intent, flags, startId);
//		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (intent != null && intent.getAction() != null
//				&& intent.getAction().equals("stop")) {
//			// User clicked the notification. Need to stop the service.
//			nm.cancel(0);
//			stopSelf();
//		} else {
//			// Service starting. Create a notification.
//			Notification notification = new Notification(
//					R.drawable.ic_launcher, "IOIO service running",
//					System.currentTimeMillis());
//			    notification
//					.setLatestEventInfo(this, "IOIO Service", "Click to stop",
//							PendingIntent.getService(this, 0, new Intent(
//                                    "stop", null, this, this.getClass()), 0));
//			notification.flags |= Notification.FLAG_ONGOING_EVENT;
//			nm.notify(0, notification);
//		}
		return result;


    }

	@Override
	public IBinder onBind(Intent arg0) {
        Log.i(LOG_TAG, "In onBind");
        return null;
	}
}
