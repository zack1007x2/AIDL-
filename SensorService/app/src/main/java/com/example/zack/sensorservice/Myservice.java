package com.example.zack.sensorservice;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Zack on 15/5/22.
 */
public class Myservice extends Service {
    private SensorManager sensorManager;
    private long lastUpdate,preUpdate;
    private SensorEventListener listen;
    private float senX,senY,senZ;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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


        return START_STICKY;
    }

    public void onCreate() {
        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accel!=null)
            sensorManager.registerListener(listen, accel, SensorManager.SENSOR_DELAY_UI);
        else
            Log.d("Zack", "SENSOR is null");


        listen = new SensorListen();
    }
    public void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(listen);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        senX = values[0];
        senY = values[1];
        senZ = values[2];
        Log.d("Zack","X = "+senX+"Y = "+senY+"Z = "+senZ);

        if(Math.abs(senY)>8){
            preUpdate = System.currentTimeMillis();
        }
        if(Math.abs(senX)>8){
            lastUpdate = System.currentTimeMillis();
        }

        if(Math.abs(preUpdate-lastUpdate)<2000 && Math.abs(senZ)<1){
            Log.d("Zack","STARTACTIVITY");
            preUpdate=0;
            lastUpdate=0;
            senX=0;
            senY=0;
            Intent startAct = new Intent();
            startAct.setClass(this,MainActivity.class);
            startAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(startAct);
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
