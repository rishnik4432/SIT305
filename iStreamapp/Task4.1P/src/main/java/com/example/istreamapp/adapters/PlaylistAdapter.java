package com.example.istreamapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.istreamapp.R;
import com.example.istreamapp.data.database.PlaylistItem;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<PlaylistItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String url);
    }

    public PlaylistAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<PlaylistItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.videoUrlTextView.setText(item.getVideoUrl());
        holder.videoTitleTextView.setText("Video " + (position + 1));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item.getVideoUrl()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitleTextView, videoUrlTextView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitleTextView = itemView.findViewById(R.id.videoTitleTextView);
            videoUrlTextView = itemView.findViewById(R.id.videoUrlTextView);
        }
    }
}