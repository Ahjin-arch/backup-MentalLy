package com.example.myaply.ui.habit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;
import com.example.myaply.data.Habit;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habitList;

    public void setHabitList(List<Habit> habits) {
        this.habitList = habits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.tvName.setText(habit.getName());
        holder.tvFrequency.setText("Frecuencia: " + habit.getFrequency());
    }

    @Override
    public int getItemCount() {
        return habitList != null ? habitList.size() : 0;
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvFrequency;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_habit_name);
            tvFrequency = itemView.findViewById(R.id.tv_habit_frequency);
        }
    }
}
