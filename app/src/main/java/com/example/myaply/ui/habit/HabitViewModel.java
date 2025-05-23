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
    public void deleteHabit(Habit habit) {
        new Thread(() -> habitDao.deleteHabit(habit)).start();
    }

    public void markHabitAsDone(Habit habit) {
        new Thread(() -> {
            int newProgress = habit.getProgress() + 10; // o el valor que quieras aumentar
            if (newProgress > 100) newProgress = 100; // para no pasar de 100
            habitDao.updateProgress(habit.getId(), newProgress);
        }).start();
    }

    public void insertHabit(Habit habit) {
        new Thread(() -> habitDao.insertHabit(habit)).start();
    }



}
