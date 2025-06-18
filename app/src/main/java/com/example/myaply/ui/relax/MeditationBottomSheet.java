package com.example.myaply.ui.relax;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeditationBottomSheet extends BottomSheetDialogFragment {

    private TextView tvPhrase;
    private MaterialButtonToggleGroup rgDuration;
    private Button btnStart;
    private Handler handler = new Handler();
    private int currentPhraseIndex = 0;
    private long sessionDurationMillis = 60000; // default 1 min
    private List<String> meditationPhrases;
    private Runnable phraseRunnable;
    private CountDownTimer sessionTimer;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private ObjectAnimator mandalaAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meditation_sheet, container, false);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.meditation_sound);
        mediaPlayer.setLooping(true);
        //animación background
        View root = view.findViewById(R.id.container_meditation);
        AnimationDrawable anim = (AnimationDrawable) root.getBackground();
        anim.setEnterFadeDuration(2000);
        anim.setExitFadeDuration(2000);
        anim.start();

        tvPhrase = view.findViewById(R.id.tv_meditation_phrase);
        rgDuration = view.findViewById(R.id.rg_duration_meditation);
        btnStart = view.findViewById(R.id.btn_start_meditation);
        ImageView mandala=view.findViewById(R.id.iv_focal_point);
        setupMandalaAnimation(mandala);


        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);

        meditationPhrases = Arrays.asList(
                "Respira profundamente...",
                "Siente tu cuerpo en calma...",
                "Deja pasar los pensamientos como nubes...",
                "Estás aquí. Ahora. Presente.",
                "Abraza este momento de quietud..."
        );

        btnStart.setOnClickListener(v -> {
            definirDuracion();
            iniciarMeditacion();
            btnStart.setText(" ");
        });

        return view;
    }
    private void setupMandalaAnimation(ImageView mandala) {
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0f, 360f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mandala, rotation);
        animator.setDuration(60000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

    }
    private void definirDuracion() {
        int checkedId = rgDuration.getCheckedButtonId();

        Map<Integer, Long> durationMap = new HashMap<>();
        durationMap.put(R.id.rb_3_min, 180000L);
        durationMap.put(R.id.rb_5_min, 300000L);
        durationMap.put(R.id.rb_1_min, 60000L); // duracion por defecto
        sessionDurationMillis = durationMap.getOrDefault(checkedId, 60000L);
    }

    private void iniciarMeditacion() {
        btnStart.setEnabled(false);
        mediaPlayer.start();
        rgDuration.setEnabled(false);
        mostrarSiguienteFrase();

        sessionTimer = new CountDownTimer(sessionDurationMillis, 7000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mostrarSiguienteFrase();
            }

            @Override
            public void onFinish() {
                tvPhrase.setText("Meditación finalizada 🧘‍♂️");
                vibrate();
                stopMediaPlayer();
                new Handler().postDelayed(() -> dismiss(), 2500);
            }
        }.start();
    }
    private void vibrate(){
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }
    }
    private void stopMediaPlayer(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void mostrarSiguienteFrase() {
        if (meditationPhrases.isEmpty()) return;

        String frase = meditationPhrases.get(currentPhraseIndex % meditationPhrases.size());
        currentPhraseIndex++;
        tvPhrase.setAlpha(0f);
        tvPhrase.setText(frase);
        tvPhrase.animate().alpha(1f).setDuration(1000).start();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mandalaAnimator != null) {
            mandalaAnimator.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mandalaAnimator != null && mandalaAnimator.isRunning()) {
            mandalaAnimator.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sessionTimer != null) sessionTimer.cancel();
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
}

