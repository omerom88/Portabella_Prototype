package ioio.examples.hello_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by omerrom on 27/07/16.
 */
public class ioioBrodcastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,HelloIOIOService.class);
        context.startService(intent1);
    }
}
