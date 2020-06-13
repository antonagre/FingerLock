package com.antonagre.fingerprintunlock;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.fingerprintunlock.R;


public class MainActivity extends AppCompatActivity {
    public FingerprintUtils finger;
    private String ip_device;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.stopService();//STOP SERVICE IF RUNNING
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_settings= (Button) findViewById(R.id.bt_settings);
        Button bt_aa= (Button) findViewById(R.id.AAAA);
        Utils.setContext(this.getApplicationContext());

        //finger = new FingerprintUtils(this.getApplicationContext());
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });
        bt_aa.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                finger.callbackFunction();
            }
        });
        startService();///START SERVICE
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startService() {
        Intent serviceIntent = new Intent(this.getApplicationContext(), FingerLockService.class);
        serviceIntent.putExtra("DEVICE_IP", ip_device);
        startForegroundService(serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, FingerLockService.class);
        stopService(serviceIntent);
    }

    public void qrRead(){

    }

    public void goToSettings(){
        Intent myIntent = new Intent(this.getApplicationContext(), Qr_Activity.class);
        this.startActivity(myIntent);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


}
