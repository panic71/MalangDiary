package org.techtown.malangdiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class DiaryActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

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
        Intent diaryIntent = new Intent(this, MainActivity.class);
        diaryIntent.putExtra("alarm", "일기가 작성되었어요.");
        setResult(Activity.RESULT_OK, diaryIntent);

        //일기가 입력되어 저장되었으면 OK, 그렇지 않으면 CANCEL이 되어야 함.
        //지금 DB에 입력하는 게 구현 안 되었으니 걍 누르면 OK를 출력하도록 했음.

        finish();
    }


}
