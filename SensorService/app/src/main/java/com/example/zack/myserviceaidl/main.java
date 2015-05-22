package com.example.zack.myserviceaidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Zack on 15/5/22.
 */
public class main extends Activity {


    private TextView tvUnlock, tvx, tvy, tvz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.main);
        tvUnlock = (TextView) findViewById(R.id.tvUnlock);
        tvx = (TextView) findViewById(R.id.tvx);
        tvy = (TextView) findViewById(R.id.tvy);
        tvz = (TextView) findViewById(R.id.tvz);
        tvUnlock.setOnClickListener(ls);
    }

    private View.OnClickListener ls = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.zack.myserviceaidl", "com.example.zack.myserviceaidl.MyWeatherService"));
            startService(intent);
//            bindService(intent, mConnection, 0);
        }

    };

    private IMyAidlInterface mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("Zack", "connect service");
            mService = IMyAidlInterface.Stub.asInterface(service);
//            try {
////                mService.echoHello();
////                tvHello.setText(mService.getWeather());
//            } catch (RemoteException e) {
//                Log.d("Zack", "RemoteException");
//            }
        }


        public void onServiceDisconnected(ComponentName className) {
            Log.d("Zack","disconnect service");
            mService = null;
        }
    };

}
