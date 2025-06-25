package com.example.myaply.ui.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myaply.LoginActivity;
import com.example.myaply.R;
import com.example.myaply.SecurePrefsUtil;
import com.example.myaply.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MaterialToolbar topBar;
    private TextView tvDailyTip, tvWelcome,tvDailyPhrase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        topBar = binding.topAppBar;
        tvWelcome=binding.tvWelcome;
        tvDailyTip=binding.tvDailyTip;
        tvDailyPhrase=binding.tvDailyPhrase;

        SharedPreferences securePrefs = SecurePrefsUtil.getEncryptedPreferences(requireContext());
        String username = securePrefs.getString("current_user", "Usuario");
        String hello="Hola, "+username+"!";
        tvWelcome.setText(hello);
        animateTextChange(tvDailyTip, getDailyTip());
        animateTextChange(tvDailyPhrase, getDailyPhrase());

        showSelected();
        return root;
    }
    private String getDailyTip() {
        String[] tips = {
                "Respira profundamente durante 1 minuto y enfócate solo en tu respiración.",
                "Tómate un descanso de las redes sociales por al menos una hora.",
                "Sal a caminar y observa conscientemente lo que te rodea.",
                "Habla con alguien en quien confíes, aunque sea para saludar.",
                "Escribe tres cosas por las que estás agradecido hoy.",
                "Duerme al menos 7–8 horas esta noche para recargar tu mente.",
                "Permítete sentir lo que sientes, sin juzgarte.",
                "Haz algo que disfrutes, aunque sean solo 10 minutos.",
                "Recuerda: pedir ayuda no te hace débil, te hace humano.",
                "Respeta tus límites. Decir 'no' también es autocuidado."
        };
        return tips[(int) (Math.random() * tips.length)];
    }
    private void animateTextChange(TextView textView, String newText) {
        textView.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(() -> {
                    textView.setText(newText);
                    textView.animate()
                            .alpha(1f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }
    private String getDailyPhrase() {
        String[] phrase = {
                "No tienes que ser productivo para tener valor. Tu existencia ya es suficiente.",
                "Está bien pedir ayuda. La vulnerabilidad también es fortaleza.",
                "Permítete tener días lentos, tu mente también necesita descanso.",
                "Cada pequeño paso que tomas hacia el cuidado personal cuenta.",
                "No eres tus pensamientos. Observa, respira y sigue.",
                "El autocuidado no es un lujo, es una necesidad.",
                "Hoy no tiene que ser perfecto para que sea valioso.",
                "Sanar no siempre se nota por fuera, pero se siente con el tiempo.",
                "Decir 'no' también es una forma de decirte 'sí' a ti mismo.",
                "Recuerda: estás haciendo lo mejor que puedes con lo que tienes."
        };
        return phrase[(int) (Math.random() * phrase.length)];
    }




    private void showSelected(){
        topBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                Toast.makeText(getContext(), "logout", Toast.LENGTH_SHORT).show();
                logout();
                return true;
            }

            if (item.getItemId() == R.id.action_config) {
                Toast.makeText(getContext(), "Config", Toast.LENGTH_SHORT).show();

                ConfigBottomSheet bottomSheet = new ConfigBottomSheet();
                bottomSheet.show(getParentFragmentManager(), "ConfigBottomSheet");
                return true;
            }
            if (item.getItemId() == R.id.action_about) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Acerca de esta aplicación")
                        .setMessage("Esta aplicación fue desarrollada con el " +
                                "objetivo de ayudarte a crear una experiencia de relajación y enfoque. " +
                                "\n\nIncluye herramientas como fondos personalizables, modo oscuro, " +
                                "y módulos de respiración guiada. \n\nDesarrollador:" +
                                " \nJorge\nJose\nVersión: 1.0" +
                                "\n\nGracias por usar esta app. Tu bienestar mental es importante!!")
                        .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                        .show();

                return true;
            }
            return false;
        });
    }
    private void logout() {
        SharedPreferences securePrefs = SecurePrefsUtil.getEncryptedPreferences(getContext());
        securePrefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("current_user")
                .apply();

        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}