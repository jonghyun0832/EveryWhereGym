package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class reviewActivity extends AppCompatActivity {

    private TextView tv_title;
    private EditText et_text;
    private TextView tv_content;

    private RatingBar rb;
    private Button btn_send;

    private String live_id;
    private String uploader_id;
    private String live_title;

    //private String review_text;
    private String review_id;
    //private String review_score;

    private boolean isEdit;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");

        tv_title = findViewById(R.id.tv_review_title);
        et_text = findViewById(R.id.et_review);
        tv_content = findViewById(R.id.tv_review_content);

        rb = findViewById(R.id.rb_review);
        btn_send = findViewById(R.id.btn_review);

        Bundle bundle = getIntent().getExtras();
        live_id = bundle.getString("live_id");
        isEdit = bundle.getBoolean("Edit",false);

        if(isEdit){
            String review_text = bundle.getString("text");
            String review_title = bundle.getString("title");
            review_id = bundle.getString("id");
            String review_score = bundle.getString("score");

            tv_content.setVisibility(View.INVISIBLE);
            tv_title.setText(review_title);
            rb.setRating(Float.parseFloat(review_score));
            et_text.setText(review_text);
            btn_send.setText("수정완료");

        } else {
            getLiveInfo(live_id);
        }


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = et_text.getText().toString();
                int score = (int)rb.getRating();

                if(isEdit){
                    editReview(review_id, review, score);
                } else {
                    sendReview(uploader_id, review, score);
                }

            }
        });


    }





    private void getLiveInfo(String li_id){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.getLiveInfo(li_id);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        //가져온 정보들로 평가창 만들면됨
                        live_title = response2.body().getLive_title();
                        uploader_id = response2.body().getUploader_id();

                        tv_title.setText("라이브 제목 : " + live_title);
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(reviewActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendReview(String trainer_id, String review, int score){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.addReview(trainer_id,user_id,review,score,live_title);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        //리뷰 저장
                        Toast.makeText(reviewActivity.this, "리뷰를 남겨주셔서 감사합니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(reviewActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editReview(String rv_id, String review, int score){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReviewData> call2 = apiInterface2.editReivew(rv_id,review,score);
        call2.enqueue(new Callback<ReviewData>() {
            @Override
            public void onResponse(Call<ReviewData> call2, Response<ReviewData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        AlertDialog.Builder ad = new AlertDialog.Builder(reviewActivity.this);
                        ad.setTitle("알림");
                        ad.setMessage("수정이 완료되었습니다");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = ad.create();
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewData> call2, Throwable t) {
                Toast.makeText(reviewActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

}