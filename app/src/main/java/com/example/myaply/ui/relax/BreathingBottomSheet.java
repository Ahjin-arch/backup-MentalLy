package com.example.myaply.ui.relax;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BreathingBottomSheet extends BottomSheetDialogFragment {

    private TextView tvInstruction;
    private View circleView;
    private Button btnStart;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private RadioGroup rgDuration;
    private int sessionDurationMillis = 60000; // por defecto 1 min



    private final String[] pattern = {"Inhala", "Mantén", "Exhala", "Mantén"};
    private final int[] durations = {4000, 4000, 4000, 4000}; // 4s cada fase
    private int step = 0;

    private Runnable breathRunnable = new Runnable() {

        @Override
        public void run() {
            //Color
            int color = Color.BLUE; // por defecto
            switch (step) {
                case 0: color = Color.parseColor("#81D4FA"); break; // Inhala
                case 1: color = Color.parseColor("#4FC3F7"); break; // Mantén
                case 2: color = Color.parseColor("#0288D1"); break; // Exhala
                case 3: color = Color.parseColor("#4FC3F7"); break; // Mantén
            }
            circleView.setBackgroundTintList(ColorStateList.valueOf(color));

            if (!isRunning) return;

            tvInstruction.setText(pattern[step]);

            float scale = (step == 0) ? 1.5f : (step == 2) ? 1f : circleView.getScaleX();

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(circleView, "scaleX", scale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(circleView, "scaleY", scale);
            scaleX.setDuration(1000);
            scaleY.setDuration(1000);
            scaleX.start();
            scaleY.start();

            handler.postDelayed(this, durations[step]);
            step = (step + 1) % pattern.length;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.breathing_bottom_sheet, container, false);

        tvInstruction = view.findViewById(R.id.tv_breath_instruction);
        circleView = view.findViewById(R.id.breath_circle);
        btnStart = view.findViewById(R.id.btn_start_breathing);
        rgDuration = view.findViewById(R.id.rg_duration);

        rgDuration.setOnCheckedChangeListener((group, checkedId) -> {
             checkedId = rgDuration.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_1_min) {
                sessionDurationMillis = 60000;
            } else if (checkedId == R.id.rb_3_min) {
                sessionDurationMillis = 180000;
            } else if (checkedId == R.id.rb_5_min) {
                sessionDurationMillis = 300000;
            }

        });


        btnStart.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                btnStart.setText("Detener");
                step = 0;
                breathRunnable.run();
                mediaPlayer.start();

                handler.postDelayed(() -> {
                    isRunning = false;
                    Toast.makeText(getContext(), "¡Sesión completada! Bien hecho 😊", Toast.LENGTH_LONG).show();
                    btnStart.setText("Comenzar");
                    tvInstruction.setText("¡Completado!");
                    circleView.setScaleX(1f);
                    circleView.setScaleY(1f);
                    mediaPlayer.pause();
                }, sessionDurationMillis);



            } else {
                isRunning = false;
                btnStart.setText("Comenzar");
                tvInstruction.setText("Prepárate...");
                circleView.setScaleX(1f);
                circleView.setScaleY(1f);
                handler.removeCallbacks(breathRunnable);
                mediaPlayer.pause();

            }
        });
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.breath_sound);
        mediaPlayer.setLooping(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(breathRunnable);
        isRunning = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
