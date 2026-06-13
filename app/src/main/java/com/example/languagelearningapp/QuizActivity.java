package com.example.languagelearningapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestion;
    private MaterialButton[] btnOptions = new MaterialButton[4];
    private LinearProgressIndicator progressIndicator;
    private ArrayList<WordModel> quizWords;
    private int currentIndex = 0;
    private int score = 0;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = findViewById(R.id.txtQuestion);
        btnOptions[0] = findViewById(R.id.btnOption1);
        btnOptions[1] = findViewById(R.id.btnOption2);
        btnOptions[2] = findViewById(R.id.btnOption3);
        btnOptions[3] = findViewById(R.id.btnOption4);
        progressIndicator = findViewById(R.id.quizProgressIndicator);

        dbHelper = new DatabaseHelper(this);
        loadQuizData();

        if (quizWords == null || quizWords.size() < 4) {
            Toast.makeText(this, "Add more words to start a quiz!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        progressIndicator.setMax(quizWords.size());
        showQuestion();
    }

    private void loadQuizData() {
        quizWords = dbHelper.getAllWords();
        Collections.shuffle(quizWords);
        // Limit to 10 questions
        if (quizWords.size() > 10) {
            quizWords = new ArrayList<>(quizWords.subList(0, 10));
        }
    }

    private void showQuestion() {
        WordModel currentWord = quizWords.get(currentIndex);
        txtQuestion.setText(currentWord.getEnglish());
        progressIndicator.setProgress(currentIndex + 1, true);

        // Prepare choices
        List<String> options = new ArrayList<>();
        options.add(currentWord.getUrdu()); // The right answer

        // Get random wrong answers
        ArrayList<WordModel> allWords = dbHelper.getAllWords();
        Collections.shuffle(allWords);
        for (WordModel w : allWords) {
            if (!w.getUrdu().equals(currentWord.getUrdu()) && !options.contains(w.getUrdu())) {
                options.add(w.getUrdu());
            }
            if (options.size() == 4) break;
        }

        Collections.shuffle(options);

        for (int i = 0; i < 4; i++) {
            btnOptions[i].setText(options.get(i));
            // Reset button styling for new question
            btnOptions[i].setBackgroundColor(ContextCompat.getColor(this, R.color.surface));
            btnOptions[i].setStrokeColor(ContextCompat.getColorStateList(this, R.color.outline));
            btnOptions[i].setTextColor(ContextCompat.getColor(this, R.color.onSurface));
            btnOptions[i].setEnabled(true);
            
            final String selectedAnswer = options.get(i);
            btnOptions[i].setOnClickListener(v -> checkAnswer(selectedAnswer, (MaterialButton) v));
        }
    }

    private void checkAnswer(String selected, MaterialButton clickedBtn) {
        String correct = quizWords.get(currentIndex).getUrdu();
        
        // Disable all options immediately
        for (MaterialButton btn : btnOptions) btn.setEnabled(false);

        if (selected.equals(correct)) {
            score++;
            clickedBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.success));
            clickedBtn.setTextColor(ContextCompat.getColor(this, R.color.onPrimary));
            dbHelper.markAsLearned(quizWords.get(currentIndex).getEnglish());
        } else {
            clickedBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.error));
            clickedBtn.setTextColor(ContextCompat.getColor(this, R.color.onPrimary));
            // Highlight correct answer in green
            for (MaterialButton btn : btnOptions) {
                if (btn.getText().toString().equals(correct)) {
                    btn.setBackgroundColor(ContextCompat.getColor(this, R.color.success));
                    btn.setTextColor(ContextCompat.getColor(this, R.color.onPrimary));
                }
            }
        }

        // Delay before moving to next question
        new Handler().postDelayed(() -> {
            currentIndex++;
            if (currentIndex < quizWords.size()) {
                showQuestion();
            } else {
                Toast.makeText(this, "Quiz Finished! Score: " + score + "/" + quizWords.size(), Toast.LENGTH_LONG).show();
                finish();
            }
        }, 1200);
    }
}
