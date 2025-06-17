package com.example.myaply;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecurePrefsUtil {
    private static final String PREFS_NAME = "secure_app_prefs";

    public static SharedPreferences getEncryptedPreferences(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new SecurityException("Error creating encrypted preferences", e);
        }
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = getEncryptedPreferences(context);
        return prefs.getBoolean("is_logged_in", false);
    }

    public static String getCurrentUser(Context context) {
        SharedPreferences prefs = getEncryptedPreferences(context);
        return prefs.getString("current_user", "");
    }

    public static void logout(Context context) {
        SharedPreferences prefs = getEncryptedPreferences(context);
        prefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("current_user")
                .apply();
    }
}