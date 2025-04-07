package com.example.myaply.ui.emotion;

import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaply.R;
import com.example.myaply.data.EmotionEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmotionAdapter extends RecyclerView.Adapter<EmotionAdapter.EmotionViewHolder> {
    private List<EmotionEntry> emotionList = new ArrayList<>();

    public void setEmotionList(List<EmotionEntry> emotions) {
        this.emotionList = emotions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emotion, parent, false);
        return new EmotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmotionViewHolder holder, int position) {
        EmotionEntry entry = emotionList.get(position);
        holder.tvEmotion.setText(entry.getEmotion());
        holder.tvNote.setText(entry.getNote());
        holder.tvDate.setText(DateFormat.getDateTimeInstance().format(new Date(entry.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return emotionList.size();
    }

    static class EmotionViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmotion, tvNote, tvDate;

        public EmotionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmotion = itemView.findViewById(R.id.tv_emotion);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvDate = itemView.findViewById(R.id.tv_date);

        }
    }

}

