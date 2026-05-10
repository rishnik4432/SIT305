package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {
    private EditText etLessonText;
    private Button btnSummarise;
    private ProgressBar progressBar;
    private TextView tvPrompt, tvResponse;
    private GeminiHelper geminiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etLessonText = findViewById(R.id.etLessonText);
        btnSummarise = findViewById(R.id.btnSummarise);
        progressBar = findViewById(R.id.progressBar);
        tvPrompt = findViewById(R.id.tvPrompt);
        tvResponse = findViewById(R.id.tvResponse);
        geminiHelper = new GeminiHelper(this);

        btnSummarise.setOnClickListener(v -> {
            String lesson = etLessonText.getText().toString().trim();
            if (lesson.isEmpty()) {
                tvResponse.setText("Please paste a lesson text.");
                return;
            }

            String prompt = "Summarise the following lesson in 3-5 sentences:\n\n" + lesson;
            tvPrompt.setText(prompt);

            progressBar.setVisibility(View.VISIBLE);
            btnSummarise.setEnabled(false);

            geminiHelper.askGemini(prompt, new GeminiHelper.Callback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSummarise.setEnabled(true);
                        tvResponse.setText(response);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSummarise.setEnabled(true);
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