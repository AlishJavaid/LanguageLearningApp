package com.example.languagelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Daily Lesson Card
        TextView txtDailyLessonWord = view.findViewById(R.id.txtDailyLessonWord);
        MaterialButton btnStartDailyLesson = view.findViewById(R.id.btnStartDailyLesson);

        // Progress Tracking
        ProgressBar homeProgressBar = view.findViewById(R.id.homeProgressBar);
        TextView txtHomeProgress = view.findViewById(R.id.txtHomeProgress);
        TextView txtGoalStatus = view.findViewById(R.id.txtGoalStatus);
        
        // Categories
        RecyclerView recyclerCategories = view.findViewById(R.id.recyclerCategories);
        
        // Quick Actions
        MaterialButton btnStartQuiz = view.findViewById(R.id.btnStartQuiz);
        MaterialButton btnViewStats = view.findViewById(R.id.btnViewStats);
        
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        
        // Setup Daily Lesson
        ArrayList<WordModel> dailyWords = dbHelper.getDailyLesson(1);
        if (!dailyWords.isEmpty()) {
            WordModel lessonWord = dailyWords.get(0);
            txtDailyLessonWord.setText(getString(R.string.daily_lesson_format, lessonWord.getEnglish(), lessonWord.getUrdu()));
            
            btnStartDailyLesson.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), FlashcardActivity.class);
                intent.putExtra("CATEGORY", lessonWord.getCategory());
                startActivity(intent);
            });
        } else {
            txtDailyLessonWord.setText(R.string.all_caught_up);
            btnStartDailyLesson.setVisibility(View.GONE);
        }

        // Update Progress Tracking - Today's Goal (10 words)
        int learnedCount = dbHelper.getLearnedCount();
        int dailyGoal = 10;
        int progress = Math.min(learnedCount, dailyGoal);
        
        homeProgressBar.setMax(dailyGoal);
        homeProgressBar.setProgress(progress);
        txtGoalStatus.setText(getString(R.string.words_progress_format, progress, dailyGoal));
        
        if (progress >= dailyGoal) {
            txtHomeProgress.setText(R.string.goal_reached);
        } else {
            txtHomeProgress.setText(getString(R.string.goal_remaining_format, dailyGoal - progress));
        }

        // Vocabulary Categories from SQLite
        List<String> categories = dbHelper.getCategories();
        Map<String, Integer> categoryCounts = dbHelper.getCategoryCounts();
        
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter adapter = new CategoryAdapter(categories, categoryCounts, category -> {
            Intent intent = new Intent(getContext(), FlashcardActivity.class);
            intent.putExtra("CATEGORY", category);
            startActivity(intent);
        });
        recyclerCategories.setAdapter(adapter);

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QuizActivity.class);
            startActivity(intent);
        });

        btnViewStats.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProgressFragment())
                        .commit();
            }
        });

        return view;
    }
}