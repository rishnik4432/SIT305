package com.example.task51c.adapters;

import android.text.Layout;
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
public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder> {
    private List<NewsItem> featuredList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    public FeaturedAdapter(List<NewsItem> featuredList, OnItemClickListener listener) {
        this.featuredList = featuredList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = featuredList.get(position);
        holder.titleTextView.setText(item.getTitle());
        //Load image using Glide (assumes imageUrl is a drawable resource name)
        int resId = holder.itemView.getContext().getResources(). getIdentifier(item.getImageUrl(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(resId).placeholder(R.drawable.ic_launcher_foreground).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return featuredList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
