package com.example.myaply;

import android.content.Context;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BiometricAuthHelper {

    public interface BiometricAuthCallback {
        void onSuccess();
        void onError(int errorCode, CharSequence errString);
    }

    public static void authenticate(Context context, BiometricAuthCallback callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        FragmentActivity activity = (FragmentActivity) context;

        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        callback.onSuccess();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        callback.onError(errorCode, errString);
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Inicie sesión usando su huella o rostro")
                .setNegativeButtonText("Usar contraseña")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}