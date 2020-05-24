package com.example.fingerprintunlock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    public Client client;
    public FingerprintUtils finger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///THIS PERMIT TO USE NETWORK ON MAIN TRHREAD
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        //#####################################
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_settings= findViewById(R.id.bt_settings);
        Button bt_aa= findViewById(R.id.AAAA);
        final SharedPreferences sp = getSharedPreferences("FingerUnlock", Context.MODE_PRIVATE);
        client = new Client();
        finger = new FingerprintUtils(this.getApplicationContext());
        finger.setClient(client);
        client.setFPU(finger);
        client.start();
        client.sendMsg("test");
        //finger.callbackFunction();
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
        //client.setResp("test");
        //test t = new test();
        //t.start();
    }

    public void goToSettings(){
        Intent myIntent = new Intent(this.getApplicationContext(), SettingsActivity.class);
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
