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
import com.google.android.material.button.MaterialButtonToggleGroup;

public class BreathingBottomSheet extends BottomSheetDialogFragment {

    private TextView tvInstruction;
    private View circleView;
    private Button btnStart;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;


    private final String[] pattern = {"Inhala", "Mantén", "Exhala", "Mantén"};
    private final int[] durations = {4000, 4000, 4000, 4000}; // 4s cada fase
    private int step = 0;
    private long startTime;

    private long totalDurationMillis=60000;
    private MaterialButtonToggleGroup rgDuration;


    private Runnable breathRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= totalDurationMillis) {
                isRunning = false;
                btnStart.setText("Comenzar");
                tvInstruction.setText("Prepárate...");
                circleView.setScaleX(1f);
                circleView.setScaleY(1f);
                handler.removeCallbacks(this);
                mediaPlayer.pause();
                return;
            }
            int color = Color.BLUE;
            switch (step) {
                case 0: color = Color.parseColor("#A3E4FD"); break; // Inhala
                case 1: color = Color.parseColor("#64B5F6"); break; // Mantén
                case 2: color = Color.parseColor("#1565C0"); break; // Exhala
                case 3: color = Color.parseColor("#64B5F6"); break; // Mantén
            }
            circleView.setBackgroundTintList(ColorStateList.valueOf(color));
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

        btnStart.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                btnStart.setText("Detener");
                step = 0;
                startTime = System.currentTimeMillis();
                breathRunnable.run();
                mediaPlayer.start();

                totalDurationMillis = getSelectedDuration(rgDuration);
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
    private long getSelectedDuration(MaterialButtonToggleGroup rgDuration) {
        int checkedId=rgDuration.getCheckedButtonId();
        if (checkedId == R.id.rb_1_min) {
            totalDurationMillis= 60000;
        } else if (checkedId == R.id.rb_3_min) {
            totalDurationMillis= 180000;
        } else if (checkedId == R.id.rb_5_min) {
            totalDurationMillis= 300000;
        }
        return totalDurationMillis;
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
