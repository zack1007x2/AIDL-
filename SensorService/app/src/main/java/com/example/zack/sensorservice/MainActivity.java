package com.example.zack.sensorservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private TextView tvUnlock, tvx, tvy, tvz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        tvUnlock = (TextView) findViewById(R.id.tvUnlock);
        tvx = (TextView) findViewById(R.id.tvx);
        tvy = (TextView) findViewById(R.id.tvy);
        tvz = (TextView) findViewById(R.id.tvz);
        tvUnlock.setOnClickListener(listeners);
    }

    private View.OnClickListener listeners = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startService(new Intent(MainActivity.this, Myservice.class));
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
