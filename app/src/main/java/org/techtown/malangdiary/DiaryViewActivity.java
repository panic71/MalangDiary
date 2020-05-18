package org.techtown.malangdiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class DiaryViewActivity extends AppCompatActivity {
    SQLiteDatabase database;
    Cursor cursor;
    Date today = new Date();
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    String todayString = timeFormat.format(today);
    String date;
    String title;
    String content;
    byte[] img;
    TextView dateText;
    TextView titleText;
    TextView contentText;
    ImageView imgView;
    Button editButton;

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
        title = cursor.getString(1);
        content = cursor.getString(2);
        img = cursor.getBlob(3);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

        dateText = findViewById(R.id.dateText);
        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);
        imgView = findViewById(R.id.imgView);

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
