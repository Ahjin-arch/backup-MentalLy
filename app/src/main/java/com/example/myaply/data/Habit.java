package com.example.myaply.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private String frequency;

    private int progress;
    private boolean congratulated; // Nuevo campo


    // Constructor
    public Habit(String name,String description, String frequency) {
        this.name = name;
        this.frequency = frequency;
        this.description = description;
        this.progress = 0; // empieza en 0
    }

    // Getters y setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public boolean isCongratulated() {
        return congratulated;
    }

    public void setCongratulated(boolean congratulated) {
        this.congratulated = congratulated;
    }
}


