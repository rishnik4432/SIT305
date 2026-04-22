package com.example.task51c.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.task51c.R;
import com.example.task51c.adapters.NewsAdapter;
import com.example.task51c.data.DataProvider;
import com.example.task51c.data.database.AppDatabase;
import com.example.task51c.data.database.Bookmark;
import com.example.task51c.models.NewsItem;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private NewsItem currentItem;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        db = AppDatabase.getInstance(getContext());

        // Get the passed news item
        if (getArguments() != null) {
            currentItem = (NewsItem) getArguments().getSerializable("news_item");
        }

        if (currentItem == null) {
            return view;
        }

        // Populate views
        ImageView imageView = view.findViewById(R.id.detailImage);
        TextView titleView = view.findViewById(R.id.detailTitle);
        TextView descView = view.findViewById(R.id.detailDescription);
        Button bookmarkButton = view.findViewById(R.id.bookmarkButton);

        int resId = getResources().getIdentifier(currentItem.getImageUrl(), "drawable", requireContext().getPackageName());
        Glide.with(this).load(resId).placeholder(R.drawable.ic_launcher_foreground).into(imageView);
        titleView.setText(currentItem.getTitle());
        descView.setText(currentItem.getDescription());

        //bookmark logic
        bookmarkButton.setOnClickListener(v -> {
            new Thread(() -> {
                Bookmark existing = db.bookmarkDao().getBookmarkByNewsId(currentItem.getId());
                if (existing == null) {
                    Bookmark bookmark = new Bookmark(currentItem.getId(), currentItem.getTitle(),
                            currentItem.getDescription(), currentItem.getImageUrl(), currentItem.getCategory());
                    db.bookmarkDao().insert(bookmark);
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Bookmarked", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Already bookmarked", Toast.LENGTH_SHORT).show());
                    }
                }
            }).start();
        });

        // Related stories (same category, exclude current)
        List<NewsItem> allNews = DataProvider.getNewsList();
        List<NewsItem> related = new ArrayList<>();
        for (NewsItem item : allNews) {
            if (item.getCategory().equals(currentItem.getCategory()) && item.getId() != currentItem.getId()) {
                related.add(item);
            }
        }
        RecyclerView relatedRecycler = view.findViewById(R.id.relatedRecyclerView);
        NewsAdapter relatedAdapter = new NewsAdapter(related, this::openRelatedDetail);
        relatedRecycler.setAdapter(relatedAdapter);

        return view;
    }

    private void openRelatedDetail(NewsItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("news_item", item);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, DetailFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }
}
