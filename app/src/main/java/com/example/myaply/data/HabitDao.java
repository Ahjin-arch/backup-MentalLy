package com.example.myaply.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HabitDao {

    @Insert
    void insertHabit(Habit habit);

    @Query("SELECT * FROM habits ORDER BY id DESC")
    LiveData<List<Habit>> getAllHabits();
}
