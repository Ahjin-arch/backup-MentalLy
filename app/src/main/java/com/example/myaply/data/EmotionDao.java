package com.example.myaply.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmotionDao {
    @Insert
    void insertEmotion(EmotionEntry entry);

    @Query("SELECT * FROM emotions ORDER BY date DESC")
    LiveData<List<EmotionEntry>> getAllEmotions();
}
