package com.antonagre.fingerprintunlock;

import android.os.Build;

import androidx.annotation.RequiresApi;

public abstract class Client {
    public FingerprintUtils fp;
    private String clientName;

    public Client(String clientName,FingerprintUtils fp){
        this.fp=fp;
        this.clientName=clientName;
        initClient();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void handleMessageReceived(String message){
        fp.handleCommand(message);
    }

    public abstract void initClient();

    public abstract void sendResponse(String Response);

}
