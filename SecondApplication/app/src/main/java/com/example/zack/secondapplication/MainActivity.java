package com.example.zack.secondapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.example.zack.myserviceaidl.IMyAidlInterface;


public class MainActivity extends Activity implements ServiceConnection {
    private String weather;
    private TextView tvHello;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHello = (TextView)findViewById(R.id.tvHello);
        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.example.zack.myserviceaidl", "com.example.zack.myserviceaidl.MyWeatherService"));
        Log.d("Zack","bindService");
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

    }

    private IMyAidlInterface mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("Zack", "connect service");
            mService = IMyAidlInterface.Stub.asInterface(service);
            try {
                mService.echoHello();
                tvHello.setText(mService.getWeather());
            } catch (RemoteException e) {
                Log.d("Zack", "RemoteException");
            }
        }


        public void onServiceDisconnected(ComponentName className) {
            Log.d("Zack","disconnect service");
            mService = null;
        }
    };

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
