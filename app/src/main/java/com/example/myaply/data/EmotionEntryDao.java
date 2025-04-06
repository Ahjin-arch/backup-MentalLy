package com.example.myaply.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmotionEntryDao {

    @Insert
    void insertEmotion(EmotionEntry entry);

    @Query("SELECT * FROM emotion_entries ORDER BY timestamp DESC")
    LiveData<List<EmotionEntry>> getAllEntries();

    @Query("SELECT * FROM emotion_entries WHERE date(timestamp / 1000, 'unixepoch') = date('now')")
    LiveData<List<EmotionEntry>> getTodayEntries();

    @Delete
    void deleteEmotion(EmotionEntry entry);
}
