package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExplainActivity extends AppCompatActivity {
    private EditText etQuestion, etAnswer;
    private Button btnExplain;
    private ProgressBar progressBar;
    private TextView tvPrompt, tvResponse;
    private GeminiHelper geminiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etQuestion = findViewById(R.id.etQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        btnExplain = findViewById(R.id.btnExplain);
        progressBar = findViewById(R.id.progressBar);
        tvPrompt = findViewById(R.id.tvPrompt);
        tvResponse = findViewById(R.id.tvResponse);
        geminiHelper = new GeminiHelper(this);

        btnExplain.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            String answer = etAnswer.getText().toString().trim();

            if (question.isEmpty() || answer.isEmpty()) {
                tvResponse.setText("Please enter both the question and your answer.");
                return;
            }

            String prompt = "Question: " + question + "\nStudent's answer: " + answer +
                    "\n\nIs this answer correct? If yes, praise the student. If no, explain why it's incorrect and give the correct answer. Keep the explanation short.";
            tvPrompt.setText(prompt);

            progressBar.setVisibility(View.VISIBLE);
            btnExplain.setEnabled(false);

            geminiHelper.askGemini(prompt, new GeminiHelper.Callback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnExplain.setEnabled(true);
                        tvResponse.setText(response);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnExplain.setEnabled(true);
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