package com.example.fingerprintunlock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;


public class FingerprintUtils {
    private static final String TAG = MainActivity.class.getName();
    private BiometricPrompt mBiometricPrompt;
    private String addr = "192.168.1.202";
    private WebSocketClient webSocketClient;
    private Context context;

    public FingerprintUtils(Context c) {
        this.context = c;
        //initialize socket
        initializeSocket();
    }

    public FingerprintUtils(Context c,String address) {
        this.context = c;
        //initialize socket
        this.addr=address;
        initializeSocket();
    }

    private void initializeSocket(){
        URI uri;
        try {
            uri = new URI("ws://"+addr+":5555");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
                callbackFunction();
            }

            @Override
            public void onTextReceived(String message) {
                System.out.println("onTextReceived");
                handleCommand(message);
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

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(0);
        webSocketClient.connect();
    }

    private void sendResponse(String Response){
        webSocketClient.send("AUTHORIZED_LOGIN");
        Log.d(TAG,"Response: "+Response);
    }

    public void callbackFunction() {
        final BiometricPrompt.AuthenticationCallback callback = getAuthenticationCallback();
        Prompt(callback);
    }

    private void handleCommand(String command) {
        if (command == "REQ_AUTH") {
            callbackFunction();
        }
    }

    @android.support.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.P)
    public void Prompt(BiometricPrompt.AuthenticationCallback authenticationCallback) {
        if (isSupportBiometricPrompt()) {
            // Create biometricPrompt
            mBiometricPrompt = new BiometricPrompt.Builder(this.context)
                    .setTitle("FingerLock")
                    .setSubtitle("Fingerprint Login Assistant")
                    .setDescription("Scan your Fingerprint")
                    .setNegativeButton("Cancel", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i(TAG, "Cancel button clicked");
                        }
                    })
                    .build();
            CancellationSignal cancellationSignal = getCancellationSignal();
            mBiometricPrompt.authenticate(cancellationSignal, context.getMainExecutor(), authenticationCallback);

        }
    }

    private boolean isSupportBiometricPrompt() {
        PackageManager packageManager = this.context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
    }

    private CancellationSignal getCancellationSignal() {
        // With this cancel signal, we can cancel biometric prompt operation
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                //handle cancel result
                Log.i(TAG, "Canceled");
            }
        });
        return cancellationSignal;
    }


    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //sendResponse("UNAUTHORIZED");
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context, "LOGIN_AUTHORIZED", Toast.LENGTH_SHORT).show();
                sendResponse("AUTHORIZED");
            }
        };
    }

}
