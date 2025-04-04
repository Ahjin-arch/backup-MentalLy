package com.example.myaply.ui.emotion;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myaply.R;
import com.example.myaply.data.EmotionEntry;

import java.util.Date;
import java.util.Locale;

public class EmotionFragment extends Fragment {

    private EmotionViewModel emotionViewModel;
    private TextView tvStatus;
    private RecyclerView recyclerView;
    private EmotionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion, container, false);

        emotionViewModel = new ViewModelProvider(this).get(EmotionViewModel.class);
        tvStatus = view.findViewById(R.id.tv_status);

        setupEmotionButton(view.findViewById(R.id.btn_happy), "😊");
        setupEmotionButton(view.findViewById(R.id.btn_sad), "😔");
        setupEmotionButton(view.findViewById(R.id.btn_angry), "😡");
        setupEmotionButton(view.findViewById(R.id.btn_relaxed), "😌");


        recyclerView = view.findViewById(R.id.recycler_emotions);
        adapter = new EmotionAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        emotionViewModel.getAllEmotions().observe(getViewLifecycleOwner(), emotions -> {
            adapter.setEmotionList(emotions);
        });

        return view;
    }

    private void setupEmotionButton(Button button, String emoji) {
        button.setOnClickListener(v -> {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            EmotionEntry entry = new EmotionEntry(emoji, today);
            emotionViewModel.insertEmotion(entry);
            tvStatus.setText("Estado registrado: " + emoji);
            scheduleDailyReminder();
        });
    }



    private void scheduleDailyReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getContext(), EmotionReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

}
