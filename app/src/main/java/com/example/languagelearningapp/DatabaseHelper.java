package com.example.languagelearningapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "LanguageDB";
    private static final int DB_VERSION = 100; 

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE words (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "english TEXT," +
                        "urdu TEXT," +
                        "category TEXT," +
                        "is_learned INTEGER DEFAULT 0)"
        );
        insertInitialData(db);
    }

    private void insertWord(SQLiteDatabase db, String en, String ur, String cat) {
        ContentValues values = new ContentValues();
        values.put("english", en);
        values.put("urdu", ur);
        values.put("category", cat);
        values.put("is_learned", 0);
        db.insert("words", null, values);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // --- MASTERY (Complex & Rare Words) ---
        insertWord(db, "Ameliorate", "بہتر بنانا", "Mastery");
        insertWord(db, "Cognizant", "باخبر / آگاہ ہونا", "Mastery");
        insertWord(db, "Equanimity", "اطمینانِ قلب / سکون", "Mastery");
        insertWord(db, "Magnanimous", "عالی ظرف / فراخ دل", "Mastery");
        insertWord(db, "Pragmatic", "عملی / حقیقت پسندانہ", "Mastery");
        insertWord(db, "Ubiquitous", "ہمہ گیر / ہر جگہ موجود", "Mastery");
        insertWord(db, "Venerate", "تعظیم کرنا / احترام کرنا", "Mastery");
        insertWord(db, "Fastidious", "نق چڑھا / باریک بین", "Mastery");
        insertWord(db, "Inscrutable", "ناقابلِ فہم", "Mastery");
        insertWord(db, "Resilience", "لچک / ثابت قدمی", "Mastery");
        insertWord(db, "Paradigm", "مثال / نمونہ", "Mastery");
        insertWord(db, "Loquacious", "بسیار گو / باتونی", "Mastery");

        // --- PROFESSIONAL (Corporate & Business) ---
        insertWord(db, "Acquisition", "حصول / خریداری", "Professional");
        insertWord(db, "Collaboration", "تعاون / باہمی اشتراک", "Professional");
        insertWord(db, "Implementation", "نفاذ / عمل درآمد", "Professional");
        insertWord(db, "Infrastructure", "بنیادی ڈھانچہ", "Professional");
        insertWord(db, "Negotiation", "مذاکرات", "Professional");
        insertWord(db, "Sustainability", "پائیداری", "Professional");
        insertWord(db, "Synergy", "باہمی معاونت / ملاپ", "Professional");
        insertWord(db, "Accountability", "احتساب / جوابدہی", "Professional");

        // --- ACADEMIC (Research & Study) ---
        insertWord(db, "Empirical", "تجربی / مشاہداتی", "Academic");
        insertWord(db, "Hypothesis", "مفروضہ", "Academic");
        insertWord(db, "Methodology", "طریقہ کار", "Academic");
        insertWord(db, "Perspective", "نقطہ نظر", "Academic");
        insertWord(db, "Synthesis", "ترکیب / امتزاج", "Academic");
        insertWord(db, "Metacognition", "بالائے ادراک", "Academic");

        // --- IDIOMS (Advanced Phrases) ---
        insertWord(db, "Blessing in disguise", "زحمت میں رحمت", "Idioms");
        insertWord(db, "Beat around the bush", "ادھر ادھر کی باتیں کرنا", "Idioms");
        insertWord(db, "Cut to the chase", "اصل بات پر آنا", "Idioms");
        insertWord(db, "Hit the nail on the head", "بالکل درست بات کہنا", "Idioms");
        insertWord(db, "Piece of cake", "بہت آسان کام", "Idioms");

        // --- BASICS ---
        insertWord(db, "Knowledge", "علم", "Basics");
        insertWord(db, "Education", "تعلیم", "Basics");
        insertWord(db, "Progress", "ترقی", "Basics");
        insertWord(db, "Success", "کامیابی", "Basics");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS words");
        onCreate(db);
    }

    public ArrayList<WordModel> getAllWords() {
        ArrayList<WordModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM words ORDER BY english ASC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new WordModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) == 1
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<WordModel> searchWords(String query) {
        ArrayList<WordModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM words WHERE english LIKE ? OR urdu LIKE ? ORDER BY english ASC",
                new String[]{"%" + query + "%", "%" + query + "%"});
        if (cursor.moveToFirst()) {
            do {
                list.add(new WordModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) == 1
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<WordModel> getDailyLesson(int limit) {
        ArrayList<WordModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM words WHERE is_learned = 0 ORDER BY RANDOM() LIMIT ?", new String[]{String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new WordModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        false
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<WordModel> getUnlearnedWords(int limit) {
        ArrayList<WordModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM words WHERE is_learned = 0 LIMIT ?", new String[]{String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new WordModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        false
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<WordModel> getWordsByCategory(String category) {
        ArrayList<WordModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM words WHERE category = ?", new String[]{category});
        if (cursor.moveToFirst()) {
            do {
                list.add(new WordModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) == 1
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<String> getCategories() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT category FROM words ORDER BY category ASC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Map<String, Integer> getCategoryCounts() {
        Map<String, Integer> counts = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT category, COUNT(*) FROM words GROUP BY category", null);
        if (cursor.moveToFirst()) {
            do {
                counts.put(cursor.getString(0), cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return counts;
    }

    public void markAsLearned(String english) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_learned", 1);
        db.update("words", values, "english = ?", new String[]{english});
    }

    public int getLearnedCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM words WHERE is_learned = 1", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTotalCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM words", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}
