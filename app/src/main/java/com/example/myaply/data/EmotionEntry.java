package com.example.myaply.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emotion_entries")
public class EmotionEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String emotion;
    private String note;
    private long timestamp;

    public EmotionEntry(String emotion, String note, long timestamp) {
        this.emotion = emotion;
        this.note = note;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
