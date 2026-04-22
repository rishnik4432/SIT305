package com.example.task51c.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task51c.R;
import com.example.task51c.adapters.FeaturedAdapter;
import com.example.task51c.adapters.NewsAdapter;
import com.example.task51c.data.DataProvider;
import com.example.task51c.models.NewsItem;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<NewsItem> allNews;
    private NewsAdapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        allNews = DataProvider.getNewsList();

        //Featured horizontal adapter
        List<NewsItem> featuredList = new ArrayList<>();
        for (NewsItem item : allNews) {
            if (item.isFeatured()) featuredList.add(item);
        }
        RecyclerView featuredRecycler = view.findViewById(R.id.featuredRecyclerView);
        FeaturedAdapter featuredAdapter = new FeaturedAdapter(featuredList, this::openDetail);
        featuredRecycler.setAdapter(featuredAdapter);

        //Vertical news list
        RecyclerView newsRecycler = view.findViewById(R.id.newsRecyclerView);
        newsAdapter = new NewsAdapter(allNews, this::openDetail);
        newsRecycler.setAdapter(newsAdapter);

        // Search filter
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
    private void filterNews(String query) {
        if (query == null || query.isEmpty()) {
            newsAdapter.updateList(allNews);
            return;
        }
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem item : allNews) {
            if (item.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        newsAdapter.updateList(filtered);
    }

    private void openDetail(NewsItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("news_item", item); //NewsItem must implement Serializable
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_detailFragment, bundle);
    }
}
