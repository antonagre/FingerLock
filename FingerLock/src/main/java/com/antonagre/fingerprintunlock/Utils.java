package com.antonagre.fingerprintunlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Utils {
    private static Context context;
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;


    public static void setContext(Context c){
        context=c;
        setupSharedPreferencs();
    }

    public static Context getContext(){
        return context;
    }

    private static void setupSharedPreferencs(){
        sharedPref= context.getSharedPreferences("FingerUnlock", Context.MODE_PRIVATE);;
        editor = sharedPref.edit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addHost(String hostIp){
        String[] currentHosts = sharedPref.getString("HOSTS",null).split("|");
        ArrayList<String> hosts;
        hosts = new ArrayList<String>(currentHosts);
        if (! hosts.contains(hostIp)){
            hosts.add(hostIp);
            String updatedHosts = String.join("|", (String) hosts.toArray());
            editor.putString("HOSTS",updatedHosts);
            editor.commit();
        }
    }

    public static ArrayList<String> getHosts(){
        String[] currentHosts = sharedPref.getString("HOSTS",null).split("|");
        ArrayList<String> hosts=new ArrayList<String>(currentHosts);
        return hosts;
    }
}
