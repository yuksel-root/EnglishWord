package com.yuksi.worden.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.yuksi.worden.database.QuizDbHelper;
import com.yuksi.worden.R;

public class ShowQuizInfos extends AppCompatActivity {
    int id;
    String info;
    private TextView sinavDateText;
    private TextView questionCountText;
    private TextView trueCountText;
    private TextView falseCountText;
    private TextView QscoreText;
    private TextView quizNumberText;
    private SQLiteDatabase database;
    QuizDbHelper qdB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_infos);
        qdB = new QuizDbHelper(getApplicationContext());
        database = this.openOrCreateDatabase("Quiz", MODE_PRIVATE, null);
        setupUI();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM  quizInfo WHERE id = ?", new String[]{String.valueOf(id)});
            while (cursor.moveToNext()) {
                String qDate = (cursor.getString(cursor.getColumnIndex("quizDate")));
                int quizId = cursor.getInt(cursor.getColumnIndex("id"));
                int qCount = (cursor.getInt(cursor.getColumnIndex("questionCount")));
                int tCount = (cursor.getInt(cursor.getColumnIndex("trueCount")));
                int fCount = (cursor.getInt(cursor.getColumnIndex("falseCount")));
                int qScore = (cursor.getInt(cursor.getColumnIndex("quizScore")));
                System.out.println("SHOW INFOS CEKILEN VERİLER...");
                System.out.println("date" + qDate);
                System.out.println("qnumber=" + quizId + " qCount=" + qCount + " qScore=" + qScore);
                sinavDateText.setText(qDate);
                String qn = "Quiz Number: ";
                String qc = "Question Count: ";
                String tc = "True Count: ";
                String fc = "False Count: ";
                String qs = "Quiz Score: ";
                quizNumberText.setText(qn + (quizId));
                questionCountText.setText(qc + (qCount));
                trueCountText.setText(tc + (tCount));
                falseCountText.setText(fc + (fCount));
                QscoreText.setText(qs + (qScore));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Quiz info SqI Çekme HATASI");
        }
    }

    Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_quiz_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        intent = new Intent();
        if (item.getItemId() == R.id.delete_quiz_item) {
            intent = new Intent(ShowQuizInfos.this, QuizInfoList.class);
            qdB.deleteQuizInfo(id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("quizId", 1);
        System.out.println("QuizId intent = " + id);
        sinavDateText = findViewById(R.id.sinavDateText);
        questionCountText = findViewById(R.id.soruCount);
        trueCountText = findViewById(R.id.trueCount);
        falseCountText = findViewById(R.id.falseCount);
        QscoreText = findViewById(R.id.score);
        quizNumberText = findViewById(R.id.sinavNumberText);
    }
}

