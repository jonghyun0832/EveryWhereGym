package com.example.everywheregym;
import android.content.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonClickListener;

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
    private String is_trainer;

    private ProgressDialog prDialog;



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

        initDialog();

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");
        is_trainer = sharedPreferences.getString("is_trainer","");

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


        // 로딩 출력
        showpDialog();

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

                    //로딩 숨기기
                    hidepDialog();
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

        vodAdapter.setOnClickImgListener(new VodAdapter.myRecyclerViewImgClickListener() {
            @Override
            public void whenImgClick(int position) {
                Intent intent = new Intent(getContext(),ShowProfileActivity.class);
                intent.putExtra("uploader_id",vodArray.get(position).getVod_uploader_id());
                getContext().startActivity(intent);
            }
        });

        vodAdapter.setOnClickMoreListener(new VodAdapter.myRecyclerViewMoreClickListener() {
            @Override
            public void whenMoreClick(int position) {

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_more, null);

                TextView tv_more_edit = dialogView.findViewById(R.id.tv_more_edit);
                TextView tv_more_delete = dialogView.findViewById(R.id.tv_more_delete);
                TextView tv_more_register = dialogView.findViewById(R.id.tv_more_register);
                ImageView iv_more_edit = dialogView.findViewById(R.id.iv_more_edit);
                ImageView iv_more_delete = dialogView.findViewById(R.id.iv_more_delete);
                //ImageView iv_more_register = dialogView.findViewById(R.id.iv_more_register);

                tv_more_edit.setVisibility(View.GONE);
                tv_more_delete.setVisibility(View.GONE);
                iv_more_edit.setVisibility(View.GONE);
                iv_more_delete.setVisibility(View.GONE);

                if (is_trainer.equals("1") && user_id.equals(vodArray.get(position).getVod_uploader_id())){
                    tv_more_edit.setVisibility(View.VISIBLE);
                    tv_more_delete.setVisibility(View.VISIBLE);
                    iv_more_edit.setVisibility(View.VISIBLE);
                    iv_more_delete.setVisibility(View.VISIBLE);
                }

                String vod_id = vodArray.get(position).getVod_id(); //수정에서 보냄
                String vod_thumbnail_path = vodArray.get(position).getVod_thumbnail(); //수정에서 보냄
                String vod_time = vodArray.get(position).getVod_time(); //수정에서 보냄
                String vod_title = vodArray.get(position).getVod_title(); //수정에서 보냄
                String vod_category = vodArray.get(position).getVod_category(); //수정에서 보냄
                String vod_difficulty = vodArray.get(position).getVod_difficulty(); //수정에서 보냄
                String vod_path = vodArray.get(position).getVod_path();
                String vod_explain = vodArray.get(position).getVod_explain();
                String vod_material = vodArray.get(position).getVod_materail();

                ad.setView(dialogView);

                AlertDialog alertDialog = ad.create();

                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point deviceSize = new Point();
                display.getSize(deviceSize);

                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.width = deviceSize.x;
                params.horizontalMargin = 0.0f;
                alertDialog.getWindow().setAttributes(params);

                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                window.setGravity(Gravity.BOTTOM);
                alertDialog.show();

                //동영상 수정하기
                tv_more_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(getContext(), VodDetailActivity.class);
                        intent.putExtra("vod_id",vod_id);
                        intent.putExtra("vod_thumbnail_path",vod_thumbnail_path);
                        intent.putExtra("vod_time",vod_time);
                        intent.putExtra("vod_title",vod_title);
                        intent.putExtra("vod_category",vod_category);
                        intent.putExtra("vod_difficulty",vod_difficulty);
                        intent.putExtra("vod_explain",vod_explain);
                        intent.putExtra("vod_material",vod_material);
                        intent.putExtra("isEdit",true);
                        startActivity(intent);
                    }
                });

                //동영상 삭제하기
                tv_more_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        AlertDialog.Builder ad2 = new AlertDialog.Builder(getContext());
                        ad2.setTitle("알림");
                        ad2.setMessage("동영상을 삭제하시겠습니까?");
                        ad2.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //삭제 레트로핏
                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<VodData> call = apiInterface.deleteVod(vod_id,vod_thumbnail_path,vod_path);
                                call.enqueue(new Callback<VodData>() {
                                    @Override
                                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                                        if (response.isSuccessful() && response.body() != null){
                                            if(response.body().isSuccess()){

                                                //리로드 (트레이너만 삭제되니까 걱정안해도됨)
                                                final FragmentTransaction ftt = getActivity().getSupportFragmentManager().beginTransaction();
                                                ftt.replace(R.id.main_frame_tr, new FragVideo());
                                                ftt.commit();

                                                Toast.makeText(getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<VodData> call, Throwable t) {
                                        Toast.makeText(getContext(), "삭제 통신 문제.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                        ad2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog alertDialog2 = ad2.create();
                        alertDialog2.show();


                    }
                });


                //즐겨찾기 등록
                tv_more_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //즐겨찾기 테이블에 저장하기 ㄱㄱ
                    }
                });

            }
        });



        vodAdapter.setOnClickPopupListener(new VodAdapter.myRecyclerViewPopUpClickListener() {
            @Override
            public void whenPopUpClick(VodAdapter.VodViewHolder vh, int position) {
                String vod_material = vodArray.get(position).getVod_materail();
                Balloon balloon = new Balloon.Builder(getContext())
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setArrowPosition(0.1f)
                        .setHeight(70)
                        .setTextSize(20f)
                        .setCornerRadius(4f)
                        .setAlpha(0.7f)
                        .setText("준비물 : " + vod_material)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.white))
                        .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.fb))
                        .setBalloonAnimation(BalloonAnimation.FADE)
                        .build();

                balloon.showAlignBottom(vh.iv_popup_material);
                balloon.setOnBalloonClickListener(new OnBalloonClickListener() {
                    @Override
                    public void onBalloonClick(@NonNull View view) {
                        balloon.dismiss();
                    }
                });
            }
        });


        return view;

    }

    protected void initDialog(){
        prDialog = new ProgressDialog(getContext());
        prDialog.setMessage("loading");
        prDialog.setCancelable(true);
    }

    protected void showpDialog(){
        if(!prDialog.isShowing()){
            prDialog.show();
        }
    }

    protected void hidepDialog(){
        if(prDialog.isShowing()){
            prDialog.dismiss();
        }
    }

}
