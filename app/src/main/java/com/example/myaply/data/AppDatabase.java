package com.example.myaply.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Habit.class, EmotionEntry.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract HabitDao habitDao();
    public abstract EmotionDao emotionDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "habit_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}