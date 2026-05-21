package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AssessmentActivity extends AppCompatActivity {
    private TextView tvQuestion, tvExplanation;
    private RadioGroup radioGroup;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private GeminiHelper geminiHelper;

    // Dummy question data
    private String question = "What is the capital of France?";
    private String[] options = {"Berlin", "Madrid", "Paris", "Lisbon"};
    private String correctAnswer = "Paris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvQuestion = findViewById(R.id.tvQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        tvExplanation = findViewById(R.id.tvExplanation);

        tvQuestion.setText(question);
        rbOption1.setText(options[0]);
        rbOption2.setText(options[1]);
        rbOption3.setText(options[2]);
        rbOption4.setText(options[3]);

        geminiHelper = new GeminiHelper(this);

        btnSubmit.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                tvExplanation.setText("Please select an answer.");
                return;
            }
            RadioButton selectedRadio = findViewById(selectedId);
            String userAnswer = selectedRadio.getText().toString();
            boolean isCorrect = userAnswer.equals(correctAnswer);

            String prompt = "The question is: " + question +
                    "\nCorrect answer: " + correctAnswer +
                    "\nStudent answered: " + userAnswer +
                    "\nIs the student correct? Explain why the answer is " + (isCorrect ? "correct" : "incorrect") +
                    " and provide a short helpful feedback.";

            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);

            geminiHelper.askGemini(prompt, new GeminiHelper.Callback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSubmit.setEnabled(true);
                        tvExplanation.setText(response);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSubmit.setEnabled(true);
                        tvExplanation.setText("Error: " + error);
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