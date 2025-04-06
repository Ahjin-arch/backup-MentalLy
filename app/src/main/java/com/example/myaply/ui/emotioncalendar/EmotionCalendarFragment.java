package com.example.myaply.ui.emotioncalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EmotionCalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private List<CalendarDayModel> dayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion_calendar, container, false);
        recyclerView = view.findViewById(R.id.calendar_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7)); // 7 días por semana

        generateCalendar();
        adapter = new CalendarAdapter(dayList,((day, position) -> {
            showEmotionDialog(day,position);
        }));


        recyclerView.setAdapter(adapter);
        return view;
    }

    private void generateCalendar() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // Ir al primer día del mes
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 = domingo
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Agregar celdas vacías antes del primer día (si no empieza en lunes)
        int offset = firstDayOfWeek == 0 ? 6 : firstDayOfWeek - 1;
        for (int i = 0; i < offset; i++) {
            dayList.add(new CalendarDayModel(0, "", ""));
        }

        // Agregar los días reales del mes
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String date = sdf.format(calendar.getTime());
            dayList.add(new CalendarDayModel(day, date, "")); // emoción vacía por ahora
        }
    }

    private void showEmotionDialog(CalendarDayModel day, int position) {
        String[] emotions = {"😊", "😢", "😠", "😴", "😐"};
        new AlertDialog.Builder(getContext())
                .setTitle("¿Cómo te sentiste este día?")
                .setItems(emotions, (dialog, which) -> {
                    day.setEmotion(emotions[which]);
                    adapter.notifyItemChanged(position);
                })
                .show();
    }

}
