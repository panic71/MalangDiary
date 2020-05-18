package org.techtown.malangdiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DiaryViewActivity extends AppCompatActivity {
    SQLiteDatabase database;
    Cursor cursor;
    String date;
    String title;
    String content;
    byte[] img;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_view);
        initializeView();
    }

    public void initializeView(){

        Intent intent = getIntent();
        String queryDate = intent.getStringExtra("date");

        database = openOrCreateDatabase("Diary.db", MODE_PRIVATE, null);
        String SQL = "SELECT * FROM DIARIES WHERE DATE='" + queryDate + "';";

        cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();

        date = cursor.getString(cursor.getColumnIndex("DATE"));
        title = cursor.getString(cursor.getColumnIndex("TITLE"));
        content = cursor.getString(cursor.getColumnIndex("CONTENT"));
        img = cursor.getBlob(cursor.getColumnIndex("IMG"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

        TextView dateText = findViewById(R.id.dateText);
        TextView titleText = findViewById(R.id.titleText);
        TextView contentText = findViewById(R.id.contentText);
        ImageView imgView = findViewById(R.id.imgView);

        dateText.setText(date);
        titleText.setText(title);
        contentText.setText(content);
        imgView.setImageBitmap(bitmap);
    }

    public void onEditButtonClick(View v){
        Intent writeDiary = new Intent(this, DiaryActivity.class);

        writeDiary.putExtra("date", date);
        writeDiary.putExtra("title", title);
        writeDiary.putExtra("content", content);
        writeDiary.putExtra("img", img);

        startActivityForResult(writeDiary, 101);
    }
}
