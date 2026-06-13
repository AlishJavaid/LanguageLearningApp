package com.example.languagelearningapp;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class FlashcardActivity extends AppCompatActivity {

    TextView txtWord, txtMeaning;
    Button btnShow, btnNext, btnPrevious, btnSpeak, btnLearned;
    int currentIndex = 0;
    TextToSpeech textToSpeech;
    ArrayList<WordModel> wordList;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // UI Setup
        txtWord = findViewById(R.id.txtWord);
        txtMeaning = findViewById(R.id.txtMeaning);
        btnShow = findViewById(R.id.btnShow);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnLearned = findViewById(R.id.btnLearned);

        dbHelper = new DatabaseHelper(this);

        // Get Category from Intent
        String category = getIntent().getStringExtra("CATEGORY");
        
        if (category != null && !category.isEmpty()) {
            wordList = dbHelper.getWordsByCategory(category);
            setTitle(category + " Flashcards");
        } else {
            wordList = dbHelper.getAllWords();
            setTitle("All Flashcards");
        }

        if (wordList == null || wordList.isEmpty()) {
            Toast.makeText(this, "No words found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        showWord();

        btnShow.setOnClickListener(v -> {
            txtMeaning.setText(wordList.get(currentIndex).getUrdu());
            txtMeaning.setVisibility(View.VISIBLE);
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < wordList.size() - 1) {
                currentIndex++;
                showWord();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showWord();
            }
        });

        btnSpeak.setOnClickListener(v -> {
            textToSpeech.speak(wordList.get(currentIndex).getEnglish(), TextToSpeech.QUEUE_FLUSH, null, null);
        });

        btnLearned.setOnClickListener(v -> {
            dbHelper.markAsLearned(wordList.get(currentIndex).getEnglish());
            Toast.makeText(this, "Marked as learned!", Toast.LENGTH_SHORT).show();
        });
    }

    private void showWord() {
        txtWord.setText(wordList.get(currentIndex).getEnglish());
        txtMeaning.setText("");
        txtMeaning.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
