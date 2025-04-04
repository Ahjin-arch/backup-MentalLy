package com.example.myaply.ui.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myaply.data.AppDatabase;
import com.example.myaply.data.Habit;
import com.example.myaply.data.HabitDao;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {

    private HabitDao habitDao;
    private LiveData<List<Habit>> allHabits;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        habitDao = db.habitDao();
        allHabits = habitDao.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public void insertHabit(Habit habit) {
        new Thread(() -> habitDao.insertHabit(habit)).start(); // Ejecutar en hilo para evitar bloquear la UI
    }
}
