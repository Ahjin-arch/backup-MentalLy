package com.example.myaply.ui.habit;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        holder.tvFrequency.setText(habit.getFrequency());
        holder.progressBar.setProgress(habit.getProgress());
        if (habit.getIcon() != 0) {
            holder.ivIcon.setImageResource(habit.getIcon());
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_habit_default);
        }
        int colorRes;

        if (habit.getIcon()==R.drawable.ic_meditation){
            colorRes=R.color.icon_meditation_color;
        } else if (habit.getIcon()==R.drawable.ic_exercise) {
            colorRes = R.color.icon_exercise_color;
        }else if (habit.getIcon()==R.drawable.ic_reading) {
            colorRes = R.color.icon_reading_color;
        }else if (habit.getIcon()==R.drawable.ic_sleep) {
            colorRes = R.color.icon_sleep_color;
        }else {
            colorRes = R.color.icon_default_color;
        }

        holder.ivIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), colorRes)));


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
        ImageView ivIcon;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_habit_name);
            tvDes=itemView.findViewById(R.id.tv_habit_description);
            tvFrequency = itemView.findViewById(R.id.tv_habit_frequency);
            progressBar = itemView.findViewById(R.id.progress_habit);
            btnHecho = itemView.findViewById(R.id.btn_habit_complete);
            ivIcon=itemView.findViewById(R.id.iv_habit_icon);
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
