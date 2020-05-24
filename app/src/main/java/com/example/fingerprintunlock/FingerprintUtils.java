package com.example.fingerprintunlock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.samigehi.socket.callback.ConnectCallback;


public class FingerprintUtils {
    private static final String TAG = MainActivity.class.getName();
    private BiometricPrompt mBiometricPrompt;
    public Context context;

    public FingerprintUtils(Context c) {
        this.context = c;
        //initialize socket
        SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://192.168.1.2:3000", new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, SocketIOClient client) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                client.setStringCallback(new StringCallback() {
                    @Override
                    public void onString(String string) {
                        System.out.println(string);
                    }
                });
                client.on("someEvent", new EventCallback() {
                    @Override
                    public void onEvent(JSONArray argument, Acknowledge acknowledge) {
                        System.out.println("args: " + arguments.toString());
                    }
                });
                client.setJSONCallback(new JSONCallback() {
                    @Override
                    public void onJSON(JSONObject json) {
                        System.out.println("json: " + json.toString());
                    }
                });
            }
        });
    }

    private void sendResponse(String response){

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
                SendResponse("AUTHORIZED");
            }
        };
    }
}
