package com.example.marlangdiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);


    }
    public void submitDiary(View v) {
        Intent submit_diary = new Intent(this, MainActivity.class);
        submit_diary.putExtra("alarm", "일기가 작성되었어요. 수고했어요!");
        setResult(Activity.RESULT_OK, submit_diary);

        // 일기나 사진이 입력되어 성공적으로 저장되었으면 RESULT_OK, 그렇지 않았으면 RESULT_CANCEL을 하고
        // 제대로 될 때까지 오류 메시지를 출력하는 기능이 필요

        finish();
    }
}
