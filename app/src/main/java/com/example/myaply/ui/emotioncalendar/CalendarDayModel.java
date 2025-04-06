package com.example.myaply.ui.emotioncalendar;

public class CalendarDayModel {
    private int day;
    private String date;     // Formato: "2025-04-03"
    private String emotion;  // 😊 😢 😠 😴

    public CalendarDayModel(int day, String date, String emotion) {
        this.day = day;
        this.date = date;
        this.emotion = emotion;
    }

    // Getters y Setters
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}


