package com.example.myaply.ui.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.myaply.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ConfigBottomSheet extends BottomSheetDialogFragment {

    private Switch switchDarkMode;
    private RadioGroup radioGroupWallpaper;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_WALLPAPER="wallpaper";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bottom_sheet_config, container, false);

        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        radioGroupWallpaper=view.findViewById(R.id.radioGroupWallpaper);
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        boolean isDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        int savedWallpaperId=sharedPreferences.getInt(KEY_WALLPAPER,R.id.radioWallpaper1);

        switchDarkMode.setChecked(isDarkMode);
        radioGroupWallpaper.check(savedWallpaperId);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDarkMode(isChecked);
            saveDarkModeState(isChecked);
        });

        radioGroupWallpaper.setOnCheckedChangeListener((group, checkedId) -> {
                changeWallpaper(checkedId);
                saveWallpaperPreference(checkedId);
        });
        return view;
    }

    private void setDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
         requireActivity().recreate();
    }

    private void saveDarkModeState(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_DARK_MODE, isDarkMode);
        editor.apply();
    }
    private void changeWallpaper(int checkedId) {
        ImageView imageView=requireActivity().findViewById(R.id.imageViewWallpaper);
        if(checkedId==R.id.radioWallpaper1){
            imageView.setImageResource(R.drawable.bg_breath2);
        } else if (checkedId==R.id.radioWallpaper2) {
            imageView.setImageResource(R.drawable.bg_muscle2);
        }else if (checkedId==R.id.radioWallpaper3) {
            imageView.setImageResource(R.drawable.bg_sounds2);
        }else if (checkedId==R.id.radioWallpaper4) {
            imageView.setImageResource(R.drawable.bg_meditation2);
        }
    }
    private void saveWallpaperPreference(int checkedId) {
    SharedPreferences.Editor editor=sharedPreferences.edit();
    editor.putInt(KEY_WALLPAPER,checkedId);
    editor.apply();
    }

    }
