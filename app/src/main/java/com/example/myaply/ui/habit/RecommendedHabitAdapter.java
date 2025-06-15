package com.example.myaply.ui.habit;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;
import com.example.myaply.data.Habit;

import java.util.List;

public class RecommendedHabitAdapter extends RecyclerView.Adapter<RecommendedHabitAdapter.RecommendedHabitViewHolder> {
    private List<Habit> recommendedHabits;
    private HabitViewModel habitViewModel;
    public RecommendedHabitAdapter(List<Habit> recommendedHabits, HabitViewModel habitViewModel) {
        this.recommendedHabits = recommendedHabits;
        this.habitViewModel=habitViewModel;
    }

    @NonNull
    @Override
    public RecommendedHabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_habit, parent, false);
        return new RecommendedHabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedHabitViewHolder holder, int position) {
        Habit habit = recommendedHabits.get(position);
        holder.tvName.setText(habit.getName());
        holder.ivIcon.setImageResource(habit.getIcon());
        holder.tvDes.setText(habit.getDescription());
        // Aplicar tinte según el color asociado
        int colorRes;

        if (habit.getIcon()==R.drawable.ic_meditation){
            colorRes=R.color.icon_meditation_color;
        } else if (habit.getIcon()==R.drawable.ic_exercise) {
            colorRes = R.color.icon_exercise_color;
        }else if (habit.getIcon()==R.drawable.ic_reading) {
            colorRes = R.color.icon_reading_color;
        }else {
            colorRes = R.color.icon_default_color;
        }

        holder.ivIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), colorRes)));
        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirmar acción")
                    .setMessage("¿Quieres añadir el hábito " + habit.getName() + "?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        habitViewModel.insertHabit(habit);
                        Toast.makeText(holder.itemView.getContext(), "Hábito " + habit.getName() + " añadido", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }


    @Override
    public int getItemCount() {
        return recommendedHabits.size();
    }

    static class RecommendedHabitViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName,tvDes;

        public RecommendedHabitViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_recommended_habit_icon);
            tvName = itemView.findViewById(R.id.tv_recommended_habit_name);
            tvDes=itemView.findViewById(R.id.tv_recommended_habit_description);
        }
    }
}
