package com.yuksi.worden.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yuksi.worden.dataclass.Questions;
import com.yuksi.worden.dataclass.Quiz;
import com.yuksi.worden.dataclass.Words;
import com.yuksi.worden.dataclass.TableContracts.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.yuksi.worden.database.WordsDbHelper.QuizArray;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Quiz";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    String randOption1;
    String randOption2;
    String randOption3;
    String randOption4;

    Questions questions = new Questions();
    ArrayList<String> randomOptArray = new ArrayList<>();
    ArrayList<String> randOptionArray = new ArrayList<>();
    ArrayList<Words> randquestionArray = new ArrayList<>();
    ArrayList<Integer> highScoreArray = new ArrayList<>();
    public static ArrayList<Questions> questionsList2 = new ArrayList<>();
    public ArrayList<Quiz> quizInfoList = new ArrayList<>();
    Words words = new Words();
    Words currentQuestion = new Words();
    byte[] byteArray;
    byte[] byteArray2;
    private int QuestionCount = 0;
    private int highScores = 0;
    private int scores = 0;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        byteArray = new byte[]{};
        byteArray2 = new byte[]{};
        this.db = db;
        try {
            final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                    QuestionTable.TABLE_NAME + " ( " +
                    QuestionTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    QuestionTable.COLUMN_QUESTION + " VARCHAR, " +
                    QuestionTable.COLUMN_OPTION1 + " VARCHAR, " +
                    QuestionTable.COLUMN_OPTION2 + " VARCHAR, " +
                    QuestionTable.COLUMN_OPTION3 + " VARCHAR, " +
                    QuestionTable.COLUMN_OPTION4 + " VARCHAR, " +
                    QuestionTable.COLUMN_ANSWER + " VARCHAR " +
                    ")";
            db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB Question CREATE TABLE  hatası");
        }
        try {
            final String SQL_CREATE_QUIZ_INFO_TABLE = "CREATE TABLE " +
                    QuizInfoTable.TABLE_NAME + " ( " +
                    QuizInfoTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    QuizInfoTable.COLUMN_QUIZ_DATE + " VARCHAR, " +
                    QuizInfoTable.COLUMN_QUIZ_NUMBER + " INTEGER, " +
                    QuizInfoTable.COLUMN_QUESTION_COUNT + " INTEGER, " +
                    QuizInfoTable.COLUMN_TRUE_COUNT + " INTEGER, " +
                    QuizInfoTable.COLUMN_FALSE_COUNT + " INTEGER, " +
                    QuizInfoTable.COLUMN_QUIZ_SCORE + " INTEGER " +
                    ")";
            db.execSQL(SQL_CREATE_QUIZ_INFO_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB QuizInfo Create Table Hatası");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    private String getNow() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyLocalizedPattern(" d MMM yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public boolean addQuestions(Questions questions) {
        try {
            ContentValues cv = new ContentValues();
            db = getWritableDatabase();
            cv.put(QuestionTable.COLUMN_QUESTION, questions.getQuestion());
            cv.put(QuestionTable.COLUMN_OPTION1, questions.getOption1());
            cv.put(QuestionTable.COLUMN_OPTION2, questions.getOption2());
            cv.put(QuestionTable.COLUMN_OPTION3, questions.getOption3());
            cv.put(QuestionTable.COLUMN_OPTION4, questions.getOption4());
            cv.put(QuestionTable.COLUMN_ANSWER, questions.getAnswer());
            //cv.put(QuestionTable.COLUMN_QUIZ_IMAGE,questions.getByteArrayQ());
            db.insert(QuestionTable.TABLE_NAME, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB EKLEME hatası");
            return false;
        }
        return true;
    }

    public boolean addQuizInfo(Quiz q2) {
        try {
            ContentValues cv = new ContentValues();
            db = getWritableDatabase();
            cv.put(QuizInfoTable.COLUMN_QUIZ_DATE, getNow());
            cv.put(QuizInfoTable.COLUMN_QUIZ_NUMBER, q2.getQuizNumber());
            cv.put(QuizInfoTable.COLUMN_QUESTION_COUNT, q2.getQuestionCount());
            cv.put(QuizInfoTable.COLUMN_TRUE_COUNT, q2.getTrueCount());
            cv.put(QuizInfoTable.COLUMN_FALSE_COUNT, q2.getFalseCount());
            cv.put(QuizInfoTable.COLUMN_QUIZ_SCORE, q2.getQuizScore());
            db.insert(QuizInfoTable.TABLE_NAME, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB add QUİZ İNFO  EKLEME hatası");
            return false;
        }
        return true;
    }


    public ArrayList<Questions> getAllQuestions() {
        Cursor c;
        try {
            db = getReadableDatabase();
            String[] Projection = {
                    QuestionTable.COLUMN_ID,
                    QuestionTable.COLUMN_QUESTION,
                    QuestionTable.COLUMN_OPTION1,
                    QuestionTable.COLUMN_OPTION2,
                    QuestionTable.COLUMN_OPTION3,
                    QuestionTable.COLUMN_OPTION4,
                    QuestionTable.COLUMN_ANSWER,
                    //QuestionTable.COLUMN_QUIZ_IMAGE
            };
            c = db.query(QuestionTable.TABLE_NAME,
                    Projection,
                    null,
                    null,
                    null,
                    null,
                    "id DESC",
                    "20");
            questionsList2 = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    Questions questions = new Questions();
                    questions.setQuestionId(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ID)));
                    questions.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                    questions.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                    questions.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                    questions.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                    questions.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                    questions.setAnswerNr(c.getString(c.getColumnIndex(QuestionTable.COLUMN_ANSWER)));
                    //questions.setByteArrayQ(c.getBlob(c.getColumnIndex(QuestionTable.COLUMN_QUIZ_IMAGE)));
                    //System.out.println("QDB_image = ");
                    //System.out.println((c.getBlob(c.getColumnIndex(QuestionTable.COLUMN_QUIZ_IMAGE))));
                    questionsList2.add(questions);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB getAllquestion list hatası");
        }
        //   System.out.println("QuestionList ARRAY size = " + questionsList2.size());
        ///  int k = 0;
        // for (Questions question : questionsList2) {
        //    System.out.println("QDB questionList ARRAY Soru" + k + "  " + question.getQuestion());
        //     System.out.println("QDB ARRAY qlistId = " + question.getQuestionId());
        //    k++;
        // }
        return questionsList2;
    }

    public ArrayList<Quiz> getAllQuiz() {
        Cursor c;
        try {
            db = getReadableDatabase();
            String[] Projection = {
                    QuizInfoTable.COLUMN_ID,
                    QuizInfoTable.COLUMN_QUIZ_DATE,
                    QuizInfoTable.COLUMN_QUIZ_NUMBER,
                    QuizInfoTable.COLUMN_QUESTION_COUNT,
                    QuizInfoTable.COLUMN_TRUE_COUNT,
                    QuizInfoTable.COLUMN_FALSE_COUNT,
                    QuizInfoTable.COLUMN_QUIZ_SCORE,
            };
            c = db.query(QuizInfoTable.TABLE_NAME,
                    Projection,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            quizInfoList = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    Quiz q2 = new Quiz();
                    q2.setQuizId(c.getInt(c.getColumnIndex(QuizInfoTable.COLUMN_ID)));
                    q2.setQuizDate(c.getString(c.getColumnIndex(QuizInfoTable.COLUMN_QUIZ_DATE)));
                    q2.setQuizNumber(c.getInt(c.getColumnIndex(QuizInfoTable.COLUMN_QUIZ_NUMBER)));
                    q2.setQuestionCount(c.getInt(c.getColumnIndex(QuizInfoTable.COLUMN_QUESTION_COUNT)));
                    q2.setTrueCount(c.getInt(c.getColumnIndex(QuizInfoTable.COLUMN_TRUE_COUNT)));
                    q2.setFalseCount(c.getInt(c.getColumnIndex(QuizInfoTable.COLUMN_FALSE_COUNT)));
                    q2.setQuizScore(c.getInt(c.getColumnIndex(QuizInfoTable.COLUMN_QUIZ_SCORE)));
                    quizInfoList.add(q2);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB QUİZ INFO list hatası");
        }
        //     System.out.println("QuizInfoList ARRAY size = " + quizInfoList.size());
        //     int k = 0;
        //    for (Quiz q2 : quizInfoList) {
        //         System.out.println("Quiz Id " + q2.getQuizId());
        //         System.out.println("Quiz Number" + q2.getQuizNumber());
        //        k++;
        //     }
        return quizInfoList;
    }

    public Cursor showDataQuestion() {
        db = this.getReadableDatabase();
        String sorgu = "SELECT * FROM " + QuestionTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(sorgu, null);
        return cursor;
    }

    public Cursor showDataQuizInfo() {
        db = this.getReadableDatabase();
        String sorgu = "SELECT * FROM " + QuizInfoTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(sorgu, null);
        return cursor;
    }

    public Integer getHighScore() {
        Cursor c;
        highScoreArray = new ArrayList<>();
        try {
            db = getReadableDatabase();
            c = showDataQuizInfo();
            if (c.getCount() == 0) {
                //System.out.println("HighScore için  VERİ YOK");
            } else {
                if (c.moveToFirst()) {
                    do {
                        scores = c.getInt(c.getColumnIndex("quizScore"));
                        if (scores > highScores) {
                            highScores = scores;
                            highScoreArray.add(highScores);
                        }
                    } while (c.moveToNext());
                }
                c.close();
            }
            //System.out.println("QDB HighScoreArray size = "+highScoreArray);
            for (int i = 0; i < highScoreArray.size(); i++) {
                if (scores > highScoreArray.get(i)) {
                    highScores = scores;
                } else {
                    highScoreArray.remove(i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HighScore çekme Hatası");
        }

        return highScores;
    }

    public void viewDataQDB() {
        Cursor c;
        try {
            db = getReadableDatabase();
            c = showDataQuestion();
            QuestionCount = c.getCount();
            if (c.getCount() == 0) {
                System.out.println("QDB TABLOSU VERİ YOK");
            } else {
                if (c.moveToFirst()) {
                    do {
                        System.out.println("vdQDB SoruId = " + (c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ID))));
                        System.out.println("vdQDB Soru = " + (c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION))));
                    } while (c.moveToNext());
                }
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QDB VERİ ÇEKME HATASI");
        }
    }

    public boolean deleteQuizInfo(int id) {
        try {
            db = this.getReadableDatabase();
            db.execSQL("DELETE FROM quizInfo WHERE id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Quiz Info silme hatası");
            return false;
        }
        return true;
    }

    public void clearQuizTable() {
        try {
            db = getReadableDatabase();
            db.delete(QuestionTable.TABLE_NAME, null, null);
            //  viewDataQDB();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SİLME QDB Question table silme hatası");
        }

    }

    public void CreateQuestions() {
        clearQuizTable();
        for (int j = 0; j < QuizArray.size(); j++) {
            words = QuizArray.get(j);
            randquestionArray.add(words);
            randomOptArray.add(words.getWordTr());
        }
        Collections.shuffle(randquestionArray);
        for (int k = 0; k < QuizArray.size(); k++) {
            currentQuestion = randquestionArray.get(k);
            String trueOption = currentQuestion.getWordTr();
            String Cquestions = currentQuestion.getWordEn();

            randomOptArray.remove(trueOption);
            Collections.shuffle(randomOptArray);

            randOption1 = randomOptArray.get(0);
            randOption2 = randomOptArray.get(1);
            randOption3 = randomOptArray.get(2);

            randOptionArray.add(randOption1);
            randOptionArray.add(randOption2);
            randOptionArray.add(randOption3);
            randOptionArray.add(trueOption);

            randomOptArray.add(trueOption);
            Collections.shuffle(randOptionArray);

            randOption1 = randOptionArray.get(0);
            randOption2 = randOptionArray.get(1);
            randOption3 = randOptionArray.get(2);
            randOption4 = randOptionArray.get(3);
            randOptionArray.clear();

            questions = new Questions(Cquestions, randOption1, randOption2, randOption3, randOption4, trueOption, byteArray);
            addQuestions(questions);
        }
    }


}


