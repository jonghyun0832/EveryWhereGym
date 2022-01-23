package com.example.everywheregym;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FragVideo extends Fragment {

    private ImageView iv_user_img;
    private ImageView iv_search;
    private TextView filter_category;
    private TextView filter_time;
    private TextView filter_difficulty;


    private RecyclerView recyclerView;
    private ArrayList<VodData> vodArray;
    private VodAdapter vodAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_video,container,false);

        iv_search = (ImageView) view.findViewById(R.id.iv_vod_search);
        iv_user_img = (ImageView) view.findViewById(R.id.iv_vod_user_img);
        filter_category = (TextView) view.findViewById(R.id.filter_category);
        filter_time = (TextView) view.findViewById(R.id.filter_time);
        filter_difficulty = (TextView) view.findViewById(R.id.filter_difficulty);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_vod);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        //최신것이 제일 위에 올 수 있도록 설정
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        vodArray = new ArrayList<>();
        vodAdapter = new VodAdapter(vodArray, getActivity());
        recyclerView.setAdapter(vodAdapter);


        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

        if(!user_id.equals("0")){
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<UserInfo> call = apiInterface.getInfo(user_id);
            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if (response.isSuccessful() && response.body() != null){
                        if(response.body().isSuccess()){
                            String user_img = response.body().getUser_img();
                            String user_img_url;
                            if (user_img.equals("")){
                                user_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
                            } else{
                                user_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + user_img;
                            }
                            Glide.with(getContext()).load(user_img_url).override(40,40).into(iv_user_img);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "사용자 정보 오류", Toast.LENGTH_SHORT).show();
        }



        //리사이클러뷰 데이터 받아오기
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodDataArray> call = apiInterface.getvodList();
        call.enqueue(new Callback<VodDataArray>() {
            @Override
            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    vodArray = response.body().getVodDataArray();
                    vodAdapter.setAdapter(vodArray);
                    vodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<VodDataArray> call, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });

        vodAdapter.setOnClickListener(new VodAdapter.myRecyclerViewClickListener() {
            @Override
            public void whenItemClick(int position) {
                final VodData item = vodArray.get(position);
                String vod_path = item.getVod_path();
                String vod_title = item.getVod_title();
                String vod_difficulty = item.getVod_difficulty();

                Intent intent = new Intent(getContext(),TestActivity.class);
                intent.putExtra("vod_path",vod_path);
                intent.putExtra("vod_title",vod_title);
                intent.putExtra("vod_difficulty",vod_difficulty);
                startActivity(intent);

            }
        });





        return view;
    }
}
