package com.yuksi.worden.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yuksi.worden.dataclass.Questions;
import com.yuksi.worden.dataclass.Quiz;
import com.yuksi.worden.database.QuizDbHelper;
import com.yuksi.worden.MainActivity;
import com.yuksi.worden.R;
import com.yuksi.worden.fragments.SinavlarFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.yuksi.worden.database.QuizDbHelper.questionsList2;

public class CreateQuiz extends AppCompatActivity {
    QuizDbHelper qdbHelper;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;

    private TextView questionText;
    private TextView scoreText;
    private TextView questionCountText;
    private TextView trueText, falseText;

    private Questions currentQuestion;
    public static ArrayList<Questions> questionList;

    private int trueCount;
    private int falseCount;
    private int questionCounter;
    private int questionTotal;
    private int score;
    private int answerNr;

    private String quizDate;
    private static int quizNumberCounter;
    private int quizNumber;
    private int questionCount;
    private int quizTrue;
    private int quizFalse;
    private int quizScore;

    Questions questions2;
    private boolean clickAble = true;
    private int clickCount = 0;
    private SinavlarFragment SF = new SinavlarFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        setupUI();
        clickAble = true;
        SF = new SinavlarFragment();
        qdbHelper = new QuizDbHelper(getApplicationContext());
        questions2 = new Questions();
        questionList = new ArrayList<>();
        qdbHelper.getAllQuestions();
        for (int i = 0; i < questionsList2.size(); i++) {
            questions2 = questionsList2.get(i);
            questionList.add(questions2);
        }
        questionTotal = questionList.size();
        showNextQuestion();
    }

    public void button_click(View v) {
        Handler handler = new Handler();
        checkAnswer();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clickCount++;

                if (clickCount == 1) {
                    clickAble = true;

                    rb1.setEnabled(clickAble);
                    rb2.setEnabled(clickAble);
                    rb3.setEnabled(clickAble);
                    rb4.setEnabled(clickAble);
                    showNextQuestion();
                } else {
                    clickAble = false;

                    rb1.setEnabled(clickAble);
                    rb2.setEnabled(clickAble);
                    rb3.setEnabled(clickAble);
                    rb4.setEnabled(clickAble);
                }
            }
        }, 400);

    }

    private void checkAnswer() {
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr >= 1) {
            if (clickCount == 1) {
                clickAble = true;

                rb1.setEnabled(clickAble);
                rb2.setEnabled(clickAble);
                rb3.setEnabled(clickAble);
                rb4.setEnabled(clickAble);
            } else {
                clickAble = false;

                rb1.setEnabled(clickAble);
                rb2.setEnabled(clickAble);
                rb3.setEnabled(clickAble);
                rb4.setEnabled(clickAble);
            }
        }
        switch (answerNr) {
            case 1:
                rb1.setBackgroundColor(Color.RED);
                break;
            case 2:
                rb2.setBackgroundColor(Color.RED);
                break;
            case 3:
                rb3.setBackgroundColor(Color.RED);
                break;
            case 4:
                rb4.setBackgroundColor(Color.RED);
                break;
        }
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        RadioButton def = null;
        radioButtons.add(def);
        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);
        radioButtons.add(rb4);
        //System.out.println("secilen buttons" + radioButtons.get(answerNr).getText());
        //System.out.println("dogru button "+ currentQuestion.getAnswer());
        if (radioButtons.get(answerNr).getText().equals(currentQuestion.getAnswer())) {
            score += 5;
            // System.out.println("secilen button1" + radioButtons.get(answerNr).getText());
            // System.out.println("dogru button1 "+ currentQuestion.getAnswer());
            String scoreT = "Score: ";
            scoreText.setText(scoreT + score);
            trueCount = score / 5;
            String trueT = "True: ";
            trueText.setText(trueT + trueCount);
            radioButtons.get(answerNr).setBackgroundColor(Color.GREEN);
        } else {
            falseCount++;
            String falseT = "False: ";
            falseText.setText(falseT + falseCount);

        }
        if (!(questionCounter < questionTotal)) {
            Toast.makeText(this, "SINAV BİTTİ", Toast.LENGTH_SHORT).show();
        }

    }

    private void showNextQuestion() {
        rb1.setBackgroundColor(Color.WHITE);
        rb2.setBackgroundColor(Color.WHITE);
        rb3.setBackgroundColor(Color.WHITE);
        rb4.setBackgroundColor(Color.WHITE);
        rbGroup.clearCheck();

        if (questionCounter < questionTotal) {
            currentQuestion = questionList.get(questionCounter);

            questionText.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            clickCount = 0;
            String questionT = "Question: "; String t = "/";
            questionCountText.setText(questionT + questionCounter + t + questionTotal);
        } else {
            QuestionCount();
            QuizDate();
            QuizStat();
            addQuizInfos();
            Intent i;
            i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    private void addQuizInfos() {
        Quiz qx;
        qx = new Quiz(quizDate, quizNumber, questionCount, quizTrue, quizFalse, quizScore);
        if (qdbHelper.addQuizInfo(qx) == true) {
            System.out.println("CQ QUİZ INFOS EKLEME BASARILI ");
        } else System.out.println("CQ QUİZ INFOS EKLEME BASARISIZ ");

    }

    private String getNow() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyLocalizedPattern(" d MMM yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public void QuizDate() {
        Quiz q = new Quiz();
        q.setQuizDate(getNow());
        quizDate = getNow();
    }

    public void QuizStat() {
        Quiz q = new Quiz();
        q.setTrueCount(trueCount);
        q.setFalseCount(falseCount);
        q.setQuizScore(score);
        quizScore = score;
        quizTrue = trueCount;
        quizFalse = falseCount;
    }

    public void QuestionCount() {
        questionCount = questionTotal;
        Quiz q = new Quiz();
        q.setQuestionCount(questionCount);
    }

    private void setupUI() {
        questionText = findViewById(R.id.question_text);
        scoreText = findViewById(R.id.text_score);
        questionCountText = findViewById(R.id.text_question_count);
        trueText = findViewById(R.id.text_correct);
        falseText = findViewById(R.id.text_wrong);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
    }
}

