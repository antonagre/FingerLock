package com.example.fingerprintunlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.Intent;

public class SettingsActivity  extends AppCompatActivity {
    public String hostIpAddress;
    public EditText editIP;
    public CheckBox checkConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Button bt_update = (Button) findViewById(R.id.bt_updateSettings);
        Button bt_pair = (Button) findViewById(R.id.bt_pair);
        Button bt_home = (Button) findViewById(R.id.bt_home);
        EditText editIP = (EditText) findViewById(R.id.host_ip_in);
        CheckBox checkConn = (CheckBox) findViewById(R.id.check_pair);
        bt_update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                updateSettings();}
        });
        bt_home.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                goToHome();}
        });
        bt_pair.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                pairHostMachine();}
        });


        final SharedPreferences sp = getSharedPreferences("FingerUnlock", Context.MODE_PRIVATE);
    }

    public void updateSettings(){
        hostIpAddress = editIP.getText().toString();
        checkConn.setChecked(true);
    }

    public void goToHome(){
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("key", hostIpAddress); //Optional parameters
        this.startActivity(myIntent);
    }

    public void pairHostMachine() {
        checkConn.setChecked(true);
    }

}