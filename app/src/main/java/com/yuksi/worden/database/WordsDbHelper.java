package com.yuksi.worden.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yuksi.worden.dataclass.Words;
import com.yuksi.worden.dataclass.TableContracts.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.yuksi.worden.database.QuizDbHelper.questionsList2;
import static com.yuksi.worden.ui.CreateQuiz.questionList;

public class WordsDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "English";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    ContentValues cv;
    private int wordCounts = 0;
    public static ArrayList<Words> QuizArray;

    public WordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        cv = new ContentValues();
        final String SQL_CREATE_WORDS_TABLE = "CREATE TABLE " +
                WordsTable.TABLE_NAME + " ( " +
                WordsTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WordsTable.COLUMN_WORD_EN + " VARCHAR, " +
                WordsTable.COLUMN_WORD_TR + " VARCHAR, " +
                WordsTable.COLUMN_SENTENCE_EN + " VARCHAR, " +
                WordsTable.COLUMN_SENTENCE_TR + " VARCHAR, " +
                WordsTable.COLUMN_WORD_DATE + " TEXT, " +
                WordsTable.COLUMN_WORD_IMAGE + " BLOB " +
                ")";
        db.execSQL(SQL_CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS words");
        onCreate(db);
    }

    private String getNow() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyLocalizedPattern(" d MMM yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public boolean addWord(Words words) {
        try {
            db = this.getWritableDatabase();
            cv = new ContentValues();
            cv.put(WordsTable.COLUMN_WORD_EN, words.getWordEn());
            cv.put(WordsTable.COLUMN_WORD_TR, words.getWordTr());
            cv.put(WordsTable.COLUMN_SENTENCE_EN, words.getSentenceEn());
            cv.put(WordsTable.COLUMN_SENTENCE_TR, words.getSentenceTr());
            cv.put(WordsTable.COLUMN_WORD_IMAGE, words.getByteArray());
            cv.put(WordsTable.COLUMN_WORD_DATE, getNow());
            db.insert(WordsTable.TABLE_NAME, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EKLEME addWord sqlhelper hata");
            return false;
        }
        return true;
    }

    public boolean uptadeWord(int id, Words words) {
        try {
            db = this.getWritableDatabase();
            cv = new ContentValues();
            cv.put(WordsTable.COLUMN_WORD_EN, words.getWordEn());
            cv.put(WordsTable.COLUMN_WORD_TR, words.getWordTr());
            cv.put(WordsTable.COLUMN_SENTENCE_EN, words.getSentenceEn());
            cv.put(WordsTable.COLUMN_SENTENCE_TR, words.getSentenceTr());
            cv.put(WordsTable.COLUMN_WORD_IMAGE, words.getByteArray());
            db.execSQL("UPDATE words set wordDate = datetime('now','3 hour') WHERE id=" + id);
            db.update(WordsTable.TABLE_NAME, cv, "id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GÜNCELLEME uptdateWord sqlHelper hata");
            return false;
        }
        return true;
    }

    public boolean deleteWord(int id) {
        try {
            db = this.getReadableDatabase();
            db.execSQL("DELETE FROM words WHERE id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            System.out.println("delete word hata");
            return false;
        }

        return true;
    }

    public Cursor showData() {
        db = this.getReadableDatabase();
        String sorgu = "SELECT * FROM " + WordsTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(sorgu, null);
        return cursor;
    }

    public void viewDataWDB() {
        Cursor c;
        try {
            db = getReadableDatabase();
            c = showData();
            wordCounts = c.getCount();
            if (c.getCount() == 0) {
                System.out.println("WORD DB TABLOSU VERİ YOK");
            } else {
                if (c.moveToFirst()) {
                    do {
                        System.out.println("vdWDB SoruId = " + (c.getInt(c.getColumnIndex(WordsTable.COLUMN_ID))));
                        System.out.println("vdWDB Soru = " + (c.getString(c.getColumnIndex(WordsTable.COLUMN_SENTENCE_EN))));
                    } while (c.moveToNext());
                }
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WDB  VERİ ÇEKME HATASI");
        }
    }

    public void clearWordArray() {
        try {
            if (QuizArray.size() > 0) {
                db = getReadableDatabase();
                //  viewDataWDB();
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SİLME word table silme hatası");
        }
        try {
            if (QuizArray.size() > 0) {
                QuizArray.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SİLME quiz array silme hatası");
        }
        try {
            if (questionList.size() > 0) {
                questionList.clear();
                questionsList2.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SİLME questionList array silme hatası");
        }

    }

    public void fillQuestionsTable() {
        clearWordArray();
        try {
            db = getReadableDatabase();
            String[] Projection = {
                    WordsTable.COLUMN_ID,
                    WordsTable.COLUMN_WORD_EN,
                    WordsTable.COLUMN_WORD_TR,
                    WordsTable.COLUMN_SENTENCE_EN,
                    WordsTable.COLUMN_SENTENCE_TR,
                    WordsTable.COLUMN_WORD_DATE,
                    WordsTable.COLUMN_WORD_IMAGE
            };
            Cursor c = db.query(WordsTable.TABLE_NAME,
                    Projection,
                    null,
                    null,
                    null,
                    null,
                    "id DESC",
                    "20");
            QuizArray = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    Words words = new Words();
                    words.setWordId(c.getInt(c.getColumnIndex(WordsTable.COLUMN_ID)));
                    words.setWordEn(c.getString(c.getColumnIndex(WordsTable.COLUMN_WORD_EN)));
                    words.setWordTr(c.getString(c.getColumnIndex(WordsTable.COLUMN_WORD_TR)));
                    words.setWordDate(c.getString(c.getColumnIndex(WordsTable.COLUMN_WORD_DATE)));
                    words.setSentenceEn(c.getString(c.getColumnIndex(WordsTable.COLUMN_SENTENCE_EN)));
                    words.setSentenceTr(c.getString(c.getColumnIndex(WordsTable.COLUMN_SENTENCE_TR)));
                    words.setByteArray(c.getBlob(c.getColumnIndex(WordsTable.COLUMN_WORD_IMAGE)));
                    //System.out.print("setByteArray = ");
                    //System.out.print(c.getBlob(c.getColumnIndex(wordsTable.COLUMN_WORD_IMAGE)));
                    QuizArray.add(words);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WDB Soruları çekme hatası");
        }
        //for(Words words :QuizArray){
        //   System.out.println("Quiz ARRAY  WID = "+words.getWordId());
        //       System.out.println("Quiz ARRAY WEN DB  ="+words.getWordEn());
        //  }
    }


}

