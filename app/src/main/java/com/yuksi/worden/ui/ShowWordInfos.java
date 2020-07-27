package com.yuksi.worden.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuksi.worden.dataclass.Words;
import com.yuksi.worden.database.QuizDbHelper;
import com.yuksi.worden.database.WordsDbHelper;
import com.yuksi.worden.fragments.KelimelerFragment;
import com.yuksi.worden.MainActivity;
import com.yuksi.worden.R;

import static com.yuksi.worden.fragments.KelimelerFragment.wordCount;
import static com.yuksi.worden.fragments.SinavlarFragment.started;

public class ShowWordInfos extends AppCompatActivity {
    private TextView wordDateText;
    private TextView wordEnText;
    private TextView wordTrText;
    private TextView sentenceEnText;
    private TextView sentenceTrText;
    private ImageView wordImage;
    public String info;
    Words words;
    WordsDbHelper wordsDb;
    QuizDbHelper quizDb;
    public SQLiteDatabase database;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word_infos);
        setupUI();
        words = new Words();
        wordsDb = new WordsDbHelper(getApplicationContext());
        quizDb = new QuizDbHelper(getApplicationContext());
        database = this.openOrCreateDatabase("English", MODE_PRIVATE, null);
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM words WHERE id = ?", new String[]{String.valueOf(id)});
            int wordDateIx = cursor.getColumnIndex("wordDate");
            int wordEnIx = cursor.getColumnIndex("wordEn");
            int wordTrIx = cursor.getColumnIndex("wordTr");
            int sentenceEnIx = cursor.getColumnIndex("sentenceEn");
            int sentenceTrIx = cursor.getColumnIndex("sentenceTr");
            int wordImageIx = cursor.getColumnIndex("wordImage");
            while (cursor.moveToNext()) {
                wordDateText.setText(cursor.getString(wordDateIx));
                wordEnText.setText(cursor.getString(wordEnIx));
                wordTrText.setText(cursor.getString(wordTrIx));
                sentenceEnText.setText(cursor.getString(sentenceEnIx));
                sentenceTrText.setText(cursor.getString(sentenceTrIx));
                byte[] bytes = cursor.getBlob(wordImageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                wordImage.setImageBitmap(bitmap);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GÖSTERME HATASI");
        }
    }

    private void setupUI() {

        Intent intent = getIntent();
        wordDateText = findViewById(R.id.wordDate);
        wordEnText = findViewById(R.id.wordEn);
        wordTrText = findViewById(R.id.wordTr);
        sentenceEnText = findViewById(R.id.sentenceEn);
        sentenceTrText = findViewById(R.id.sentenceTr);
        wordImage = findViewById(R.id.wordImage);
        info = intent.getStringExtra("info");
        id = intent.getIntExtra("id", 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_word_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Intent intent;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        intent = new Intent();
        if (item.getItemId() == R.id.update_word_item) {
            intent = new Intent(ShowWordInfos.this, AddWord.class);
            intent.putExtra("info", "update");
            intent.putExtra("id", id);
            started = true;
            startActivity(intent);
        } else if (item.getItemId() == R.id.delete_word_item) {
            intent = new Intent(ShowWordInfos.this, KelimelerFragment.class);
            wordsDb.deleteWord(id);
            started = true;
            if (wordCount <= 3) {
                Toast.makeText(ShowWordInfos.this, "En az 4 kelime ekleyiniz!", Toast.LENGTH_SHORT).show();
            }
            intent.putExtra("position", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            intent = new Intent(ShowWordInfos.this, MainActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

}



