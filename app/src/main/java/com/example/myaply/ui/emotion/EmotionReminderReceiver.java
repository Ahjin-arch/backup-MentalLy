package com.example.myaply.ui.emotion;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.myaply.R;

public class EmotionReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Si el permiso no está otorgado, no enviar la notificación
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "emotion_channel")
                .setSmallIcon(R.drawable.baseline_emoji_emotions_24)
                .setContentTitle("¿Cómo te sentiste hoy?")
                .setContentText("No olvides registrar tu emoción del día 😊")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1001, builder.build());
    }

}

