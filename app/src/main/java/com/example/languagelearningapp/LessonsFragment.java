package com.example.languagelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class LessonsFragment extends Fragment {

    private RecyclerView recyclerSuggested, recyclerAll;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        
        recyclerSuggested = view.findViewById(R.id.recyclerSuggested);
        recyclerAll = view.findViewById(R.id.recyclerAll);
        
        dbHelper = new DatabaseHelper(getContext());
        
        recyclerSuggested.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerAll.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadLessons();
        
        return view;
    }

    private void loadLessons() {
        // Daily Basis Lessons - Horizontal list of unlearned words
        ArrayList<WordModel> dailyLesson = dbHelper.getUnlearnedWords(8);
        WordAdapter horizontalAdapter = new WordAdapter(dailyLesson);
        recyclerSuggested.setAdapter(horizontalAdapter);
        
        // Complete Library
        ArrayList<WordModel> allWords = dbHelper.getAllWords();
        WordAdapter verticalAdapter = new WordAdapter(allWords);
        recyclerAll.setAdapter(verticalAdapter);
    }
}