package com.example.istreamapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.istreamapp.R;
import com.example.istreamapp.data.database.AppDatabase;
import com.example.istreamapp.data.database.PlaylistItem;
import com.example.istreamapp.utils.SessionManager;

public class HomeFragment extends Fragment {
    private EditText urlEditText;
    private WebView webView;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        db = AppDatabase.getInstance(getContext());
        sessionManager = new SessionManager(getContext());

        urlEditText = view.findViewById(R.id.urlEditText);
        webView = view.findViewById(R.id.youtubeWebView);
        Button playButton = view.findViewById(R.id.playButton);
        Button addToPlaylist = view.findViewById(R.id.addToPlaylistButton);
        Button myPlaylist = view.findViewById(R.id.myPlaylistButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // Setup WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        playButton.setOnClickListener(v -> playVideo());
        addToPlaylist.setOnClickListener(v -> addToPlaylist());
        myPlaylist.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.playlistFragment));
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void playVideo() {
        String url = urlEditText.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(getContext(), "Enter a YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }
        String videoId = extractVideoId(url);
        if (videoId == null) {
            Toast.makeText(getContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }
        String embedHtml = "<html><body style='margin:0;padding:0'><iframe width='100%' height='100%' src='https://www.youtube.com/embed/" + videoId + "' frameborder='0' allowfullscreen></iframe></body></html>";
        webView.loadDataWithBaseURL(null, embedHtml, "text/html", "UTF-8", null);
    }

    private String extractVideoId(String url) {
        if (url.contains("youtu.be/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        } else if (url.contains("v=")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) {
                String id = parts[1];
                if (id.contains("&")) id = id.substring(0, id.indexOf("&"));
                return id;
            }
        }
        return null;
    }

    private void addToPlaylist() {
        String url = urlEditText.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(getContext(), "Enter a URL first", Toast.LENGTH_SHORT).show();
            return;
        }
        int userId = sessionManager.getUserId();
        if (userId == -1) return;

        new Thread(() -> {
            PlaylistItem item = new PlaylistItem(userId, url, url);
            db.playlistDao().insert(item);
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Added to playlist", Toast.LENGTH_SHORT).show());
        }).start();
    }

    private void logout() {
        sessionManager.clearSession();
        NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
    }
}