package com.example.fingerprintunlock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.samigehi.socket.SocketHelper;
import com.samigehi.socket.callback.AsyncSocket;
import com.samigehi.socket.callback.DataEmitter;
import com.samigehi.socket.callback.SocketListener;


public class FingerprintUtils {
    private static final String TAG = MainActivity.class.getName();
    private SocketHelper helper;
    private BiometricPrompt mBiometricPrompt;
    public Context context;

    public FingerprintUtils(Context c) {
        this.context = c;
        //initialize socket
        initializeSocket();
    }

    private void initializeSocket(){
        helper = new SocketHelper("192.168.1.202", 5555);

        helper.connect(new SocketListener() {

            @Override
            public void onConnect(AsyncSocket socket) {
                // on successfully connected to server
                helper.send("CONNECTED");
            }

            @Override
            public void onError(Exception error) {
                // when an error occurred
                error.printStackTrace();
            }

            @Override
            public void onClosed(Exception error) {
                // when connection closed by server or an error occurred to forcefully close server connection
            }

            @Override
            public void onDisconnect(Exception error) {
                // when connection closed by server or self closed by calling disconnect
            }

            @Override
            public void onDataWrite(String message, Exception error) {
                // notify when data successfully sent to server
                Log.d("SocketHelper", "onDataWrite >> " + message);
                if (error != null)
                    error.printStackTrace();
            }

            @Override
            public void onDataReceived(String message, DataEmitter emitter) {
                // notify when new data received from server
                Log.d("SocketHelper", "onDataReceived >> " + message);
                if (message=="AUTHENTICATE") {
                    // Request AUTHENTICATION
                    callbackFunction();
                }
            }
        });
    }

    private void sendResponse(String response){
        helper.send("CONNECTED");
    }
    public void callbackFunction() {
        final BiometricPrompt.AuthenticationCallback callback = getAuthenticationCallback();
        Prompt(callback);
    }

    public void handleCommand(String command) {
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
