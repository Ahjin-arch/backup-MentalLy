package com.example.myaply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText passwordVerifi = findViewById(R.id.passwordVerifi);
        Button registerButton = findViewById(R.id.registerButton);
        Button fini = findViewById(R.id.fini);

        registerButton.setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String passVeri = passwordVerifi.getText().toString();
            registerUser(user, pass, passVeri);
        });

        fini.setOnClickListener(view -> returnLogin());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void returnLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void registerUser(String user, String pass, String passVerifi) {
        if (user.isEmpty() || pass.isEmpty() || passVerifi.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(passVerifi)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getEncryptedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registeredUser", user);
        editor.putString("registeredPass", pass);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        Toast.makeText(this, "Registro exitoso. Bienvenido!", Toast.LENGTH_SHORT).show();

        returnLogin();

    }

    private SharedPreferences getEncryptedPreferences() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            return EncryptedSharedPreferences.create(
                    "MySecurePrefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error al crear EncryptedSharedPreferences", e);
        }
    }


}