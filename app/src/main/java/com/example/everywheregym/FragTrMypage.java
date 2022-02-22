package com.example.everywheregym;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class FragTrMypage extends Fragment {

    ImageView iv_backgroud; //뒷 배경 이미지
    ImageView iv_profile_img; // 프로필 이미지
    TextView tv_profile_name; //프로필 이름
    Button btn_tr_edit; //프로필 수정 버튼
    TextView tv_profile_intro;//프로필 소개
    TextView tv_tr_expert; //전문분야
    TextView tv_tr_career; //경력사항
    TextView tv_tr_certify; //자격사항
    Button btn_my_live; //내 라이브 일정
    Button btn_my_upload; //내 업로드 영상
    Button btn_my_bookmark; //내 북마크
    Button btn_tr_logout; //로그아웃

    RatingBar rb;

    String img_url; //프로필 이미지
    String back_img_url; //배경 이미지

    String user_name;
    String user_img;
    String tr_img;
    String tr_intro;
    String tr_expert;
    String tr_career;
    String tr_certify;

    private String user_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer_mypage,container,false);

        iv_backgroud = view.findViewById(R.id.iv_tr_mypage_background);
        iv_profile_img = view.findViewById(R.id.iv_tr_mypage_img);

        tv_profile_name = view.findViewById(R.id.textview_tr_mypage_name);
        btn_tr_edit = view.findViewById(R.id.btn_tr_mypage_edit);
        tv_profile_intro = view.findViewById(R.id.textview_tr_mypage_intro);

        tv_tr_expert = view.findViewById(R.id.textview_tr_expert);
        tv_tr_career = view.findViewById(R.id.textview_tr_career);
        tv_tr_certify = view.findViewById(R.id.textview_tr_certify);

        btn_my_live = view.findViewById(R.id.btn_my_live);
        btn_my_upload = view.findViewById(R.id.btn_my_upload);
        btn_my_bookmark = view.findViewById(R.id.btn_my_bookmark_tr);
        btn_tr_logout = view.findViewById(R.id.btn_tr_logout);

        rb = view.findViewById(R.id.ratingBar_tr_mypage);

        SharedPreferences sharedPreferences= getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");


        iv_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowImageActivity.class);
                intent.putExtra("img_url",img_url);
                startActivity(intent);
            }
        });

        iv_backgroud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowImageActivity.class);
                intent.putExtra("img_url",back_img_url);
                startActivity(intent);
            }
        });



        btn_tr_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TrainerEditProfileActivity.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_img",user_img);
                intent.putExtra("tr_img",tr_img);
                intent.putExtra("tr_intro",tr_intro);
                intent.putExtra("tr_expert",tr_expert);
                intent.putExtra("tr_career",tr_career);
                intent.putExtra("tr_certify",tr_certify);
                startActivity(intent);
            }
        });



        //내 라이브 일정 확인
        btn_my_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "라이브 확인 만들어야함", Toast.LENGTH_SHORT).show();
            }
        });


        //내 업로드 영상 확인
        btn_my_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TrainerMyVodActivity.class);
                startActivity(intent);
            }
        });

        //내 북마크
        btn_my_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MyBookMarkActivity.class);
                startActivity(intent);
            }
        });



        //로그아웃 버튼
        btn_tr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그아웃 (로그인 설정되어있는것 해제해주기
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("알림");
                ad.setMessage("로그아웃 하시겠습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //쉐어드 유저정보 삭제
                        SharedPreferences sharedPreferences= getContext().getSharedPreferences("info", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_id");
                        editor.remove("is_trainer");
                        editor.commit();
                        //저장된 토큰 삭제
                        deleteToken(user_id);

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","0");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TrainerInfo> call = apiInterface.getTrainerInfo(user_id);
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
                        int tr_score = response.body().getTr_score();
                        if(tr_score < 0){
                            tr_score = 0;
                        }

                        float result = (float)tr_score/100 * 5;

                        //텍스트 설정
                        tv_profile_name.setText(user_name);
                        tv_profile_intro.setText(tr_intro);
                        tv_tr_expert.setText(tr_expert);
                        tv_tr_career.setText(tr_career);
                        tv_tr_certify.setText(tr_certify);
                        rb.setRating(result);

                        //이미지 설정
                        if (user_img == null || user_img.equals("")){
                            img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
                        }else {
                            img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + user_img;
                        }
                        Glide.with(getContext()).load(img_url).override(250,250).into(iv_profile_img);

                        if (tr_img == null || tr_img.equals("")){
                            back_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_back_image.jpeg";
                        }else {
                            back_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + tr_img;
                        }
                        Glide.with(getContext()).load(back_img_url).into(iv_backgroud);

                        //제대로 온경우 코드 작성
                    }
                }
            }

            @Override
            public void onFailure(Call<TrainerInfo> call, Throwable t) {
                Toast.makeText(getContext(), "레트로핏 오류", Toast.LENGTH_SHORT).show();
            }
        });

        //여기서 불러오자

    }
    private void deleteToken(String userId){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call = apiInterface.deleteToken(userId);
        call.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                if (response.isSuccessful() && response.body() != null){

                }
            }

            @Override
            public void onFailure(Call<LiveData> call, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
