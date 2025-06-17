package com.example.myaply.ui.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.myaply.LoginActivity;
import com.example.myaply.SecurePrefsUtil;
import com.example.myaply.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button button=binding.btnLogout;
        button.setOnClickListener(v -> logout());

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void logout() {
        SharedPreferences securePrefs = SecurePrefsUtil.getEncryptedPreferences(getContext());
        securePrefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("current_user")
                .apply();

        startActivity(new Intent(getContext(), LoginActivity.class));
    }

}