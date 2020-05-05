package org.techtown.malangdiary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DiaryActivity extends AppCompatActivity {

    private ImageView imageView;
    Button dateButton;
    EditText titleEditText;
    EditText contentEditText;
    Date today = new Date();
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    SQLiteDatabase database;
    Cursor cursor;

    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        this.initializeView();
        this.initializeListener();

        imageView = findViewById(R.id.imageUpload);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 102);
            }

            //클릭시 앨범에 접근해 사진을 올릴 수 있도록 했음

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 102) {
            if(resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch(Exception e) {


                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }

        }

        //앨범에서 사진을 고르면 입력되도록 하고, 뒤로가기를 누르면 취소되었다는 팝업 출력
    }

    public void diary_submit(View v) {
        titleEditText = findViewById(R.id.titleText);
        contentEditText = findViewById(R.id.contentText);
        dateButton = findViewById(R.id.dateButton);

        String date = dateButton.getText().toString();
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

//         유효성 검사 통과하면 등록
        if(validData(date, title, content)) {
            createDatabase();
            createTable();

            String SQL = "SELECT DATE FROM DIARIES WHERE DATE='" + date + "';";
            cursor = database.rawQuery(SQL, null);
            int count = cursor.getCount();

            if(count==1){
                updateData(date, title, content);

            }
            else{ insertData(date, title, content); }

            Intent diaryIntent = new Intent(this, MainActivity.class);
            diaryIntent.putExtra("alarm", "일기가 작성되었어요.");
            setResult(Activity.RESULT_OK, diaryIntent);
            finish();
        }
        else{ Toast.makeText(this, "날짜와 제목, 내용을 작성해주세요.", Toast.LENGTH_SHORT).show(); }
    }

    public void onDateButtonClick(View view){
        try{
            Date curDate = timeFormat.parse(dateButton.getText().toString());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            cal.setTime(curDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);
            dialog.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createDatabase(){
        database = openOrCreateDatabase("Diary.db", MODE_PRIVATE, null);
    }

    public void createTable(){
        String SQL = "CREATE TABLE IF NOT EXISTS DIARIES (DATE TEXT PRIMARY KEY, TITLE TEXT, CONTENT TEXT);";
        database.execSQL(SQL);
    }

    public boolean validData(String date, String title, String content){
        if(date.length()>0 && title.length()>0 && content.length()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public void insertData(String date, String title, String content){
        try
        {
            if(database != null)
            {
                String SQL = "INSERT INTO DIARIES VALUES ('" + date + "', '" + title + "', '" + content + "');";
                database.execSQL(SQL);
            }
        }
        catch(Exception e) { e.printStackTrace(); }
    }

    public void updateData(String date, String title, String content){
        try{
            if(database!=null)
            {
                String SQL = "UPDATE DIARIES SET TITLE='" + title + "', CONTENT='" + content + "' WHERE DATE='" + date + "';";
                database.execSQL(SQL);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initializeView(){
        dateButton = findViewById(R.id.dateButton);
        String todayString = timeFormat.format(today);
        dateButton.setText(todayString);
    }

    public void initializeListener(){
        // 날짜 선택
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 0, 0);
                String time = timeFormat.format(c.getTime());
                dateButton = findViewById(R.id.dateButton);
                dateButton.setText(time);
            }
        };
    }

}
