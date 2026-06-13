package com.example.languagelearningapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class DictionaryFragment extends Fragment {

    private RecyclerView recyclerView;
    private WordAdapter adapter;
    private ArrayList<WordModel> wordList;
    private DatabaseHelper dbHelper;
    private TextView txtNoResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        recyclerView = view.findViewById(R.id.recyclerDictionary);
        txtNoResults = view.findViewById(R.id.txtNoResults);
        TextInputEditText etSearch = view.findViewById(R.id.etSearch);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initial load of complete dictionary
        loadCompleteDictionary();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadCompleteDictionary() {
        wordList = dbHelper.getAllWords();
        updateUI(wordList);
    }

    private void performSearch(String query) {
        ArrayList<WordModel> results;
        if (query.isEmpty()) {
            results = dbHelper.getAllWords();
        } else {
            results = dbHelper.searchWords(query);
        }
        updateUI(results);
    }

    private void updateUI(ArrayList<WordModel> list) {
        if (list == null || list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            if (txtNoResults != null) txtNoResults.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (txtNoResults != null) txtNoResults.setVisibility(View.GONE);
            adapter = new WordAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    }
}
