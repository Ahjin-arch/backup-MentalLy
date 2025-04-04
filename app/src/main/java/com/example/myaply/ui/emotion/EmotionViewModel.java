package com.example.myaply.ui.emotion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.myaply.data.AppDatabase;
import com.example.myaply.data.EmotionDao;
import com.example.myaply.data.EmotionEntry;

import java.util.List;

public class EmotionViewModel extends AndroidViewModel {
    private EmotionDao emotionDao;
    private LiveData<List<EmotionEntry>> emotions;

    public EmotionViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = Room.databaseBuilder(application,
                AppDatabase.class, "habit-db").build();
        emotionDao = db.emotionDao();
        emotions = emotionDao.getAllEmotions();
    }

    public LiveData<List<EmotionEntry>> getAllEmotions() {
        return emotions;
    }

    public void insertEmotion(EmotionEntry entry) {
        new Thread(() -> emotionDao.insertEmotion(entry)).start();
    }
}
