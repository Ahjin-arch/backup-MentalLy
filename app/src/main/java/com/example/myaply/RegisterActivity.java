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

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etEmail;
    private Button btnRegister,btnReturnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

         etUsername  = findViewById(R.id.username);
         etPassword  = findViewById(R.id.password);
         etEmail  = findViewById(R.id.passwordVerifi);
         btnRegister  = findViewById(R.id.registerButton);
         btnReturnLogin = findViewById(R.id.fini);

        btnRegister.setOnClickListener(v -> registerUser());


        btnReturnLogin.setOnClickListener(view -> returnLogin());

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
    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences securePrefs = SecurePrefsUtil.getEncryptedPreferences(this);

        if (securePrefs.contains("user_" + username)) {
            Toast.makeText(this, "El nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        securePrefs.edit()
                .putString("user_" + username + "_password", hashedPassword)
                .putString("user_" + username + "_email", email)
                .putBoolean("user_" + username + "_registered", true)
                .apply();

        Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }



}