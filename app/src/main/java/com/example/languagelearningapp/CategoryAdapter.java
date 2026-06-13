package com.example.languagelearningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private Map<String, Integer> counts;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoryAdapter(List<String> categories, Map<String, Integer> counts, OnCategoryClickListener listener) {
        this.categories = categories;
        this.counts = counts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.txtCategoryName.setText(category);
        
        Integer count = counts.get(category);
        holder.txtCategoryCount.setText(String.format(holder.itemView.getContext().getString(R.string.category_count_format), count != null ? count : 0));
        
        // Dynamic coloring and icons based on curriculum categories
        int colorRes = R.color.secondaryContainer;
        int iconRes = android.R.drawable.ic_menu_agenda;
        
        switch (category) {
            case "Pro Mastery": 
                colorRes = R.color.cat_mastery; 
                iconRes = android.R.drawable.btn_star_big_on;
                break;
            case "Strategic Business": 
                colorRes = R.color.cat_professional; 
                iconRes = android.R.drawable.ic_menu_manage;
                break;
            case "Academic Discourse": 
                colorRes = R.color.cat_academic; 
                iconRes = android.R.drawable.ic_menu_edit;
                break;
            case "Advanced Idioms": 
                colorRes = R.color.cat_idioms; 
                iconRes = android.R.drawable.ic_menu_send;
                break;
            case "Core Fundamentals":
                colorRes = R.color.cat_basics; 
                iconRes = android.R.drawable.ic_menu_today;
                break;
        }
        
        holder.cardIcon.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), colorRes));
        holder.imgCategoryIcon.setImageResource(iconRes);
        holder.imgCategoryIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.onSurface));
        
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName, txtCategoryCount;
        MaterialCardView cardIcon;
        ImageView imgCategoryIcon;
        
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtCategoryCount = itemView.findViewById(R.id.txtCategoryCount);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            imgCategoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
        }
    }
}
