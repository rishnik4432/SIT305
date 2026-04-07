package com.example.task41p;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity{
    private ProgressBar progressBar;
    private TextView tvQuestionNumber;
    private TextView tvQuestion;
    private RadioGroup radioGroup;
    private RadioButton[] radioOptions;
    private Button btnSubmit;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private boolean isSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Bind views
        progressBar = findViewById(R.id.progressBar);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        radioOptions = new RadioButton[]{
                findViewById(R.id.radioOption0),
                findViewById(R.id.radioOption1),
                findViewById(R.id.radioOption2),
                findViewById(R.id.radioOption3)
        };
        btnSubmit = findViewById(R.id.btnSubmit);

        //Create quiz questions
        questions.add(new Question("What is the capital of France?",
                new String[]{"Berlin", "Madrid", "Paris", "Lisbon"},2));
        questions.add(new Question("Which planet is known as the 'Red Planet'?",
                new String[]{"Earth", "Mars", "Jupiter", "Venus"},1));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?",
                new String[]{"Charles Dickens", "Mark Twain", "Jane Austen", "William Shakespeare"},3));
        questions.add(new Question("Which element has the chemical symbol 'O'?",
                new String[]{"Oxygen", "Gold", "Osmium", "Oganesson"}, 0));
        questions.add(new Question("What is the smallest country in the world?",
                new String[]{"Monaco", "San Marino", "Vatican City", "Malta"}, 2));

        progressBar.setMax(questions.size());
        loadQuestion();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSubmitted) {
                    //Submit answer
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int selectedIndex = -1;
                    for (int i =0; i < radioOptions.length; i++) {
                        if (radioOptions[i].getId() == selectedId) {
                            selectedIndex = i;
                            break;
                        }
                    }

                    Question currentQuestion = questions.get(currentIndex);
                    boolean isCorrect = (selectedIndex == currentQuestion.getCorrectOptionIndex());

                    if (isCorrect) {
                        score++;
                    }

                    //Visual feedback: correct option always green
                    radioOptions[currentQuestion.getCorrectOptionIndex()].setTextColor(Color.GREEN);
                    if (!isCorrect) {
                        radioOptions[selectedIndex].setTextColor(Color.RED);
                    }

                    //Disable all radio button
                    for (RadioButton rb : radioOptions) {
                        rb.setEnabled(false);
                    }

                    isSubmitted = true;

                    //Change button text
                    if (currentIndex == questions.size() - 1) {
                        btnSubmit.setText("View Results");
                    }else {
                        btnSubmit.setText("Next");
                    }
                } else {
                    //Move to next question of finish
                    if (currentIndex == questions.size() - 1) {
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        intent.putExtra("SCORE", score);
                        intent.putExtra("TOTAL", questions.size());
                        startActivity(intent);
                        finish();
                    } else {
                        currentIndex++;
                        loadQuestion();
                        isSubmitted = false;
                        btnSubmit.setText("Submit");
                        //Update progress bar: number of completed questions = currentIndex
                        progressBar.setProgress(currentIndex);
                    }
                }
            }
        });
    }
    private void loadQuestion() {
        Question q = questions.get(currentIndex);
        tvQuestion.setText(q.getText());
        tvQuestionNumber.setText("Question " + (currentIndex +1) + "/" + questions.size());

        //set option texts and reset colors/enabled state
        String[] opts = q.getOptions();
        for (int i =0; i < radioOptions.length; i++) {
            radioOptions[i].setText(opts[i]);
            radioOptions[i].setTextColor(Color.BLACK);
            radioOptions[i].setEnabled(true);
        }
        radioGroup.clearCheck();
    }
}
