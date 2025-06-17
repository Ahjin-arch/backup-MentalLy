package com.example.myaply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);
        btnGoToRegister = findViewById(R.id.registerButton);

        btnLogin.setOnClickListener(v -> loginUser());
        btnGoToRegister.setOnClickListener(v -> goToRegister());
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences securePrefs = SecurePrefsUtil.getEncryptedPreferences(this);


        String storedHash = securePrefs.getString("user_" + username + "_password", null);
        if (storedHash == null) {
            Toast toast = Toast.makeText(this, "Usuario no registradp", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200); // Ajusta la posición
            toast.show();

            return;
        }

        if (BCrypt.checkpw(password, storedHash)) {
            Toast.makeText(this, "Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
            securePrefs.edit()
                    .remove("failed_" + username)
                    .remove("blocked_" + username)
                    .apply();
            securePrefs.edit()
                    .putBoolean("is_logged_in", true)
                    .putString("current_user", username)
                    .apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            handleFailedLogin(username);
        }
        long blockUntil = securePrefs.getLong("blocked_" + username, 0);
        if (System.currentTimeMillis() < blockUntil) {
            long minutesLeft = (blockUntil - System.currentTimeMillis()) / 60000;
            Toast.makeText(this, "Cuenta bloqueada. Intente nuevamente en " + minutesLeft + " minutos", Toast.LENGTH_LONG).show();
            return;
        }
    }
    private void handleFailedLogin(String username) {
        SharedPreferences securePrefs = SecurePrefsUtil.getEncryptedPreferences(this);

        int failedAttempts = securePrefs.getInt("failed_" + username, 0) + 1;
        securePrefs.edit().putInt("failed_" + username, failedAttempts).apply();

        if (failedAttempts >= 3) {
            long blockUntil = System.currentTimeMillis() + (5 * 60 * 1000);
            securePrefs.edit().putLong("blocked_" + username, blockUntil).apply();

            Toast.makeText(this, "Cuenta bloqueada por 5 minutos", Toast.LENGTH_LONG).show();


        } else {
            int remaining = 3 - failedAttempts;
            Toast.makeText(this, "Contraseña incorrecta. Intentos restantes: " + remaining, Toast.LENGTH_SHORT).show();
        }
    }
    private void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}