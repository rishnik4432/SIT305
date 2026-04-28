package com.example.task71p;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public List<Post> postList;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int postId);
    }

    public PostAdapter(List<Post> postList, OnDeleteClickListener listener) {
        this.postList = postList;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.tvDesc.setText(post.getDescription());
        String catType = post.getCategory() + " - " + post.getType();
        holder.tvCategoryType.setText(catType);
        holder.tvDateTime.setText(post.getDateTime());

        if (post.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
            holder.ivImage.setImageBitmap(bitmap);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(post.getId()));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDesc, tvCategoryType, tvDateTime;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_description);
            tvCategoryType = itemView.findViewById(R.id.tv_category_type);
            tvDateTime = itemView.findViewById(R.id.tv_datetime);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public void updateList(List<Post> newList) {
        this.postList = newList;
        notifyDataSetChanged();
    }
}