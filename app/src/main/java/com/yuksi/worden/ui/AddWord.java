package com.yuksi.worden.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yuksi.worden.dataclass.Words;
import com.yuksi.worden.database.WordsDbHelper;
import com.yuksi.worden.fragments.SinavlarFragment;
import com.yuksi.worden.MainActivity;
import com.yuksi.worden.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddWord extends AppCompatActivity {
    EditText wordEnEdit;
    Bitmap selectedImage;
    EditText wordTrEdit;
    EditText sentenceEnEdit;
    EditText sentenceTrEdit;
    ImageView wordImageAdd;
    int id;
    String info;
    public static int wordCounter;
    SQLiteDatabase database;
    WordsDbHelper wordsDb;
    Words words = new Words();
    Words words2 = new Words();
    byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        setupUI();
        if (info.matches("update")) {
            try {
                Cursor cursor = database.rawQuery("SELECT * FROM words WHERE id = ?", new String[]
                        {String.valueOf(id)});
                int wordEnIx = cursor.getColumnIndex("wordEn");
                int wordTrIx = cursor.getColumnIndex("wordTr");
                int sentenceEnIx = cursor.getColumnIndex("sentenceEn");
                int sentenceTrIx = cursor.getColumnIndex("sentenceTr");
                int wordImageIx = cursor.getColumnIndex("wordImage");

                while (cursor.moveToNext()) {
                    wordEnEdit.setText(cursor.getString(wordEnIx));
                    wordTrEdit.setText(cursor.getString(wordTrIx));
                    sentenceEnEdit.setText(cursor.getString(sentenceEnIx));
                    sentenceTrEdit.setText(cursor.getString(sentenceTrIx));
                    byte[] bytes = cursor.getBlob(wordImageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    wordImageAdd.setImageBitmap(bitmap);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setupUI() {
        wordEnEdit = findViewById(R.id.wordEnEdit);
        wordTrEdit = findViewById(R.id.wordTrEdit);
        sentenceEnEdit = findViewById(R.id.sentenceEnEdit);
        sentenceTrEdit = findViewById(R.id.sentenceTrEdit);
        wordImageAdd = findViewById(R.id.wordImage);
        wordsDb = new WordsDbHelper(getApplicationContext());
        database = this.openOrCreateDatabase("English", MODE_PRIVATE, null);
        words = new Words();
        Intent intent = getIntent();
        info = intent.getStringExtra("info");
        id = intent.getIntExtra("id", 1);
        byteArray = new byte[]{};
    }

    public void save(View view) {
        String wordEn = wordEnEdit.getText().toString();
        String wordTr = wordTrEdit.getText().toString();
        String sentenceEn = sentenceEnEdit.getText().toString();
        String sentenceTr = sentenceTrEdit.getText().toString();
        if (selectedImage != null) {
            Bitmap smallImage = makeSmallImage(selectedImage, 300);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            byteArray = outputStream.toByteArray();
        }
        if (info.matches("update")) {
            if (wordEn.equals("") || wordTr.equals("")) {
                Toast.makeText(getApplicationContext(), "İlk 2 satır boş kalamaz!", Toast.LENGTH_LONG).show();
            } else {
                words = new Words(wordEn, wordTr, sentenceEn, sentenceTr, byteArray);
                if (selectedImage == null) {
                    try {
                        database = this.openOrCreateDatabase("English", MODE_PRIVATE, null);
                        Cursor cursor = database.rawQuery("SELECT wordImage FROM words WHERE id = ?", new String[]{String.valueOf(id)});
                        while (cursor.moveToNext()) {
                            int wordImageIx = cursor.getColumnIndex("wordImage");
                            byteArray = cursor.getBlob(wordImageIx);
                            words.setByteArray(byteArray);
                        }
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Word Update catch ");
                    }
                }

                if (wordsDb.uptadeWord(id, words)) {
                    Toast.makeText(getApplicationContext(), "Güncelleme başarılı!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Güncelleme başarısız!", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            if (wordEn.equals("") || wordTr.equals("")) {
                Toast.makeText(getApplicationContext(), "İlk 2 satır boş kalamaz!", Toast.LENGTH_LONG).show();
            } else {
                words = new Words(wordEn, wordTr, sentenceEn, sentenceTr, byteArray);
                if (wordsDb.addWord(words) == true) {
                    wordCounter++;
                    Toast.makeText(getApplicationContext(), "Kayıt başarılı!", Toast.LENGTH_LONG).show();
                    words2 = new Words(wordCounter);
                    // System.out.println("WordCount = " + wordCounter);
                    if (wordCounter > 3) {
                        SinavlarFragment.started = true;
                        System.out.println("ADDWORD STARTED == " + SinavlarFragment.started);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kayıt başarısız!", Toast.LENGTH_LONG).show();
                }
            }

        }

        Intent i = new Intent(AddWord.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public Bitmap makeSmallImage(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);

        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentToGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGalery, 2);
        }

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intenToGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intenToGalery, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    wordImageAdd.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    wordImageAdd.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
