package com.example.languagelearningapp;

public class WordModel {

    private String english;
    private String urdu;
    private String category;
    private boolean isLearned;

    public WordModel(String english, String urdu, String category) {
        this(english, urdu, category, false);
    }

    public WordModel(String english, String urdu, String category, boolean isLearned) {
        this.english = english;
        this.urdu = urdu;
        this.category = category;
        this.isLearned = isLearned;
    }

    public String getEnglish() {
        return english;
    }

    public String getUrdu() {
        return urdu;
    }

    public String getCategory() {
        return category;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }
}
