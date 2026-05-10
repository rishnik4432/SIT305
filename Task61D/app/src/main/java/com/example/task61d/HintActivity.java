package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HintActivity extends AppCompatActivity {
    private EditText etQuestion;
    private Button btnGetHint;
    private ProgressBar progressBar;
    private TextView tvPrompt, tvResponse;
    private GeminiHelper geminiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enables back button

        etQuestion = findViewById(R.id.etQuestion);
        btnGetHint = findViewById(R.id.btnGetHint);
        progressBar = findViewById(R.id.progressBar);
        tvPrompt = findViewById(R.id.tvPrompt);
        tvResponse = findViewById(R.id.tvResponse);
        geminiHelper = new GeminiHelper(this);

        btnGetHint.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            if (question.isEmpty()) {
                tvResponse.setText("Please enter a question.");
                return;
            }

            String prompt = "Generate a helpful hint for the following question (do not give the full answer):\n\n" + question;
            tvPrompt.setText(prompt); // show the exact prompt sent

            progressBar.setVisibility(View.VISIBLE);
            btnGetHint.setEnabled(false);

            geminiHelper.askGemini(prompt, new GeminiHelper.Callback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGetHint.setEnabled(true);
                        tvResponse.setText(response);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGetHint.setEnabled(true);
                        tvResponse.setText("Error: " + error);
                    });
                }
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // handles back arrow
        return true;
    }
}