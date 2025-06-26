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
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Locale;

public class MuscleRelaxBottomSheet extends BottomSheetDialogFragment {

    private TextView tvMuscleStep;
    private LinearProgressIndicator pbProgress;
    private MaterialButton btnStart;
    private Vibrator vibrator;
    private Handler handler = new Handler();
    private int stepIndex = 0;
    private boolean isRunning = false;
    private ImageView ivMuscleIllustration;
    private TextView tvTimer;
    private long startTime;
    private ValueAnimator progressAnimator;

    private final MuscleStep[] steps = {
            new MuscleStep("Tensa los hombros", R.drawable.ic_shoulders_tense, true, 4000),
            new MuscleStep("Relájalos", R.drawable.ic_shoulders, false, 4000),
            new MuscleStep("Tensa los brazos", R.drawable.ic_arms, true, 4000),
            new MuscleStep("Relájalos", R.drawable.ic_arms, false, 4000),
            new MuscleStep("Tensa las piernas", R.drawable.ic_legs, true, 5000),
            new MuscleStep("Relájalas", R.drawable.ic_legs, false, 5000),
            new MuscleStep("Tensa la cara", R.drawable.ic_face, true, 3000),
            new MuscleStep("Relájala", R.drawable.baseline_emoji_emotions_24, false, 3000),
            new MuscleStep("Respira profundamente y suelta", R.drawable.baseline_breath, false, 8000)
    };

    // Clase auxiliar para almacenar información de cada paso
    private static class MuscleStep {
        String instruction;
        int illustrationRes;
        boolean isTenseStep;
        int duration;

        MuscleStep(String instruction, int illustrationRes, boolean isTenseStep, int duration) {
            this.instruction = instruction;
            this.illustrationRes = illustrationRes;
            this.isTenseStep = isTenseStep;
            this.duration = duration;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.relax_muscle_sheet, container, false);

        tvMuscleStep = view.findViewById(R.id.tvMuscleStep);
        pbProgress = view.findViewById(R.id.pbMuscleProgress);
        btnStart = view.findViewById(R.id.btnStartMuscle);
        ivMuscleIllustration = view.findViewById(R.id.ivMuscleIllustration);
        tvTimer = view.findViewById(R.id.tvTimer);

        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);

        btnStart.setOnClickListener(v -> {
            if (!isRunning) {
                startRelaxation();
            } else {
                stopRelaxation();
            }
        });

        return view;
    }

    private void startRelaxation() {
        isRunning = true;
        stepIndex = 0;
        btnStart.setText("Detener");
        btnStart.setIconResource(R.drawable.ic_pause);
        startTime = System.currentTimeMillis();
        runStep();
        updateTimer();
    }

    private void runStep() {
        if (!isRunning || stepIndex >= steps.length) {
            return;
        }

        MuscleStep currentStep = steps[stepIndex];
        updateUIForStep(currentStep);
        vibrateForStep(currentStep.isTenseStep);
        animateProgress();

        handler.postDelayed(() -> {
            stepIndex++;
            if (stepIndex < steps.length) {
                runStep();
            } else {
                completeRelaxation();
            }
        }, currentStep.duration);
    }

    private void updateUIForStep(MuscleStep step) {
        // Animación de transición
        animateStepTransition(step);

        // Actualizar progreso
        int progress = (int) (((float) stepIndex / steps.length) * 100);
        pbProgress.setProgress(progress);
    }

    private void animateStepTransition(MuscleStep step) {
        // Animación de fade out
        ivMuscleIllustration.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    // Cambiar recurso y hacer fade in
                    ivMuscleIllustration.setImageResource(step.illustrationRes);
                    ivMuscleIllustration.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .start();

                    // Actualizar texto con animación
                    tvMuscleStep.setText(step.instruction);
                    tvMuscleStep.setAlpha(0f);
                    tvMuscleStep.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    private void animateProgress() {
        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
        }

        int startProgress = pbProgress.getProgress();
        int endProgress = (int) (((float) (stepIndex + 1) / steps.length) * 100);

        progressAnimator = ValueAnimator.ofInt(startProgress, endProgress);
        progressAnimator.setDuration(steps[stepIndex].duration);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            pbProgress.setProgress(value);
        });
        progressAnimator.start();
    }

    private void vibrateForStep(boolean isTenseStep) {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (isTenseStep) {
            // Vibración más fuerte para tensar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        } else {
            // Vibración suave para relajar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, 50));
            } else {
                vibrator.vibrate(300);
            }
        }
    }

    private void updateTimer() {
        if (!isRunning) return;

        long elapsedMillis = System.currentTimeMillis() - startTime;
        long seconds = elapsedMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(time);

        handler.postDelayed(this::updateTimer, 1000);
    }

    private void completeRelaxation() {
        isRunning = false;
        tvMuscleStep.setText("¡Bien hecho! Relajación completa 😊");
        ivMuscleIllustration.setImageResource(R.drawable.ic_complete);
        btnStart.setText("Reiniciar");
        btnStart.setIconResource(R.drawable.ic_refresh);
        pbProgress.setProgress(100);

        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long[] pattern = {0, 200, 100, 200, 100, 300};
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
            } else {
                vibrator.vibrate(new long[]{0, 200, 100, 200, 100, 300}, -1);
            }
        }
    }

    private void stopRelaxation() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
        btnStart.setText("Continuar");
        btnStart.setIconResource(R.drawable.ic_play);
        tvMuscleStep.setText("Relajación en pausa");

        if (progressAnimator != null) {
            progressAnimator.pause();
        }
        // Detener vibración
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }

    }
}

