package com.example.myaply.ui.habit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaply.R;
import com.example.myaply.data.Habit;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FormHabitBottomSheet extends BottomSheetDialogFragment {

    private EditText etNombreHabito, etDescripcionHabito;
    private RadioGroup rgHabitFrequency;
    private Button btnGuardarHabito;

    private OnHabitoGuardadoListener listener;

    public interface OnHabitoGuardadoListener {
        void onHabitoGuardado(Habit nuevoHabito);
    }

    public void setOnHabitoGuardadoListener(OnHabitoGuardadoListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.dialog_add_habit, container, false);

        etNombreHabito = vista.findViewById(R.id.et_habit_name);
        etDescripcionHabito = vista.findViewById(R.id.et_habit_description);
        rgHabitFrequency = vista.findViewById(R.id.rgHabitFrequency);
        btnGuardarHabito = vista.findViewById(R.id.btn_habit_save);



        btnGuardarHabito.setOnClickListener(v -> guardarHabito());

        return vista;
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

    private void guardarHabito() {
        int selectedId = rgHabitFrequency.getCheckedRadioButtonId();
        String frecuenciaSeleccionada = "";

        if (selectedId == R.id.rbFrequencyDaily) {
            frecuenciaSeleccionada = "Diario";
        } else if (selectedId == R.id.rbFrequencyWeekly) {
            frecuenciaSeleccionada = "Semanal";
        } else if (selectedId == R.id.rbFrequencyMonthly) {
            frecuenciaSeleccionada = "Mensual";
        }

        // Validar selección y guardar el hábito
        String nombre = etNombreHabito.getText().toString();
        String descripcion = etDescripcionHabito.getText().toString();

        if (!nombre.isEmpty() && !frecuenciaSeleccionada.isEmpty()) {
            Habit nuevoHabito = new Habit( nombre, descripcion, frecuenciaSeleccionada);
            if (listener != null) {
                listener.onHabitoGuardado(nuevoHabito); // Enviar el hábito al Fragment principal
            }
            dismiss(); // Cerrar el Bottom Sheet
        } else {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
