package com.example.task61d;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.ViewHolder> {
    private List<Flashcard> flashcardList;

    public static class Flashcard {
        public String front;
        public String back;
        public Flashcard(String front, String back) {
            this.front = front;
            this.back = back;
        }
    }

    public FlashcardAdapter(List<Flashcard> list) {
        this.flashcardList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flashcard card = flashcardList.get(position);
        holder.tvFront.setText(card.front);
        holder.tvBack.setText(card.back);
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFront, tvBack;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFront = itemView.findViewById(R.id.tvFront);
            tvBack = itemView.findViewById(R.id.tvBack);
        }
    }
}