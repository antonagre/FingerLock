package com.antonagre.fingerprintunlock;
/*import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CriptoManager {
    private SecretKey secretKey;
    private IvParameterSpec ivParameterSpec;

    private String key = "ThisIsMyGreatKey";
    private byte[] ivKey = "ABCDEFGHabcdefgh".getBytes();


    private void main() {
        ivParameterSpec = new IvParameterSpec(ivKey);
        secretKey = new SecretKeySpec(key.getBytes(), "AES");
        String x="test-encrypt";
        String y=encryptOrDecrypt(Cipher.ENCRYPT_MODE, x);
        Log.d("encript","text"+"test-encrypt");
        String xx = encryptOrDecrypt(Cipher.DECRYPT_MODE,y);
        Log.d("decript","text"+"test-decrypt"+xx);

    }
    byte[] plainBytes = "HELLO JCE".getBytes();

    // Generate the key first
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Key size
    Key key = keyGen.generateKey();

    // Create Cipher instance and initialize it to encrytion mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  // Transformation of the algorithm
            cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherBytes = cipher.doFinal(plainBytes);

    // Reinitialize the Cipher to decryption mode
            cipher.init(Cipher.DECRYPT_MODE,key, cipher.getParameters());
    byte[] plainBytesDecrypted = cipher.doFinal(cipherBytes);

            System.out.println("DECRUPTED DATA : "+new String(plainBytesDecrypted));
}
^*/