package com.example.zack.myserviceaidl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Zack on 15/5/18.
 */
public class MyWeatherService extends Service {

    private String responseStr;
    HashMap iconsMap = new HashMap();
    private NotificationManager mNotificationManager;
    private Notification notification;
    private final int notifyID = 0;
    private String weather;
    private SensorManager sensorManager;
    private long lastUpdate,preUpdate;
    private SensorEventListener listen;
    private float senX,senY,senZ;
    private int state;

    @Override
    public IBinder onBind(Intent intent) {
        //禁用bind service
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accel!=null)
            sensorManager.registerListener(listen, accel, SensorManager.SENSOR_DELAY_UI);
        else
            Log.d("Zack", "SENSOR is null");


        listen = new SensorListen();
//        if (isOnline()) {
//            accessWebService();
//        } else {
//            notification = new Notification.Builder(getApplicationContext())
//                    .setContentTitle("No Internet")
//                    .build();
//        }
        //重啟時重新啟動service
        return START_STICKY;
    }

    public void onCreate() {
        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accel!=null)
            sensorManager.registerListener(listen, accel, SensorManager.SENSOR_DELAY_FASTEST);
        else
            Log.d("Zack", "SENSOR is null");


        listen = new SensorListen();
//        notifyID = notifyinfo.getInt("notifyID", 0);
        Log.d("Zack", "onCreate");
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
//        notification = new Notification.Builder(getApplicationContext())
//                .setContentTitle("Welcome to Weather APP")
//                .setContentText("Please wait for update")
//                .build();
//        mNotificationManager.notify(notifyID, notification);
//        startForeground(notifyID, notification);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);



    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                Log.d("Zack", "onRecieve");
                Intent intentStart = new Intent(context, MyWeatherService.class);
                context.startService(intentStart);
            }
        }
    };



    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub(){
        @Override
        public void echoHello() throws RemoteException {
            notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Welcome to Weather APP")
                .setContentText("Please wait for update")
                .build();
             mNotificationManager.notify(1, notification);
        }

        @Override
        public String getWeather() throws RemoteException {
            return weather;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };

    public void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(listen);
//        unregisterReceiver(receiver);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.zack.myserviceaidl", "com.example" +
                ".zack.myserviceaidl.MyWeatherService"));
        startService(intent);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        senX = values[0];
        senY = values[1];
        senZ = values[2];
        Log.d("Zack","X = "+senX+"Y = "+senY+"Z = "+senZ);

        if(Math.abs(senY)>8){
            state = 1;
            preUpdate = System.currentTimeMillis();
        }
        if(Math.abs(senX)>8){
            state=2;
            lastUpdate = System.currentTimeMillis();
        }

        if(Math.abs(preUpdate-lastUpdate)<2000 && Math.abs(senZ)<2&& state==2){
            Log.d("Zack","STARTACTIVITY");

            preUpdate=0;
            lastUpdate=0;
            senX=0;
            senY=0;
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.zack.myserviceaidl", "com.example" +
                    ".zack.myserviceaidl.main"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }

    public class SensorListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                getAccelerometer(event);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }
}
