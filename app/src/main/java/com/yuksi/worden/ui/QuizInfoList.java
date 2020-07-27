package com.yuksi.worden.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yuksi.worden.adapters.QuizInfoAdapter;
import com.yuksi.worden.dataclass.Quiz;
import com.yuksi.worden.database.QuizDbHelper;
import com.yuksi.worden.R;

import java.util.ArrayList;

public class QuizInfoList extends AppCompatActivity {
    private ListView quiz_List;
    private QuizDbHelper qDb;
    private ArrayList<Quiz> arrayList;
    private QuizInfoAdapter quizInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_info_list);
        qDb = new QuizDbHelper(getApplicationContext());
        arrayList = new ArrayList<>();
        quiz_List = findViewById(R.id.quiz_info_listview_ql);
        loadDataInListView();
        quiz_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), ShowQuizInfos.class);
                System.out.println("set Intent  QInfoLİST quizId = " + (arrayList.get(position).getQuizId()));
                intent.putExtra("quizId", (arrayList.get(position).getQuizId()));
                startActivity(intent);
            }
        });
    }

    private void loadDataInListView() {
        arrayList = qDb.getAllQuiz();
        quizInfoAdapter = new QuizInfoAdapter(this, arrayList);
        quiz_List.setAdapter(quizInfoAdapter);
        quizInfoAdapter.notifyDataSetChanged();
    }
}