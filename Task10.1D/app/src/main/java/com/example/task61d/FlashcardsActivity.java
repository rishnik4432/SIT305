package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {
    private EditText etTopic;
    private Button btnGenerate;
    private ProgressBar progressBar;
    private TextView tvPrompt;
    private RecyclerView rvFlashcards;
    private FlashcardAdapter adapter;
    private List<FlashcardAdapter.Flashcard> flashcardList = new ArrayList<>();
    private GeminiHelper geminiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etTopic = findViewById(R.id.etTopic);
        btnGenerate = findViewById(R.id.btnGenerate);
        progressBar = findViewById(R.id.progressBar);
        tvPrompt = findViewById(R.id.tvPrompt);
        rvFlashcards = findViewById(R.id.rvFlashcards);
        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlashcardAdapter(flashcardList);
        rvFlashcards.setAdapter(adapter);

        geminiHelper = new GeminiHelper(this);

        btnGenerate.setOnClickListener(v -> {
            String topic = etTopic.getText().toString().trim();
            if (topic.isEmpty()) {
                tvPrompt.setText("Please enter a topic.");
                return;
            }

            String prompt = "Create 3 flashcards for the topic: " + topic +
                    ". Return exactly in this format: Front1|Back1,Front2|Back2,Front3|Back3. No extra text.";
            tvPrompt.setText(prompt);

            progressBar.setVisibility(View.VISIBLE);
            btnGenerate.setEnabled(false);

            geminiHelper.askGemini(prompt, new GeminiHelper.Callback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGenerate.setEnabled(true);
                        parseFlashcards(response);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGenerate.setEnabled(true);
                        tvPrompt.setText("Error: " + error);
                    });
                }
            });
        });
    }

    private void parseFlashcards(String raw) {
        flashcardList.clear();
        // Handle both comma-separated and double-pipe separated formats
        String[] pairs;
        if (raw.contains("||")) {
            pairs = raw.split("\\|\\|");
        } else {
            pairs = raw.split(",");
        }

        // Expected format: Front1|Back1,Front2|Back2,Front3|Back3
        for (String pair : pairs) {
            String[] parts = pair.split("\\|");
            if (parts.length == 2) {
                flashcardList.add(new FlashcardAdapter.Flashcard(parts[0].trim(), parts[1].trim()));
            } else if (parts.length == 1 && !pair.isEmpty()) {
                // fallback if format is wrong
                flashcardList.add(new FlashcardAdapter.Flashcard(pair.trim(), "Review this concept"));
            }
        }
        if (flashcardList.isEmpty()) {
            flashcardList.add(new FlashcardAdapter.Flashcard("What is " + etTopic.getText().toString().trim() + "?", "The main concept of " + etTopic.getText().toString().trim()));
            flashcardList.add(new FlashcardAdapter.Flashcard("Give an example", "Example helps understand the topic"));
            flashcardList.add(new FlashcardAdapter.Flashcard("Why is this important?", "It builds a strong foundation"));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}