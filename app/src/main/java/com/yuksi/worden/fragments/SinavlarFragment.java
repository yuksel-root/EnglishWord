package com.yuksi.worden.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuksi.worden.R;
import com.yuksi.worden.ui.CreateQuiz;
import com.yuksi.worden.database.QuizDbHelper;
import com.yuksi.worden.database.WordsDbHelper;

import java.util.ArrayList;

import static com.yuksi.worden.fragments.KelimelerFragment.wordCount;
import static com.yuksi.worden.ui.AddWord.wordCounter;

public class SinavlarFragment extends Fragment {
    TextView highScoreText;
    Button startButton;
    TextView startQuizText;
    ImageView startQuizImage;
    Activity context;
    CreateQuiz createQuiz;
    WordsDbHelper wdbHelper;
    QuizDbHelper qdbHelper;
    private int wordCounts = 0;
    public static boolean started = false;
    Intent intent;
    ArrayList<Integer> highScoreArray = new ArrayList<>();
    private int highScores = 0;
    private int scores = 0;

    public SinavlarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sinavlar, container, false);
        qdbHelper = new QuizDbHelper(rootView.getContext());
        wdbHelper = new WordsDbHelper(rootView.getContext());
        startButton = rootView.findViewById(R.id.quiz_start_button);
        context = getActivity();
        startQuizImage = rootView.findViewById(R.id.quiz_start_image);
        startQuizText = rootView.findViewById(R.id.quiz_start_text);
        highScoreText = rootView.findViewById(R.id.quiz_higscore_text);
        startButton.setBackgroundColor(Color.GREEN);
        createQuiz = new CreateQuiz();
        wordCounts = 0;
        wordCounts = wordCounter;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordCounts > 3 || wordCount > 3) {
                    intent = new Intent(getActivity(), CreateQuiz.class);
                    startActivity(intent);
                    try {
                        if (wordCounts > 3 || wordCount > 3) {
                            started = true;
                            if (started == true) {
                                wdbHelper.fillQuestionsTable();
                                qdbHelper.CreateQuestions();
                                started = false;
                            }
                        } else {
                            Toast.makeText(getContext(), "En az 4 kelime ekleyiniz !", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "En az 4 kelime ekleyiniz !", Toast.LENGTH_LONG).show();
                        System.out.println("sinavlar fragment catch");
                    }
                } else {
                    Toast.makeText(getContext(), "En az 4 kelime ekleyiniz !", Toast.LENGTH_LONG).show();
                    intent = new Intent(getActivity(), AnasayfaFragment.class);
                }
            }
        });
        viewData();
        return rootView;
    }

    public void viewData() {
        Cursor c;
        highScoreArray = new ArrayList<>();
        try {
            c = qdbHelper.showDataQuizInfo();
            if (c.getCount() == 0) {
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
            System.out.println("SF hA size" + highScoreArray);
            for (int i = 0; i < highScoreArray.size(); i++) {
                if (scores > highScoreArray.get(i)) {
                    highScores = scores;
                } else {
                    highScoreArray.remove(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hsT = "HighScore: ";
        highScoreText.setText(hsT + highScores);
    }

}




