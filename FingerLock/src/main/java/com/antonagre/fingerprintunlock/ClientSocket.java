package com.antonagre.fingerprintunlock;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ClientSocket extends Client {
    private Uri uri;
    private WebSocketClient webSocketClient;
    private Thread clientThread;

    public ClientSocket(String clientName,FingerprintUtils fp) {
        super(clientName,fp);
    }

    @Override
    public void sendResponse(String Response){
        webSocketClient.send("AUTHORIZED_LOGIN");
        webSocketClient.close();
        Log.d("Socket-client","Response: "+Response);
    }

    public void execute(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                webSocketClient = new WebSocketClient(uri) {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onOpen() {
                        System.out.println("onOpen");
                        fp.callbackFunction();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onTextReceived(String message) {
                        System.out.println("onTextReceived");
                        if (message=="REQ_AUTH"){
                            fp.callbackFunction();
                        }
                    }

                    @Override
                    public void onBinaryReceived(byte[] data) {
                        System.out.println("onBinaryReceived");
                    }

                    @Override
                    public void onPingReceived(byte[] data) {
                        System.out.println("onPingReceived");
                    }

                    @Override
                    public void onPongReceived(byte[] data) {
                        System.out.println("onPongReceived");
                    }

                    @Override
                    public void onException(Exception e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onCloseReceived() {
                        System.out.println("onCloseReceived");
                    }
                };

                webSocketClient.setConnectTimeout(1000);
                webSocketClient.setReadTimeout(6000);
                webSocketClient.enableAutomaticReconnection(0);
                webSocketClient.connect();
            }
        };
        clientThread=new Thread(r);
        clientThread.run();
    }


}
