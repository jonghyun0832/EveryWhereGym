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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonClickListener;

import java.util.ArrayList;
import java.util.Locale;

public class FragVideo extends Fragment {

    private ImageView iv_user_img;
    private ImageView iv_search;
    private TextView filter_category;
    private TextView filter_time;
    private TextView filter_difficulty;
    private TextView filter_cancel;


    private RecyclerView recyclerView;
    private ArrayList<VodData> vodArray;
    private VodAdapter vodAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ProgressBar pbBar;

    private String user_id;
    private String is_trainer;

    private ProgressDialog prDialog;


    private String filtered_category;
    private String filtered_time;
    private String filtered_difficulty;

    private String tmp_difficulty;

    private String minVal;
    private String maxVal;

    private NestedScrollView nsv;
    private int page = 1, limit = 3;
    private String cursor = "0";
    private boolean isEnd = false;


    private final RangeSlider.OnSliderTouchListener rangeSliderTouchListener =
            new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(@NonNull RangeSlider slider) {

                }

                @Override
                public void onStopTrackingTouch(@NonNull RangeSlider slider) {
//                    int minNumber = Float.toString(slider.getValues().get(0)).indexOf(".");
//                    int maxNumber = Float.toString(slider.getValues().get(1)).indexOf(".");
//                    minVal = Float.toString(slider.getValues().get(0)).substring(0, minNumber);
//                    maxVal = Float.toString(slider.getValues().get(1)).substring(0, maxNumber);
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
        cursor = "0";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_video,container,false);

        Log.d("11111", "onCreateView: 생성생성" + filtered_category + filtered_time + filtered_difficulty);

        iv_search = (ImageView) view.findViewById(R.id.iv_vod_search);
        iv_user_img = (ImageView) view.findViewById(R.id.iv_vod_user_img);
        filter_category = (TextView) view.findViewById(R.id.filter_category);
        filter_time = (TextView) view.findViewById(R.id.filter_time);
        filter_difficulty = (TextView) view.findViewById(R.id.filter_difficulty);
        filter_cancel = (TextView) view.findViewById(R.id.filter_cancel);

        filter_cancel.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_vod);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        vodArray = new ArrayList<>();
        vodAdapter = new VodAdapter(vodArray, getActivity());
        recyclerView.setAdapter(vodAdapter);

        nsv = (NestedScrollView) view.findViewById(R.id.scroll_view_vod_main);
        pbBar = (ProgressBar) view.findViewById(R.id.progressBar_vod_main);

        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!nsv.canScrollVertically(1)){
                    Log.d("왜안됨", "더는아래로 못가요 " + cursor);
                    if(filtered_category != null || filtered_difficulty != null || filtered_time != null){
                        page++;
                        //pbBar.setVisibility(View.VISIBLE);
                        getFilterData();
                    } else {
                        page++;
                        //pbBar.setVisibility(View.VISIBLE);
                        getData(page,limit,cursor);
                    }
                }
            }
        });

        //getData(page,limit);

        initDialog();

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");
        is_trainer = sharedPreferences.getString("is_trainer","");


        if(!user_id.equals("0")){
            //사용자의 프로필 이미지 불러오기
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
                            Glide.with(getActivity()).load(user_img_url).override(40,40).into(iv_user_img);
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

        if (filtered_category != null){
            filter_category.setText(filtered_category + " ▼");
            filter_category.setTextColor(Color.WHITE);
            filter_category.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_blue));

            filter_cancel.setVisibility(View.VISIBLE);
        }
        if (filtered_difficulty != null){

            filter_difficulty.setText(filtered_difficulty + " ▼");
            filter_difficulty.setTextColor(Color.WHITE);
            filter_difficulty.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_blue));

            filter_cancel.setVisibility(View.VISIBLE);
        }
        if (filtered_time != null){
            filter_time.setText(filtered_time + " ▼");
            filter_time.setTextColor(Color.WHITE);
            filter_time.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_blue));

            filter_cancel.setVisibility(View.VISIBLE);
        }

        if (filtered_category != null || filtered_difficulty != null || filtered_time != null){
            //필터 기준으로 vodarray 불러오기
            pbBar.setVisibility(View.VISIBLE);
            page = 1;
            cursor = "0";

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<VodDataArray> call = apiInterface.filterVod(filtered_category,filtered_difficulty,filtered_time,page,limit,cursor);
            call.enqueue(new Callback<VodDataArray>() {
                @Override
                public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                    if (response.isSuccessful() && response.body() != null){
                        isEnd = response.body().isEnd();
                        if(isEnd){
                            pbBar.setVisibility(View.GONE);
                        }
                        cursor = response.body().getCursor();
                        ArrayList<VodData> vodArray_tmp = response.body().getVodDataArray();
                        vodArray.addAll(vodArray_tmp);

                        //vodArray = response.body().getVodDataArray();
                        vodAdapter.setAdapter(vodArray);
                        vodAdapter.notifyDataSetChanged();

                        hidepDialog();
                    }
                }
                @Override
                public void onFailure(Call<VodDataArray> call, Throwable t) {
                    //실패
                }
            });
        } else {
            Log.d("11111", "onCreateView: 필터없음");
            //리사이클러뷰 데이터 받아오기
            //원본 vodarray 불러오기

            getData(page,limit,cursor);

//            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//            Call<VodDataArray> call = apiInterface.getvodList();
//            call.enqueue(new Callback<VodDataArray>() {
//                @Override
//                public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
//                    if (response.isSuccessful() && response.body() != null){
//                        vodArray = response.body().getVodDataArray();
//                        vodAdapter.setAdapter(vodArray);
//                        vodAdapter.notifyDataSetChanged();
//
//                        //로딩 숨기기
//                        hidepDialog();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<VodDataArray> call, Throwable t) {
//                    Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
//                }
//            });
        }


        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });






        vodAdapter.setOnClickListener(new VodAdapter.myRecyclerViewClickListener() {
            @Override
            public void whenItemClick(int position) {
                final VodData item = vodArray.get(position);
                String vod_path = item.getVod_path();
                String vod_title = item.getVod_title();
                String vod_difficulty = item.getVod_difficulty();
                String vod_id = item.getVod_id();
                String vod_calorie = item.getVod_calorie();
                String vod_category = item.getVod_category();
                String vod_material = item.getVod_materail();
                String vod_uploader_name = item.getVod_uploader_name();
                String vod_explain = item.getVod_explain();
                String vod_uploader_img = item.getVod_uploader_img();
                String vod_uploader_id = item.getVod_uploader_id();
                String vod_thumbnail = item.getVod_thumbnail();
                int vod_view = item.getVod_view();

                Intent intent = new Intent(getContext(), ShowVodActivity.class);
                intent.putExtra("vod_path",vod_path);
                intent.putExtra("vod_title",vod_title);
                intent.putExtra("vod_difficulty",vod_difficulty);
                intent.putExtra("vod_id",vod_id);
                intent.putExtra("vod_view",vod_view);
                intent.putExtra("vod_calorie",vod_calorie);
                intent.putExtra("vod_category",vod_category);
                intent.putExtra("vod_material",vod_material);
                intent.putExtra("vod_uploader_name",vod_uploader_name);
                intent.putExtra("vod_explain",vod_explain);
                intent.putExtra("vod_uploader_img",vod_uploader_img);
                intent.putExtra("vod_uploader_id",vod_uploader_id);
                intent.putExtra("vod_thumbnail",vod_thumbnail);
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

                String vod_id = vodArray.get(position).getVod_id(); //수정에서 보냄
                String vod_thumbnail_path = vodArray.get(position).getVod_thumbnail(); //수정에서 보냄
                String vod_time = vodArray.get(position).getVod_time(); //수정에서 보냄
                String vod_title = vodArray.get(position).getVod_title(); //수정에서 보냄
                String vod_category = vodArray.get(position).getVod_category(); //수정에서 보냄
                String vod_difficulty = vodArray.get(position).getVod_difficulty(); //수정에서 보냄
                String vod_path = vodArray.get(position).getVod_path();
                String vod_explain = vodArray.get(position).getVod_explain();
                String vod_material = vodArray.get(position).getVod_materail();
                String vod_calorie = vodArray.get(position).getVod_calorie();




                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_more, null);

                TextView tv_more_edit = dialogView.findViewById(R.id.tv_more_edit);
                TextView tv_more_delete = dialogView.findViewById(R.id.tv_more_delete);
                TextView tv_more_register = dialogView.findViewById(R.id.tv_more_register);
                ImageView iv_more_edit = dialogView.findViewById(R.id.iv_more_edit);
                ImageView iv_more_delete = dialogView.findViewById(R.id.iv_more_delete);
                ImageView iv_more_register = dialogView.findViewById(R.id.iv_more_register);


                //북마크 확인
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<VodData> call = apiInterface.checkBookMark(user_id,vod_id);
                call.enqueue(new Callback<VodData>() {
                    @Override
                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                //북마크 없을때
                                tv_more_register.setText("북마크 추가");
                            } else {
                                //이미 북마크 된경우
                                tv_more_register.setText("북마크 해제");
                                iv_more_register.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_baseline_cancel_24));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VodData> call, Throwable t) {
                        //실패
                    }
                });



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
                        intent.putExtra("vod_calorie",vod_calorie);
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

                        alertDialog.dismiss();

                        if(tv_more_register.getText().toString().equals("북마크 추가")){

                            //즐겨찾기 테이블에 저장하기 ㄱㄱ
                            //레트로핏으로 해당 영상 정보 보내기
                            //리스트에 있으면 북마크 해제, 없으면 북마크 설정하기
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkVod(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                                            ad.setTitle("알림");
                                            ad.setMessage("동영상이 북마크에 추가되었습니다");
                                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VodData> call, Throwable t) {
                                    //실패
                                }
                            });

                        } else {
                            //북마크 해제
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkDelete(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                                            ad.setTitle("알림");
                                            ad.setMessage("해당 동영상의 북마크를 해제했습니다");
                                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();
                                        }else {
                                            Toast.makeText(getContext(), "서버에서 삭제실패", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VodData> call, Throwable t) {
                                    //실패
                                }
                            });

                        }

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
                balloon.dismissWithDelay(2500L);
            }
        });

        filter_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카테고리 필터
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("카테고리 필터");
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_category_box, null);
                ad.setView(dialogView);

                CheckBox cb_whole = dialogView.findViewById(R.id.checkBox_whole);
                CheckBox cb_abs = dialogView.findViewById(R.id.checkBox_abs);
                CheckBox cb_leg = dialogView.findViewById(R.id.checkBox_leg);
                CheckBox cb_chest = dialogView.findViewById(R.id.checkBox_chest);
                CheckBox cb_back = dialogView.findViewById(R.id.checkBox_back);

                if (filtered_category != null){
                    String[] split = filtered_category.split(", ");
                    if(split.length != 0){ //수정용
                        for (int i = 0; i < split.length; i++){
                            if(split[i].equals("전신"))
                                cb_whole.setChecked(true);
                            if(split[i].equals("복근"))
                                cb_abs.setChecked(true);
                            if(split[i].equals("하체"))
                                cb_leg.setChecked(true);
                            if(split[i].equals("가슴"))
                                cb_chest.setChecked(true);
                            if(split[i].equals("등"))
                                cb_back.setChecked(true);
                        }
                    }
                }


                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuffer select = new StringBuffer();
                        if(cb_whole.isChecked()) {
                            select.append("전신, ");
                        }
                        if(cb_abs.isChecked()){
                            select.append("복근, ");
                        }
                        if(cb_leg.isChecked())
                            select.append("하체, ");
                        if(cb_chest.isChecked())
                            select.append("가슴, ");
                        if(cb_back.isChecked())
                            select.append("등, ");

                        if(select.length() != 0){
                            System.out.println(select);
                            String result = select.substring(0,select.length()-2);
                            filtered_category = result;
                        } else {
                            filtered_category = "";
                        }

                        //통신으로 보내기
                        //필터 기준으로 vodarray 불러오기
                        pbBar.setVisibility(View.VISIBLE);
                        page = 1;
                        cursor = "0";
                        vodArray = new ArrayList<>();

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<VodDataArray> call = apiInterface.filterVod(filtered_category,filtered_difficulty,filtered_time,page,limit,cursor);
                        call.enqueue(new Callback<VodDataArray>() {
                            @Override
                            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if(!filtered_category.equals("")){
//                                        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor= sharedPreferences.edit();
//                                        editor.putBoolean("is_filter",true);
//                                        editor.commit();
                                        filter_category.setText(filtered_category + " ▼");
                                        filter_category.setTextColor(Color.WHITE);
                                        filter_category.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_blue));

                                        filter_cancel.setVisibility(View.VISIBLE);

                                    } else {
                                        filter_category.setText("집중부위 ▽");
                                        filter_category.setTextColor(Color.BLACK);
                                        filter_category.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));

                                        filter_cancel.setVisibility(View.GONE);
                                    }
                                    isEnd = response.body().isEnd();
                                    if(isEnd){
                                        pbBar.setVisibility(View.GONE);
                                    }
                                    cursor = response.body().getCursor();
                                    ArrayList<VodData> vodArray_tmp = response.body().getVodDataArray();
                                    vodArray.addAll(vodArray_tmp);


                                    //vodArray = response.body().getVodDataArray();
                                    vodAdapter.setAdapter(vodArray);
                                    vodAdapter.notifyDataSetChanged();
                                } else {
                                //json 오류
                                }
                            }

                            @Override
                            public void onFailure(Call<VodDataArray> call, Throwable t) {
                                    //실패
                            }
                        });


                    }

                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //취소했을때 할게없음
                    }
                });


                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });

        filter_difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 난이도 필터
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("난이도를 선택해주세요");
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_difficulty_box, null);
                ad.setView(dialogView);

                CheckBox cb_diff_1 = dialogView.findViewById(R.id.checkBox_diff_1);
                CheckBox cb_diff_2 = dialogView.findViewById(R.id.checkBox_diff_2);
                CheckBox cb_diff_3 = dialogView.findViewById(R.id.checkBox_diff_3);


                if (filtered_difficulty != null){
                    String[] split = filtered_difficulty.split(", ");
                    if(split.length != 0){ //수정용
                        for (int i = 0; i < split.length; i++){
                            if(split[i].equals("입문"))
                                cb_diff_1.setChecked(true);
                            if(split[i].equals("초급"))
                                cb_diff_2.setChecked(true);
                            if(split[i].equals("중급"))
                                cb_diff_3.setChecked(true);
                        }
                    }
                }



                ad.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuffer select = new StringBuffer();
                        if(cb_diff_1.isChecked()) {
                            select.append("입문, ");
                        }
                        if(cb_diff_2.isChecked()){
                            select.append("초급, ");
                        }
                        if(cb_diff_3.isChecked())
                            select.append("중급, ");

                        if(select.length() != 0){
                            System.out.println(select);
                            String result = select.substring(0,select.length()-2);
                            filtered_difficulty = result;
                        } else {
                            filtered_difficulty = "";
                        }

                        pbBar.setVisibility(View.VISIBLE);
                        page = 1;
                        cursor = "0";
                        vodArray = new ArrayList<>();

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<VodDataArray> call = apiInterface.filterVod(filtered_category,filtered_difficulty,filtered_time,page,limit,cursor);
                        call.enqueue(new Callback<VodDataArray>() {
                            @Override
                            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if(!filtered_difficulty.equals("")){
//                                        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor= sharedPreferences.edit();
//                                        editor.putBoolean("is_filter",true);
//                                        editor.commit();
                                        filter_difficulty.setText(filtered_difficulty + " ▼");
                                        filter_difficulty.setTextColor(Color.WHITE);
                                        filter_difficulty.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_blue));

                                        filter_cancel.setVisibility(View.VISIBLE);
                                    } else {
                                        filter_difficulty.setText("난이도 ▽");
                                        filter_difficulty.setTextColor(Color.BLACK);
                                        filter_difficulty.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));

                                        filter_cancel.setVisibility(View.GONE);
                                    }
                                    isEnd = response.body().isEnd();
                                    if(isEnd){
                                        pbBar.setVisibility(View.GONE);
                                    }
                                    cursor = response.body().getCursor();
                                    ArrayList<VodData> vodArray_tmp = response.body().getVodDataArray();
                                    vodArray.addAll(vodArray_tmp);

                                    //vodArray = response.body().getVodDataArray();
                                    vodAdapter.setAdapter(vodArray);
                                    vodAdapter.notifyDataSetChanged();
                                } else {
                                    //json 오류
                                }
                            }

                            @Override
                            public void onFailure(Call<VodDataArray> call, Throwable t) {
                                //실패
                            }
                        });
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

        filter_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 시간 필터
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("영상길이를 선택해주세요");
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_time_box, null);
                ad.setView(dialogView);

                RangeSlider rangeSlider = dialogView.findViewById(R.id.rangeSlider);
                rangeSlider.setLabelBehavior(LabelFormatter.LABEL_FLOATING);

                if(maxVal != null){
                    rangeSlider.setValues(Float.parseFloat(minVal),Float.parseFloat(maxVal));
                }

                rangeSlider.setLabelFormatter(new LabelFormatter() {
                    @NonNull
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.US,"%.0f",value);

                    }
                });

                rangeSlider.addOnSliderTouchListener(rangeSliderTouchListener);



                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int minNumber = Float.toString(rangeSlider.getValues().get(0)).indexOf(".");
                        int maxNumber = Float.toString(rangeSlider.getValues().get(1)).indexOf(".");
                        minVal = Float.toString(rangeSlider.getValues().get(0)).substring(0, minNumber);
                        maxVal = Float.toString(rangeSlider.getValues().get(1)).substring(0, maxNumber);

                        filtered_time = minVal + " ~ " + maxVal;

                        pbBar.setVisibility(View.VISIBLE);
                        page = 1;
                        cursor = "0";
                        vodArray = new ArrayList<>();

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<VodDataArray> call = apiInterface.filterVod(filtered_category,filtered_difficulty,filtered_time,page,limit,cursor);
                        call.enqueue(new Callback<VodDataArray>() {
                            @Override
                            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                                if (response.isSuccessful() && response.body() != null){
//                                    SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor= sharedPreferences.edit();
//                                    editor.putBoolean("is_filter",true);
//                                    editor.commit();
                                    filter_time.setText(filtered_time + " ▼");
                                    filter_time.setTextColor(Color.WHITE);
                                    filter_time.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_blue));

                                    filter_cancel.setVisibility(View.VISIBLE);

                                    isEnd = response.body().isEnd();
                                    if(isEnd){
                                        pbBar.setVisibility(View.GONE);
                                    }
                                    cursor = response.body().getCursor();
                                    ArrayList<VodData> vodArray_tmp = response.body().getVodDataArray();
                                    vodArray.addAll(vodArray_tmp);

                                    //vodArray = response.body().getVodDataArray();
                                    vodAdapter.setAdapter(vodArray);
                                    vodAdapter.notifyDataSetChanged();
                                } else {
                                    //json 오류
                                }
                            }

                            @Override
                            public void onFailure(Call<VodDataArray> call, Throwable t) {
                                //실패
                            }
                        });
                    }
                });


                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                });


                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });



        filter_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //눌렀을때 초기화시키기

                resetData();

//                showpDialog();
//                filtered_category = null;
//                filtered_difficulty = null;
//                filtered_time = null;
//
//                //처음에 했던 레트로핏써서 다시 원래 데이터 받아오기
//                //원본 vodarray 불러오기
//                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//                Call<VodDataArray> call = apiInterface.getvodList();
//                call.enqueue(new Callback<VodDataArray>() {
//                    @Override
//                    public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
//                        if (response.isSuccessful() && response.body() != null){
//                            vodArray = response.body().getVodDataArray();
//                            vodAdapter.setAdapter(vodArray);
//                            vodAdapter.notifyDataSetChanged();
//
//                            filter_category.setText("집중부위 ▽");
//                            filter_category.setTextColor(Color.BLACK);
//                            filter_category.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));
//
//                            filter_difficulty.setText("난이도 ▽");
//                            filter_difficulty.setTextColor(Color.BLACK);
//                            filter_difficulty.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));
//
//                            filter_time.setText("운동시간 ▽");
//                            filter_time.setTextColor(Color.BLACK);
//                            filter_time.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));
//                            maxVal = null;
//
//                            filter_cancel.setVisibility(View.GONE);
//
//                            //로딩 숨기기
//                            hidepDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<VodDataArray> call, Throwable t) {
//                        Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();


//        showpDialog();

//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<VodDataArray> call = apiInterface.getvodList();
//        call.enqueue(new Callback<VodDataArray>() {
//            @Override
//            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
//                if (response.isSuccessful() && response.body() != null){
//                    vodArray = response.body().getVodDataArray();
//                    vodAdapter.setAdapter(vodArray);
//                    vodAdapter.notifyDataSetChanged();
//
//                    //로딩 숨기기
//                    hidepDialog();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<VodDataArray> call, Throwable t) {
//                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
//            }
//        });

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

    private void getData(int page, int limit, String cur_num){

        //pbBar.setVisibility(View.GONE);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodDataArray> call = apiInterface.getvodList(page,limit,cur_num);
        call.enqueue(new Callback<VodDataArray>() {
            @Override
            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    isEnd = response.body().isEnd();
                    if(isEnd){
                        pbBar.setVisibility(View.GONE);
                    }
                    cursor = response.body().getCursor();
                    ArrayList<VodData> vodArray_tmp = response.body().getVodDataArray();
                    vodArray.addAll(vodArray_tmp);
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
    }

    private void getFilterData(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodDataArray> call = apiInterface.filterVod(filtered_category,filtered_difficulty,filtered_time,page,limit,cursor);
        call.enqueue(new Callback<VodDataArray>() {
            @Override
            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    isEnd = response.body().isEnd();
                    if(isEnd){
                        pbBar.setVisibility(View.GONE);
                    }
                    cursor = response.body().getCursor();
                    ArrayList<VodData> vodArray_tmp = response.body().getVodDataArray();
                    vodArray.addAll(vodArray_tmp);

                    //vodArray = response.body().getVodDataArray();
                    vodAdapter.setAdapter(vodArray);
                    vodAdapter.notifyDataSetChanged();
                } else {
                    //json 오류
                }
            }

            @Override
            public void onFailure(Call<VodDataArray> call, Throwable t) {
                //실패
            }
        });
    }


    private void resetData(){
        page = 1;
        cursor = "0";
        vodArray = new ArrayList<>();


        pbBar.setVisibility(View.VISIBLE);

        showpDialog();
        filtered_category = null;
        filtered_difficulty = null;
        filtered_time = null;

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodDataArray> call = apiInterface.getvodList(page,limit,cursor);
        call.enqueue(new Callback<VodDataArray>() {
            @Override
            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    cursor = response.body().getCursor();

                    vodArray = response.body().getVodDataArray();
                    vodAdapter.setAdapter(vodArray);
                    vodAdapter.notifyDataSetChanged();

                    filter_category.setText("집중부위 ▽");
                    filter_category.setTextColor(Color.BLACK);
                    filter_category.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));

                    filter_difficulty.setText("난이도 ▽");
                    filter_difficulty.setTextColor(Color.BLACK);
                    filter_difficulty.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));

                    filter_time.setText("운동시간 ▽");
                    filter_time.setTextColor(Color.BLACK);
                    filter_time.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text));
                    maxVal = null;

                    filter_cancel.setVisibility(View.GONE);

                    //로딩 숨기기
                    hidepDialog();
                }
            }

            @Override
            public void onFailure(Call<VodDataArray> call, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
