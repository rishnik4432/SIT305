package com.example.task61d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {
    private EditText etName, etInterests, etHistory;
    private Button btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        etName = findViewById(R.id.etName);
        etInterests = findViewById(R.id.etInterests);
        etHistory = findViewById(R.id.etHistory);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String interests = etInterests.getText().toString().trim();
            String history = etHistory.getText().toString().trim();

            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("name", name.isEmpty() ? "Student" : name);
            editor.putString("interests", interests.isEmpty() ? "Math, Science" : interests);
            editor.putString("history", history.isEmpty() ? "Algebra, Photosynthesis" : history);
            editor.apply();

            startActivity(new Intent(SetupActivity.this, DashboardActivity.class));
            finish();
        });
    }
}