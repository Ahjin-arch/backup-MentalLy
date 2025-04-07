package com.example.myaply.ui.emotion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.example.myaply.data.EmotionEntry;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FormEmotionBottomSheet extends BottomSheetDialogFragment {

    private RadioGroup rgEmotions;
    private SeekBar seekBarIntensity;
    private EditText etCommentEmotion;
    private Button btnSaveEmotion;
    private TextView tvIntensityValue;

    private com.example.myaply.ui.emotion.FormEmotionBottomSheet.OnEmotionSaveListener listener;
    public interface OnEmotionSaveListener {
        void onEmotionSave(EmotionEntry newEmotion);
    }

    public void setOnEmotionSaveListener(com.example.myaply.ui.emotion.FormEmotionBottomSheet.OnEmotionSaveListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_diary, container, false);

        rgEmotions = view.findViewById(R.id.rg_emotions);
        seekBarIntensity = view.findViewById(R.id.seekBar_intensity);
        etCommentEmotion = view.findViewById(R.id.et_comment_emotion);
        btnSaveEmotion = view.findViewById(R.id.btn_save_emotion);
        tvIntensityValue=view.findViewById(R.id.tv_intensity_value);

        tvIntensityValue.setText(seekBarIntensity.getProgress() + "/10");
        seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvIntensityValue.setText(progress+ "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        btnSaveEmotion.setOnClickListener(v -> guardarEmocion());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(R.drawable.bg_dialog_rounded);
        }
    }




    private void guardarEmocion() {
        // Obtener emocion seleccionada
        int selectedId = rgEmotions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(requireContext(), "Selecciona una emoción", Toast.LENGTH_SHORT).show();
            return;
        }
        String emotionSelected ="";

        if (selectedId == R.id.rb_emotion_happy) {
            emotionSelected = "😊";
        } else if (selectedId == R.id.rb_Emotion_sad) {
            emotionSelected = "😢";
        } else if (selectedId == R.id.rb_emotion_angry) {
            emotionSelected = "😠";
        }

        int intensidad = seekBarIntensity.getProgress();
        String comentario = etCommentEmotion.getText().toString();
        long timestamp = System.currentTimeMillis();
        String notaFinal =intensidad + "/10. " + comentario;


        EmotionEntry entry = new EmotionEntry(emotionSelected, notaFinal, timestamp);
        if (listener != null) {
            listener.onEmotionSave(entry); // Enviar la emocion al Fragment principal
        }
        dismiss();
        Toast.makeText(getContext(), "Emoción registrada", Toast.LENGTH_SHORT).show();

        rgEmotions.clearCheck();
        seekBarIntensity.setProgress(5); // valor medio
        etCommentEmotion.setText("");
    }
}
