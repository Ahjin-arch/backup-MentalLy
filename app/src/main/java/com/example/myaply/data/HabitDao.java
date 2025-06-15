package com.example.myaply.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HabitDao {

    @Insert
    void insertHabit(Habit habit);
    @Delete
    void deleteHabit(Habit habit);

    @Query("SELECT * FROM habits ORDER BY id DESC")
    LiveData<List<Habit>> getAllHabits();



    @Query("UPDATE habits SET progress = :newProgress WHERE id = :habitId")
    void updateProgress(int habitId, int newProgress);

}
