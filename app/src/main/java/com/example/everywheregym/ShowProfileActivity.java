package com.example.everywheregym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProfileActivity extends AppCompatActivity {

    private ImageView iv_showpf_backgroud; //뒷 배경 이미지
    private ImageView iv_showpf_img; // 프로필 이미지
    private TextView tv_showpf_name; //프로필 이름
    private TextView tv_showpf_intro;//프로필 소개
    private TextView tv_showpf_expert; //전문분야
    private TextView tv_showpf_career; //경력사항
    private TextView tv_showpf_certify; //자격사항


    private String img_url; //프로필 이미지
    private String back_img_url; //배경 이미지

    private String user_name;
    private String user_img;
    private String tr_img;
    private String tr_intro;
    private String tr_expert;
    private String tr_career;
    private String tr_certify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);


        iv_showpf_backgroud = findViewById(R.id.iv_showPF_background);
        iv_showpf_img = findViewById(R.id.iv_showpf_img);

        tv_showpf_name = findViewById(R.id.textview_showPF_name);
        tv_showpf_intro = findViewById(R.id.textview_showPF_intro);

        tv_showpf_expert = findViewById(R.id.textview_showPF_expert);
        tv_showpf_career = findViewById(R.id.textview_showPF_career);
        tv_showpf_certify = findViewById(R.id.textview_showPF_certify);


        iv_showpf_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowProfileActivity.this, ShowImageActivity.class);
                intent.putExtra("img_url",img_url);
                startActivity(intent);
            }
        });

        iv_showpf_backgroud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowProfileActivity.this, ShowImageActivity.class);
                intent.putExtra("img_url",back_img_url);
                startActivity(intent);
            }
        });



        Intent intent = getIntent();
        String uploader_id = intent.getStringExtra("uploader_id");
        //System.out.println("받은 번호 : " + uploader_id);



        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TrainerInfo> call = apiInterface.getTrainerInfo(uploader_id);
        call.enqueue(new Callback<TrainerInfo>() {
            @Override
            public void onResponse(Call<TrainerInfo> call, Response<TrainerInfo> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        user_name = response.body().getUser_name(); //트레이너 이름
                        user_img = response.body().getUser_img(); // 트레이너 프로필사진
                        tr_img = response.body().getTr_img(); //트레이너 배경사진
                        tr_intro = response.body().getTr_intro(); //트레이너 소개
                        tr_expert = response.body().getTr_expert(); //트레이너 전문영역
                        tr_career = response.body().getTr_career(); //트레이너 경력
                        tr_certify = response.body().getTr_certify(); //트레이너 전문사항

                        //텍스트 설정
                        tv_showpf_name.setText(user_name);
                        tv_showpf_intro.setText(tr_intro);
                        tv_showpf_expert.setText(tr_expert);
                        tv_showpf_career.setText(tr_career);
                        tv_showpf_certify.setText(tr_certify);

                        //이미지 설정
                        if (user_img == null || user_img.equals("")){
                            img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
                        }else {
                            img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + user_img;
                        }
                        Glide.with(ShowProfileActivity.this).load(img_url).override(250,250).into(iv_showpf_img);

                        if (tr_img == null || tr_img.equals("")){
                            back_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_back_image.jpeg";
                        }else {
                            back_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + tr_img;
                        }
                        Glide.with(ShowProfileActivity.this).load(back_img_url).into(iv_showpf_backgroud);

                        //제대로 온경우 코드 작성
                    }
                }
            }

            @Override
            public void onFailure(Call<TrainerInfo> call, Throwable t) {
                Toast.makeText(ShowProfileActivity.this, "레트로핏 오류", Toast.LENGTH_SHORT).show();
            }
        });



    }
}