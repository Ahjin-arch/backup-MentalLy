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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myaply.R;
import com.example.myaply.data.EmotionEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Locale;

public class EmotionFragment extends Fragment {

    private EmotionViewModel emotionViewModel;
    private EmotionAdapter emotionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion, container, false);

        emotionViewModel = new ViewModelProvider(this).get(EmotionViewModel.class);
        //conexion del recycle y el adapter
        RecyclerView recyclerView=view.findViewById(R.id.recycler_emotions);
        emotionAdapter =new EmotionAdapter();
        recyclerView.setAdapter(emotionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_emotion);
        fabAdd.setOnClickListener(v -> abrirBottomSheet());

        emotionViewModel = new ViewModelProvider(this).get(EmotionViewModel.class);
        emotionViewModel.getAllEntries().observe(getViewLifecycleOwner(), entries -> {
            emotionAdapter.setEmotionList(entries);
        });

        scheduleDailyReminder();
        return view;
    }

    private void abrirBottomSheet() {
        FormEmotionBottomSheet bottomSheet = new FormEmotionBottomSheet();
        bottomSheet.setOnEmotionSaveListener(entry -> {
            emotionViewModel.insertEmotion(entry);
        });
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }




    private void scheduleDailyReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
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
