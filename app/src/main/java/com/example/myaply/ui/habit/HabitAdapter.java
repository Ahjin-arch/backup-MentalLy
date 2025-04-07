package com.example.myaply.ui.habit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;
import com.example.myaply.data.Habit;

import java.util.ArrayList;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habitList=new ArrayList<>();;

    private OnHabitDoneClickListener onHabitDoneClickListener;

    public interface OnHabitDoneClickListener {
        void onHabitDone(Habit habit);
    }

    public void setOnHabitDoneClickListener(OnHabitDoneClickListener listener) {
        this.onHabitDoneClickListener = listener;
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
        holder.tvDes.setText(habit.getDescription());
        holder.tvFrequency.setText("Frecuencia: " + habit.getFrequency());
        holder.progressBar.setProgress(habit.getProgress());

        if (habit.getProgress() >= 100) {
            holder.btnHecho.setEnabled(false); // desactivar botón si ya está completo
        }else{
            holder.btnHecho.setEnabled(true);
        }
        holder.btnHecho.setOnClickListener(v -> {
            if (onHabitDoneClickListener != null) {
                onHabitDoneClickListener.onHabitDone(habit);
            }
        });


        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onHabitLongClick(habit);
                return true;
            }
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return habitList != null ? habitList.size() : 0;
    }

    public void setHabitList(List<Habit> habits) {
        this.habitList = habits;
        notifyDataSetChanged();
    }

    // aqui se estable la conexion con los elementos del xml
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvFrequency,tvDes;
        ProgressBar progressBar;
        Button btnHecho;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_habit_name);
            tvDes=itemView.findViewById(R.id.tv_habit_description);
            tvFrequency = itemView.findViewById(R.id.tv_habit_frequency);
            progressBar = itemView.findViewById(R.id.progress_habit);
            btnHecho = itemView.findViewById(R.id.btn_habit_complete);

        }
    }


    public interface OnHabitLongClickListener {
        void onHabitLongClick(Habit habit);
    }

    private OnHabitLongClickListener longClickListener;

    public void setOnHabitLongClickListener(OnHabitLongClickListener listener) {
        this.longClickListener = listener;
    }

}
