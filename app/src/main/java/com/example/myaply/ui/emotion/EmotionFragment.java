package com.example.myaply.ui.emotion;

import androidx.lifecycle.ViewModelProvider;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        return view;
    }

    private void setupEmotionButton(Button button, String emoji) {
        button.setOnClickListener(v -> {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            EmotionEntry entry = new EmotionEntry(emoji, today);
            emotionViewModel.insertEmotion(entry);
            tvStatus.setText("Estado registrado: " + emoji);
        });
    }
}
