package com.example.myaply.ui.emotion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.myaply.data.AppDatabase;
import com.example.myaply.data.EmotionEntry;
import com.example.myaply.data.EmotionEntryDao;

import java.util.List;

public class EmotionViewModel extends AndroidViewModel {
    private EmotionEntryDao emotionEntryDao;
    private LiveData<List<EmotionEntry>> allEntries;

    public EmotionViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        emotionEntryDao = db.emotionEntryDao();
        allEntries = emotionEntryDao.getAllEntries();
    }

    public LiveData<List<EmotionEntry>> getAllEntries() {
        return allEntries;
    }

    public void insertEmotion(EmotionEntry entry) {
        new Thread(() -> emotionEntryDao.insertEmotion(entry)).start();
    }
    public void deleteEmotion(EmotionEntry entry) {
        new Thread(() -> emotionEntryDao.deleteEmotion(entry)).start();
    }

}
