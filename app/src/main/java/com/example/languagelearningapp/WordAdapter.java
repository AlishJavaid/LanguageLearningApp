package com.example.languagelearningapp;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private ArrayList<WordModel> list;
    private TextToSpeech tts;

    public WordAdapter(ArrayList<WordModel> list) {
        this.list = list;
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView txtEnglish, txtUrdu, txtCategory;
        ImageView imgLearned;
        ImageButton btnSpeakItem;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEnglish = itemView.findViewById(R.id.txtEnglish);
            txtUrdu = itemView.findViewById(R.id.txtUrdu);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            imgLearned = itemView.findViewById(R.id.imgLearned);
            btnSpeakItem = itemView.findViewById(R.id.btnSpeakItem);
        }
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        
        // Initialize TTS
        if (tts == null) {
            tts = new TextToSpeech(parent.getContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            });
        }
        
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel model = list.get(position);
        holder.txtEnglish.setText(model.getEnglish());
        holder.txtUrdu.setText(model.getUrdu());
        holder.txtCategory.setText(model.getCategory());
        
        // Show checkmark if learned
        if (model.isLearned()) {
            holder.imgLearned.setVisibility(View.VISIBLE);
        } else {
            holder.imgLearned.setVisibility(View.GONE);
        }

        // Pronunciation button in list
        holder.btnSpeakItem.setOnClickListener(v -> {
            if (tts != null) {
                tts.speak(model.getEnglish(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
