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
            sensorManager.registerListener(listen, accel, SensorManager.SENSOR_DELAY_UI);
        else
            Log.d("Zack", "SENSOR is null");


        listen = new SensorListen();
//        SharedPreferences notifyinfo = this.getSharedPreferences("notifyinfo", 0);
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

//        iconsMap.put("01d", R.drawable.a01d);
//        iconsMap.put("01n", R.drawable.a01n);
//        iconsMap.put("02d", R.drawable.a02d);
//        iconsMap.put("02n", R.drawable.a02n);
//        iconsMap.put("03d", R.drawable.a03d);
//        iconsMap.put("03n", R.drawable.a03n);
//        iconsMap.put("04d", R.drawable.a04d);
//        iconsMap.put("04n", R.drawable.a04n);
//        iconsMap.put("09d", R.drawable.a09d);
//        iconsMap.put("09n", R.drawable.a09n);
//        iconsMap.put("10d", R.drawable.a10d);
//        iconsMap.put("10n", R.drawable.a10n);
//        iconsMap.put("11d", R.drawable.a11d);
//        iconsMap.put("11n", R.drawable.a11n);
//        iconsMap.put("13d", R.drawable.a13d);
//        iconsMap.put("13n", R.drawable.a13n);
//        iconsMap.put("50d", R.drawable.a50d);
//        iconsMap.put("50n", R.drawable.a50n);


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

//    private class JsonReadTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            HttpClient httpclient = new DefaultHttpClient();
//            String CITY = "Taipei";
//            HttpGet httppost = new HttpGet(params[0] + "?q=" + CITY);
//            Log.v("Zack", params[0] + "?q=" + CITY);
//            try {
//                HttpResponse response = httpclient.execute(httppost);
//                String responseTXT = inputStreamToString(
//                        response.getEntity().getContent()).toString();
//                System.out.println(responseTXT);
//
//                responseStr = responseTXT;
//
//                System.out.println(responseStr);
//                JsonParse();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//        }
//
//        private StringBuilder inputStreamToString(InputStream is) {
//            String rLine;
//            StringBuilder answer = new StringBuilder();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//
//            try {
//                while ((rLine = rd.readLine()) != null) {
//                    answer.append(rLine);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return answer;
//        }
//
//    }
//
//    public void accessWebService() {
//        JsonReadTask task = new JsonReadTask();
//        String url = "http://api.openweathermap.org/data/2.5/weather";
//        task.execute(url);
//
//        try {
//            task.get();
//        } catch (Exception e) {
//            Log.e("Service", e.toString());
//        }
//
//    }
//
//    public void JsonParse() {
//
//        try {
////            SharedPreferences notifyinfo = this.getSharedPreferences("notifyinfo", 0);
////            notifyID = notifyinfo.getInt("notifyID", 0);
////            notifyID++;
//            Log.d("Zack", responseStr);
//            JSONObject jsonResponse = new JSONObject(responseStr);
//            JSONArray weatherdetail = jsonResponse.getJSONArray("weather");
//            JSONObject tempdetail = jsonResponse.getJSONObject("main");
//            weather = weatherdetail.getJSONObject(0).getString("main");
////            String description = weatherdetail.getJSONObject(0).getString("description");
//            String icon = weatherdetail.getJSONObject(0).getString("icon");
//            double temp = tempdetail.getDouble("temp");
//            Log.d("Zack", "weather = " + weather + "temp = " + (temp - 273.15) + "icon = " + icon);
//            Log.d("Zack", "icon = " + iconsMap.get(icon));
//            mNotificationManager.cancelAll();
//            notification = new Notification.Builder(getApplicationContext())
//                    .setContentTitle("Taipei")
//                    .setContentText(weather + " ,  temp = " + ((int) (temp - 273.15) * 100) / 100 + "℃")
//                    .setSmallIcon((Integer) iconsMap.get(icon))
//                    .build();
//            mNotificationManager.notify(notifyID, notification);
////            SharedPreferences example=getSharedPreferences("notifyinfo",0);
////            Editor editor = example.edit();
////            editor.putInt("notifyID",notifyID);
////            editor.apply();
//        } catch (JSONException e) {
//            Log.d("JSON Parse ERROR", e.toString());
//        }
//    }
//
//    public boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//    }


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

        if(Math.abs(preUpdate-lastUpdate)<2000 && Math.abs(senZ)<1&& state==2){
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
