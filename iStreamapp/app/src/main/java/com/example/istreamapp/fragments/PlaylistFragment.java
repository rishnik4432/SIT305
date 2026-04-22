package com.example.istreamapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.istreamapp.R;
import com.example.istreamapp.adapters.PlaylistAdapter;
import com.example.istreamapp.data.database.AppDatabase;
import com.example.istreamapp.data.database.PlaylistItem;
import com.example.istreamapp.utils.SessionManager;
import java.util.List;

public class PlaylistFragment extends Fragment {
    private AppDatabase db;
    private SessionManager sessionManager;
    private PlaylistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        db = AppDatabase.getInstance(getContext());
        sessionManager = new SessionManager(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlaylistAdapter(this::onVideoSelected);
        recyclerView.setAdapter(adapter);

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();
            NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
        });

        loadPlaylist();
        return view;
    }

    private void loadPlaylist() {
        int userId = sessionManager.getUserId();
        if (userId == -1) return;
        new Thread(() -> {
            List<PlaylistItem> items = db.playlistDao().getPlaylistForUser(userId);
            getActivity().runOnUiThread(() -> adapter.setItems(items));
        }).start();
    }

    private void onVideoSelected(String url) {
        // Go back to home and play the video
        Bundle bundle = new Bundle();
        bundle.putString("video_url", url);
        NavHostFragment.findNavController(this).navigate(R.id.homeFragment, bundle);
    }
}