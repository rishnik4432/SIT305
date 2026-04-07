package com.example.task41p;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private Button btnStartQuiz;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        sharedPrefs = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE);
        String savedName = sharedPrefs.getString("USER_NAME", "");
        if (!savedName.isEmpty()) {
            etName.setText(savedName);
        }
        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etName.getText().toString().trim();
                if (userName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }else {
                    sharedPrefs.edit().putString("USER_NAME", userName).apply();
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}