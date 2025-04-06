package com.example.myaply.ui.emotioncalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    private List<CalendarDayModel> dayList;
    private OnDayClickListener listener;
    public interface OnDayClickListener {
        void onDayClick(CalendarDayModel day, int position);
    }

    public CalendarAdapter(List<CalendarDayModel> dayList, OnDayClickListener listener) {
        this.dayList = dayList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        CalendarDayModel day = dayList.get(position);
        holder.bind(day);
        holder.itemView.setOnClickListener(v -> {
            if (day.getDay()>0){
                listener.onDayClick(day,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDay;
        private TextView tvEmoji;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day_number);
            tvEmoji = itemView.findViewById(R.id.tv_emotion_emoji);
        }

        public void bind(CalendarDayModel day) {
            if (day.getDay() > 0) {
                tvDay.setText(String.valueOf(day.getDay()));
                tvEmoji.setText(day.getEmotion());
            } else {
                // Celdas vacías para días fuera del mes
                tvDay.setText("");
                tvEmoji.setText("");
            }
            //tambien se puede usar asi:
            //          tvDay.setText(day.getDay() > 0 ? String.valueOf(day.getDay()) : "");
            //          tvEmoji.setText(day.getEmotion() != null ? day.getEmotion() : "");
        }
    }
}

