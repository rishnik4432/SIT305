package com.example.task61d;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudyPlanActivity extends AppCompatActivity {
    private Button btnGeneratePlan;
    private ProgressBar progressBar;
    private TextView tvPrompt, tvResponse;
    private GeminiHelper geminiHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyplan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGeneratePlan = findViewById(R.id.btnGeneratePlan);
        progressBar = findViewById(R.id.progressBar);
        tvPrompt = findViewById(R.id.tvPrompt);
        tvResponse = findViewById(R.id.tvResponse);
        geminiHelper = new GeminiHelper(this);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        btnGeneratePlan.setOnClickListener(v -> {
            String interests = prefs.getString("interests", "Math, Science");
            String history = prefs.getString("history", "Algebra, Photosynthesis");

            String prompt = "Based on these interests: " + interests +
                    " and past studied topics: " + history +
                    ", create a 7-day study plan. For each day, give a topic and a small activity. Keep it friendly and short.";
            tvPrompt.setText(prompt);

            progressBar.setVisibility(View.VISIBLE);
            btnGeneratePlan.setEnabled(false);

            geminiHelper.askGemini(prompt, new GeminiHelper.Callback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGeneratePlan.setEnabled(true);
                        tvResponse.setText(response);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGeneratePlan.setEnabled(true);
                        tvResponse.setText("Error: " + error);
                    });
                }
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}