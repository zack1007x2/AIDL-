package com.example.zack.myserviceaidl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Zack on 15/5/18.
 */
public class BootCompletedReciever extends BroadcastReceiver {

    final static String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.w("Zack", "starting service...");
        context.startService(new Intent("com.example.zack.weatherservice.MyWeatherservice"));
    }

}
