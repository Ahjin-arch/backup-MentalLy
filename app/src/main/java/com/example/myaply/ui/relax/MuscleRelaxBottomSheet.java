package com.example.myaply.ui.relax;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MuscleRelaxBottomSheet extends BottomSheetDialogFragment {

    private TextView tvMuscleStep;
    private LinearProgressIndicator pbProgress;
    private Vibrator vibrator;


    private Button btnStart;
    private Handler handler = new Handler();
    private int stepIndex = 0;
    private boolean isRunning = false;

    private final String[] steps = {
            "Tensa los hombros",
            "Relájalos",
            "Tensa los brazos",
            "Relájalos",
            "Tensa las piernas",
            "Relájalas",
            "Tensa la cara",
            "Relájala",
            "Respira profundamente y suelta"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.relax_muscle_sheet, container, false);

        tvMuscleStep = view.findViewById(R.id.tvMuscleStep);
        pbProgress = view.findViewById(R.id.pbMuscleProgress);
        btnStart = view.findViewById(R.id.btnStartMuscle);
        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);



        btnStart.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                stepIndex = 0;
                btnStart.setText("Detener");
                startRelaxation();
            } else {
                isRunning = false;
                handler.removeCallbacksAndMessages(null);
                btnStart.setText("Iniciar");
                tvMuscleStep.setText("Relajación detenida");
            }
        });

        return view;
    }

    private void startRelaxation() {

        if (stepIndex < steps.length && isRunning) {
            String currentStep = steps[stepIndex];
            tvMuscleStep.setText(currentStep);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
            }
            if (currentStep.toLowerCase().contains("tensa")) {
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(300);
                    }
                }
            }

            int progress = (int) (((float) stepIndex / steps.length) * 100);
            pbProgress.setProgress(progress);

            stepIndex++;

            handler.postDelayed(this::startRelaxation, 5000); // 5 segundos por paso
        } else if (isRunning) {
            tvMuscleStep.setText("¡Bien hecho! Relajación completa 😊");
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    long[] pattern = {0, 200, 100, 200, 100, 300}; // ON-OFF alternado
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                } else {
                    vibrator.vibrate(new long[]{0, 200, 100, 200, 100, 300}, -1);
                }

            }
            pbProgress.setProgress(100);
            btnStart.setText("Reiniciar");
            isRunning = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}

