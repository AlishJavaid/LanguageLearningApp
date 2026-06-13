package com.example.languagelearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProgressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        ProgressBar circularProgress = view.findViewById(R.id.circularProgress);
        TextView txtPercent = view.findViewById(R.id.txtPercent);
        TextView txtLearned = view.findViewById(R.id.txtLearned);
        TextView txtStreak = view.findViewById(R.id.txtStreak);
        TextView txtXp = view.findViewById(R.id.txtXp);
        
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int total = dbHelper.getTotalCount();
        int learned = dbHelper.getLearnedCount();
        
        int progress = (total > 0) ? (learned * 100) / total : 0;
        
        circularProgress.setProgress(progress);
        txtPercent.setText(getString(R.string.completed_percent_format, progress));
        txtLearned.setText(getString(R.string.words_learned_format, learned, total));
        
        // Static values for now, could be in DB later
        txtStreak.setText("5"); 
        txtXp.setText(String.valueOf(learned * 10));
        
        return view;
    }
}