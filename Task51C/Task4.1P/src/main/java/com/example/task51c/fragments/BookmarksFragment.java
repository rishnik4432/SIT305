package com.example.task51c.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task51c.R;
import com.example.task51c.adapters.NewsAdapter;
import com.example.task51c.data.database.AppDatabase;
import com.example.task51c.data.database.Bookmark;
import com.example.task51c.models.NewsItem;
import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {
    private AppDatabase db;
    private NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        db = AppDatabase.getInstance(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.bookmarksRecyclerView);

        // Temporary empty adapter until data loads
        adapter = new NewsAdapter(new ArrayList<>(), this::openDetail);
        recyclerView.setAdapter(adapter);

        loadBookmarks();
        return view;
    }

    private void loadBookmarks() {
        new Thread(() -> {
            List<Bookmark> bookmarks = db.bookmarkDao().getAllBookmarks();
            List<NewsItem> newsItems = new ArrayList<>();
            for (Bookmark b : bookmarks) {
                newsItems.add(new NewsItem(b.getNewsId(), b.getTitle(), b.getDescription(), b.getImageUrl(), b.getCategory(), false));
            }
            requireActivity().runOnUiThread(() -> adapter.updateList(newsItems));
        }).start();
    }

    private void openDetail(NewsItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("news_item", item);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, DetailFragment.class, bundle)
                .addToBackStack(null)
                .commit();
    }
}
