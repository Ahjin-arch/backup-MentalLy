package com.example.myaply.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emotions")
public class EmotionEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String emotion;
    public String date;


    public EmotionEntry(String emotion, String date) {
        this.emotion = emotion;
        this.date = date;
    }


}

