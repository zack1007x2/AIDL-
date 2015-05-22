package com.example.zack.myserviceaidl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    @Override
    public IBinder onBind(Intent intent) {
        //禁用bind service
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isOnline()) {
            accessWebService();
        } else {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("No Internet")
                    .build();
        }
        //重啟時重新啟動service
        return START_STICKY;
    }

    public void onCreate() {
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
        startForeground(notifyID, notification);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);

        iconsMap.put("01d", R.drawable.a01d);
        iconsMap.put("01n", R.drawable.a01n);
        iconsMap.put("02d", R.drawable.a02d);
        iconsMap.put("02n", R.drawable.a02n);
        iconsMap.put("03d", R.drawable.a03d);
        iconsMap.put("03n", R.drawable.a03n);
        iconsMap.put("04d", R.drawable.a04d);
        iconsMap.put("04n", R.drawable.a04n);
        iconsMap.put("09d", R.drawable.a09d);
        iconsMap.put("09n", R.drawable.a09n);
        iconsMap.put("10d", R.drawable.a10d);
        iconsMap.put("10n", R.drawable.a10n);
        iconsMap.put("11d", R.drawable.a11d);
        iconsMap.put("11n", R.drawable.a11n);
        iconsMap.put("13d", R.drawable.a13d);
        iconsMap.put("13n", R.drawable.a13n);
        iconsMap.put("50d", R.drawable.a50d);
        iconsMap.put("50n", R.drawable.a50n);


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

    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            String CITY = "Taipei";
            HttpGet httppost = new HttpGet(params[0] + "?q=" + CITY);
            Log.v("Zack", params[0] + "?q=" + CITY);
            try {
                HttpResponse response = httpclient.execute(httppost);
                String responseTXT = inputStreamToString(
                        response.getEntity().getContent()).toString();
                System.out.println(responseTXT);

                responseStr = responseTXT;

                System.out.println(responseStr);
                JsonParse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine;
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }

    }

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        String url = "http://api.openweathermap.org/data/2.5/weather";
        task.execute(url);

        try {
            task.get();
        } catch (Exception e) {
            Log.e("Service", e.toString());
        }

    }

    public void JsonParse() {

        try {
//            SharedPreferences notifyinfo = this.getSharedPreferences("notifyinfo", 0);
//            notifyID = notifyinfo.getInt("notifyID", 0);
//            notifyID++;
            Log.d("Zack", responseStr);
            JSONObject jsonResponse = new JSONObject(responseStr);
            JSONArray weatherdetail = jsonResponse.getJSONArray("weather");
            JSONObject tempdetail = jsonResponse.getJSONObject("main");
            weather = weatherdetail.getJSONObject(0).getString("main");
//            String description = weatherdetail.getJSONObject(0).getString("description");
            String icon = weatherdetail.getJSONObject(0).getString("icon");
            double temp = tempdetail.getDouble("temp");
            Log.d("Zack", "weather = " + weather + "temp = " + (temp - 273.15) + "icon = " + icon);
            Log.d("Zack", "icon = " + iconsMap.get(icon));
            mNotificationManager.cancelAll();
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Taipei")
                    .setContentText(weather + " ,  temp = " + ((int) (temp - 273.15) * 100) / 100 + "℃")
                    .setSmallIcon((Integer) iconsMap.get(icon))
                    .build();
            mNotificationManager.notify(notifyID, notification);
//            SharedPreferences example=getSharedPreferences("notifyinfo",0);
//            Editor editor = example.edit();
//            editor.putInt("notifyID",notifyID);
//            editor.apply();
        } catch (JSONException e) {
            Log.d("JSON Parse ERROR", e.toString());
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onDestroy(){
        super.onDestroy();
    }

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
}
