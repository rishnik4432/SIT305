package com.example.task51c.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.task51c.R;
import com.example.task51c.models.NewsItem;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsItem> newsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    public NewsAdapter(List<NewsItem> newsList, OnItemClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    public void updateList(List<NewsItem> newList) {
        this.newsList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = newsList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.descTextView.setText(item.getDescription());
        int resId = holder.itemView.getContext().getResources().getIdentifier(item.getImageUrl(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(resId)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, descTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
        }
    }
}