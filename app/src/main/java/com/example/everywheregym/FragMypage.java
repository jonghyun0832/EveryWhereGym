package com.example.everywheregym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragMypage extends Fragment {

    private Button btn_setting;
    private Button btn_edit_profile;
    private TextView tv_profile_name;
    private ImageView iv_profile;
    private Button btn_bookmark;
    private Button btn_history;

    private String user_name;
    private String user_img;
    private String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_mypage,container,false);
        Log.d("TAG", "MYPAGEonCreateView: ");

        btn_setting = view.findViewById(R.id.button_setting);
        btn_edit_profile = view.findViewById(R.id.button_edit_profile);
        tv_profile_name = view.findViewById(R.id.textview_profile_name);
        iv_profile = view.findViewById(R.id.iv_mypage_user_img);
        btn_bookmark = view.findViewById(R.id.button_bookmark);
        btn_history = view.findViewById(R.id.button_history);


        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowImageActivity.class);
                intent.putExtra("img_url",url);
                startActivity(intent);
            }
        });

        //프로필 편집하기
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_img",user_img);
                startActivity(intent);
            }
        });

        //내 시청기록
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MyHistoryActivity.class);
                startActivity(intent);
            }
        });

        //내 북마크
        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MyBookMarkActivity.class);
                startActivity(intent);
            }
        });

        //설정
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);

            }
        });



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "MYPAGEonPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","0");

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserInfo> call = apiInterface.getInfo(user_id);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        user_name = response.body().getUser_name();
                        user_img = response.body().getUser_img();
                        tv_profile_name.setText(user_name);
                        if (user_img == null || user_img.equals("")){
                            url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/src/image/IMAGE_no_image.jpeg";
                        }else {
                            url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/src/image/" + user_img;
                        }
                        Glide.with(getContext()).load(url).override(250,250).into(iv_profile);
                    } else {
                        tv_profile_name.setText("실패오류");
                    }
                } else {
                    tv_profile_name.setText("로드오류");
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                tv_profile_name.setText("통신오류");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "MYPAGEonDestroy: ");
    }
}
