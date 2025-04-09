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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;
import com.example.myaply.data.Habit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HabitFragment extends Fragment {
    private HabitViewModel habitViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        // Inicializar ViewModel
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_habits);
        HabitAdapter habitAdapter = new HabitAdapter();
        recyclerView.setAdapter(habitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //inicializamos el spinner


// Observar cambios de hábitos y actualizar la lista
        habitViewModel.getAllHabits().observe(getViewLifecycleOwner(), habitAdapter::setHabitList);
        //dialog para elimianr
        habitAdapter.setOnHabitLongClickListener(habit -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar Hábito")
                    .setMessage("¿Estás seguro de que deseas eliminar este hábito?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        habitViewModel.deleteHabit(habit);
                        Toast.makeText(getContext(), "Hábito eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        habitAdapter.setOnHabitDoneClickListener(habit -> {
            habitViewModel.markHabitAsDone(habit);
        });
// accion del boton flotante
        FloatingActionButton btnAdd = view.findViewById(R.id.btn_add_habit);
        btnAdd.setOnClickListener(v ->{
            FormHabitBottomSheet bottomSheet=new FormHabitBottomSheet();
            bottomSheet.setOnHabitoGuardadoListener(nuevoHabito -> {
                habitViewModel.insertHabit(nuevoHabito);
            });
            bottomSheet.show(getParentFragmentManager(),"FormHabitBottomSheet");
        });




        return view;
    }

}
