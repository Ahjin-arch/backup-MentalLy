package com.example.myaply.ui.relax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myaply.R;
import com.example.myaply.databinding.FragmentRelaxBinding;

public class RelaxFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relax, container, false);

        // Botón para abrir el Bottom Sheet
        view.findViewById(R.id.btn_sounds).setOnClickListener(v -> {
            SoundBottomSheet soundBottomSheet = new SoundBottomSheet();
            soundBottomSheet.show(getParentFragmentManager(), "SoundBottomSheet");
        });
        //Solo acepta llamadas directas, caso contrario stop
       view.findViewById(R.id.btn_break).setOnClickListener(v -> {
            BreathingBottomSheet sheet = new BreathingBottomSheet();
            sheet.show(getParentFragmentManager(), "breathing_sheet");
        });

        return view;
    }
}
