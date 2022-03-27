package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProfileActivity extends AppCompatActivity {

    private ImageView iv_showpf_backgroud; //뒷 배경 이미지
    private ImageView iv_showpf_img; // 프로필 이미지
    private TextView tv_showpf_name; //프로필 이름


    private String img_url; //프로필 이미지
    private String back_img_url; //배경 이미지

    private String user_name;
    private String user_img;
    private String tr_img;
    public String tr_intro;
    public String tr_expert;
    public String tr_career;
    public String tr_certify;

    private RatingBar rb;

    private ViewPager2 viewpager2;
    private TabLayout tabLayout;


    public ArrayList<VodData> getVodArray;
    public ArrayList<ReviewData> getReviewArray;
    public String show_uploader_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile2);
        Log.d("why", "ActivityonCreate: ");


        iv_showpf_backgroud = findViewById(R.id.iv_showPF_background);
        iv_showpf_img = findViewById(R.id.iv_showpf_img);

        tv_showpf_name = findViewById(R.id.textview_showPF_name);

        rb = findViewById(R.id.ratingBar_show);


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
        show_uploader_id = intent.getStringExtra("uploader_id");

        //getReviewScore(show_uploader_id);


        //리뷰 가져오기
//        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
//        String user_id = sharedPreferences.getString("user_id","");

        //vod 리스트 가져오기
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TrainerInfo> call = apiInterface.getTrainerInfo(show_uploader_id);
        call.enqueue(new Callback<TrainerInfo>() {
            @Override
            public void onResponse(Call<TrainerInfo> call, Response<TrainerInfo> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        Log.d("why", "ActivityonResponse1: " + tr_intro);
                        user_name = response.body().getUser_name(); //트레이너 이름
                        user_img = response.body().getUser_img(); // 트레이너 프로필사진
                        tr_img = response.body().getTr_img(); //트레이너 배경사진
                        tr_intro = response.body().getTr_intro(); //트레이너 소개
                        tr_expert = response.body().getTr_expert(); //트레이너 전문영역
                        tr_career = response.body().getTr_career(); //트레이너 경력
                        tr_certify = response.body().getTr_certify(); //트레이너 전문사항
//                        int tr_score = response.body().getTr_score();
//                        if(tr_score < 0){
//                            tr_score = 0;
//                        }
//
//                        float result = (float)tr_score/100 * 5;
//
//                        rb.setRating(result);

                        //텍스트 설정
                        tv_showpf_name.setText(user_name);


                        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<VodDataArray> call2 = apiInterface2.getTrainervodList(show_uploader_id);
                        call2.enqueue(new Callback<VodDataArray>() {
                            @Override
                            public void onResponse(Call<VodDataArray> call2, Response<VodDataArray> response2) {
                                if (response2.isSuccessful() && response2.body() != null){
                                    getVodArray = response2.body().getVodDataArray();

                                    //뷰페이저 탭 레이아웃 연결
                        Log.d("why", "ActivityonResponse2: " + tr_intro);
                                    ArrayList<Fragment> fragments = new ArrayList<>();
                                    fragments.add(ViewFragInfo.newInstance(0));
                                    fragments.add(ViewFragVod.newInstance(1));
                                    fragments.add(ViewFragReview.newInstance(2));
                        Log.d("why", "ActivityonResponse3: " + tr_intro);

                                    viewpager2 = (ViewPager2) findViewById(R.id.viewpager2);
                                    tabLayout = (TabLayout) findViewById(R.id.tabLayout_show);

                                    ViewFragAdapter viewFragAdapter = new ViewFragAdapter(ShowProfileActivity.this,fragments);
                                    viewpager2.setAdapter(viewFragAdapter);

                                    final List<String> tabElement = Arrays.asList("트레이너 정보", "업로드한 영상", "라이브 리뷰");
                        Log.d("why", "ActivityonResponse4: " + tr_intro);
                                    new TabLayoutMediator(tabLayout, viewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
                                        @Override
                                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                            TextView tv =  new TextView(ShowProfileActivity.this);
                                            tv.setGravity(Gravity.CENTER);
                                            tv.setTextColor(Color.BLACK);
                                            tv.setText(tabElement.get(position));
                                            tab.setCustomView(tv);
                                        }
                                    }).attach();
                                    //로딩 숨기기
                                    //hidepDialog();
                                }
                            }

                            @Override
                            public void onFailure(Call<VodDataArray> call2, Throwable t) {
                                Toast.makeText(ShowProfileActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                            }
                        });

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

    @Override
    protected void onResume() {
        super.onResume();
        getReviewScore(show_uploader_id);
    }

    public void getReviewScore(String uploader_id){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReviewData> call2 = apiInterface2.getReviewScore(uploader_id);
        call2.enqueue(new Callback<ReviewData>() {
            @Override
            public void onResponse(Call<ReviewData> call2, Response<ReviewData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    float score_result = response2.body().getRv_total_score();
                    rb.setRating(score_result);
                }
            }

            @Override
            public void onFailure(Call<ReviewData> call2, Throwable t) {
                Toast.makeText(ShowProfileActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

}