package com.example.myaply.ui.relax;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SoundBottomSheet extends BottomSheetDialogFragment {

    private MediaPlayer mediaPlayer;
    private ImageView ivBackground;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound_relax, container, false);

        // fondo

        ivBackground = view.findViewById(R.id.iv_background);
        //botones
        view.findViewById(R.id.btn_rain_sound).setOnClickListener(v -> {
            playSound(R.raw.rain);
            changeBackground(R.drawable.rain_background);
        });

        view.findViewById(R.id.btn_forest_sound).setOnClickListener(v -> {
            playSound(R.raw.forest);
            changeBackground(R.drawable.forest_background);
            Toast toast = Toast.makeText(getContext(), "Desliza hacia abajo", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
        });
        view.findViewById(R.id.btn_waves_sound).setOnClickListener(v -> {
            playSound(R.raw.waves);
            changeBackground(R.drawable.waves_background);
        });
        view.findViewById(R.id.btn_fireplace_sound).setOnClickListener(v -> {
            playSound(R.raw.fireplace);
            changeBackground(R.drawable.fireplace_background);
        });
        return view;
    }

    private void playSound(int soundResId) {
        // Si hay un sonido reproduciéndose, detenerlo
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Configurar y reproducir nuevo sonido
        mediaPlayer = MediaPlayer.create(getContext(), soundResId);
        mediaPlayer.start();

        // Liberar recursos cuando termine el sonido
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }
    private void changeBackground(int drawableResId) {
        // Cambiar el fondo dinámico
        ivBackground.setImageResource(drawableResId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenBottomSheet);
    }
    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // Expandir completamente
                behavior.setSkipCollapsed(true); // Evitar estado colapsado
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Asegurarse de liberar recursos al cerrar el Bottom Sheet
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
