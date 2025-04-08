package com.example.myaply.ui.relax;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
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

public class MuscleRelaxBottomSheet extends BottomSheetDialogFragment {

    private TextView tvMuscleStep;
    private ProgressBar pbProgress;
    private View viewPulse;
    private ObjectAnimator pulseAnimator;

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
        viewPulse = view.findViewById(R.id.viewPulse);

        pulseAnimator = ObjectAnimator.ofPropertyValuesHolder(
                viewPulse,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.4f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.4f, 1f)
        );
        pulseAnimator.setDuration(1500);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());


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
        if (!pulseAnimator.isRunning()) {
            pulseAnimator.start();
        }
        if (stepIndex < steps.length && isRunning) {
            String currentStep = steps[stepIndex];
            tvMuscleStep.setText(currentStep);

            int progress = (int) (((float) stepIndex / steps.length) * 100);
            pbProgress.setProgress(progress);

            stepIndex++;

            handler.postDelayed(this::startRelaxation, 5000); // 5 segundos por paso
        } else if (isRunning) {
            pulseAnimator.end();
            tvMuscleStep.setText("¡Bien hecho! Relajación completa 😊");
            pbProgress.setProgress(100);
            btnStart.setText("Reiniciar");
            isRunning = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pulseAnimator.end();
        handler.removeCallbacksAndMessages(null);
    }
}

