package com.example.task61d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "Student");
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome, " + name + "!");

        Button btnHint = findViewById(R.id.btnHint);
        Button btnExplain = findViewById(R.id.btnExplain);
        Button btnSummary = findViewById(R.id.btnSummary);
        Button btnFlashcards = findViewById(R.id.btnFlashcards);
        Button btnStudyPlan = findViewById(R.id.btnStudyPlan);
        Button btnAssessment = findViewById(R.id.btnAssessment);
        Button btnHistory = findViewById(R.id.btnHistory);
        Button btnSharing = findViewById(R.id.btnSharing);
        Button btnPurchase = findViewById(R.id.btnPurchase);

        btnHint.setOnClickListener(v -> startActivity(new Intent(this, HintActivity.class)));
        btnExplain.setOnClickListener(v -> startActivity(new Intent(this, ExplainActivity.class)));
        btnSummary.setOnClickListener(v -> startActivity(new Intent(this, SummaryActivity.class)));
        btnFlashcards.setOnClickListener(v -> startActivity(new Intent(this, FlashcardsActivity.class)));
        btnStudyPlan.setOnClickListener(v -> startActivity(new Intent(this, StudyPlanActivity.class)));
        btnAssessment.setOnClickListener(v -> startActivity(new Intent(this, AssessmentActivity.class)));
        btnHistory.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        btnSharing.setOnClickListener(v -> startActivity(new Intent(this, SharingActivity.class)));
        btnPurchase.setOnClickListener(v -> startActivity(new Intent(this, PurchaseActivity.class)));
    }
}