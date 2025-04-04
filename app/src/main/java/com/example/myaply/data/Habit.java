package com.example.myaply.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String frequency;

    // Constructor
    public Habit(String name, String frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) { this.frequency = frequency; }
}

