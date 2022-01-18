package com.example.everywheregym;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragTrHome extends Fragment {

    TextView tv_top; //맨위 텍스트
    Button btn_make_live; //라이브 일정 추가 버튼
    Button btn_upload_vod; //영상 업로드 버튼
    RecyclerView rv_home_live; //오늘의 라이브 리사이클러뷰


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer_home,container,false);

        tv_top = view.findViewById(R.id.textview_tr_home_top);
        btn_make_live = view.findViewById(R.id.btn_tr_home_make_live);
        btn_upload_vod = view.findViewById(R.id.btn_tr_home_upload_vod);

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","0");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TrainerInfo> call = apiInterface.getTrainerInfo(user_id);
        call.enqueue(new Callback<TrainerInfo>() {
            @Override
            public void onResponse(Call<TrainerInfo> call, Response<TrainerInfo> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        String tr_name = response.body().getUser_name();
                        tv_top.setText(tr_name + " 트레이너님 환영합니다!");
                    }
                }
            }

            @Override
            public void onFailure(Call<TrainerInfo> call, Throwable t) {
                Toast.makeText(getContext(), "레트로핏 오류", Toast.LENGTH_SHORT).show();
            }
        });

        btn_make_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MakeLiveActivity.class);
                startActivity(intent);
            }
        });

        btn_upload_vod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VodUploadActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }



}