package com.example.myaply.ui.habit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;
import com.example.myaply.data.Habit;

public class HabitFragment extends Fragment {

    private HabitViewModel habitViewModel;
    private EditText etHabitName;
    private Spinner spinnerFrequency;
    private Button btnAddHabit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        // Inicializar ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        // Referenciar elementos de la UI
        etHabitName = view.findViewById(R.id.et_habit_name);
        spinnerFrequency = view.findViewById(R.id.spinner_frequency);
        btnAddHabit = view.findViewById(R.id.btn_add_habit);
        //opciones de espiiner
        String[] opciones = new String[]{"Diario", "Semanal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapter);

        // Agregar listener al botón
        btnAddHabit.setOnClickListener(v -> {
            String habitName = etHabitName.getText().toString().trim();
            String frequency = spinnerFrequency.getSelectedItem().toString();

            if (habitName.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, escribe un hábito.", Toast.LENGTH_SHORT).show();
                return;
            }

            Habit newHabit = new Habit(habitName, frequency);
            habitViewModel.insertHabit(newHabit);

            Toast.makeText(getContext(), "¡Hábito agregado!", Toast.LENGTH_SHORT).show();
            etHabitName.setText(""); // Limpiar campo
            spinnerFrequency.setSelection(0); // Reiniciar spinner
        });



        RecyclerView recyclerView = view.findViewById(R.id.recycler_habits);
        HabitAdapter habitAdapter = new HabitAdapter();
        recyclerView.setAdapter(habitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

// Observar cambios de hábitos y actualizar la lista
        habitViewModel.getAllHabits().observe(getViewLifecycleOwner(), habits -> {
            habitAdapter.setHabitList(habits);

            // Extra: Actualizar progreso
            int progreso = (int) ((habits.size() / 10.0) * 100); // Máximo 10 hábitos
            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            TextView tvProgress = view.findViewById(R.id.tv_progress_percentage);

            progressBar.setProgress(progreso);
            tvProgress.setText(progreso + "%");
        });



        return view;
    }
}
