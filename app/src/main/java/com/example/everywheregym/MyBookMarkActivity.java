package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookMarkActivity extends AppCompatActivity {

    private ImageView iv_back;

    private RecyclerView recyclerView;
    private ArrayList<VodData> vodArray;
    private VodUploaderAdapter vodUploaderAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id;
    private String is_trainer;

    private ProgressDialog prDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_mark);

        iv_back = findViewById(R.id.iv_back_bookmark);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_my_bookmark);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        vodArray = new ArrayList<>();
        vodUploaderAdapter = new VodUploaderAdapter(vodArray, this);
        recyclerView.setAdapter(vodUploaderAdapter);

        SharedPreferences sharedPreferences= getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");
        is_trainer = sharedPreferences.getString("is_trainer","");

        initDialog();

        vodUploaderAdapter.setOnClickListener(new VodUploaderAdapter.vodUploaderAdapterClickListener() {
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

                Intent intent = new Intent(MyBookMarkActivity.this, ShowVodActivity.class);
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

        vodUploaderAdapter.setOnMoreClickListener(new VodUploaderAdapter.vodUploaderAdapterMoreClickListener() {
            @Override
            public void whenMoreClick(int position) {

                String vod_id = vodArray.get(position).getVod_id(); //???????????? ??????
                String vod_thumbnail_path = vodArray.get(position).getVod_thumbnail(); //???????????? ??????
                String vod_time = vodArray.get(position).getVod_time(); //???????????? ??????
                String vod_title = vodArray.get(position).getVod_title(); //???????????? ??????
                String vod_category = vodArray.get(position).getVod_category(); //???????????? ??????
                String vod_difficulty = vodArray.get(position).getVod_difficulty(); //???????????? ??????
                String vod_path = vodArray.get(position).getVod_path();
                String vod_explain = vodArray.get(position).getVod_explain();
                String vod_material = vodArray.get(position).getVod_materail();
                String vod_calorie = vodArray.get(position).getVod_calorie();


                AlertDialog.Builder ad = new AlertDialog.Builder(MyBookMarkActivity.this);
                LayoutInflater inflater = (LayoutInflater) MyBookMarkActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_more, null);

                TextView tv_more_edit = dialogView.findViewById(R.id.tv_more_edit);
                TextView tv_more_delete = dialogView.findViewById(R.id.tv_more_delete);
                TextView tv_more_register = dialogView.findViewById(R.id.tv_more_register);
                ImageView iv_more_edit = dialogView.findViewById(R.id.iv_more_edit);
                ImageView iv_more_delete = dialogView.findViewById(R.id.iv_more_delete);
                ImageView iv_more_register = dialogView.findViewById(R.id.iv_more_register);


                //????????? ??????
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<VodData> call = apiInterface.checkBookMark(user_id,vod_id);
                call.enqueue(new Callback<VodData>() {
                    @Override
                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                //????????? ?????????
                                tv_more_register.setText("????????? ??????");
                            } else {
                                //?????? ????????? ?????????
                                tv_more_register.setText("????????? ??????");
                                iv_more_register.setImageDrawable(ContextCompat.getDrawable(MyBookMarkActivity.this,R.drawable.ic_baseline_cancel_24));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VodData> call, Throwable t) {
                        //??????
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

                WindowManager windowManager = (WindowManager) MyBookMarkActivity.this.getSystemService(Context.WINDOW_SERVICE);
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

                tv_more_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(MyBookMarkActivity.this, VodDetailActivity.class);
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
                        intent.putExtra("fromList",true);

                        startActivity(intent);
                    }
                });

                //????????? ????????????
                tv_more_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        AlertDialog.Builder ad2 = new AlertDialog.Builder(MyBookMarkActivity.this);
                        ad2.setTitle("??????");
                        ad2.setMessage("???????????? ?????????????????????????");
                        ad2.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //?????? ????????????
                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<VodData> call = apiInterface.deleteVod(vod_id,vod_thumbnail_path,vod_path);
                                call.enqueue(new Callback<VodData>() {
                                    @Override
                                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                                        if (response.isSuccessful() && response.body() != null){
                                            if(response.body().isSuccess()){

                                                //????????? (??????????????? ??????????????? ??????????????????)
                                                finish();
                                                overridePendingTransition(0,0);
                                                Intent intent = getIntent();
                                                startActivity(intent);
                                                overridePendingTransition(0,0);

                                                Toast.makeText(MyBookMarkActivity.this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<VodData> call, Throwable t) {
                                        Toast.makeText(MyBookMarkActivity.this, "?????? ?????? ??????.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                        ad2.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog alertDialog2 = ad2.create();
                        alertDialog2.show();
                    }
                });

                //????????? ??????
                tv_more_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        if(tv_more_register.getText().toString().equals("????????? ??????")){

                            //???????????? ???????????? ???????????? ??????
                            //?????????????????? ?????? ?????? ?????? ?????????
                            //???????????? ????????? ????????? ??????, ????????? ????????? ????????????
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkVod(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(MyBookMarkActivity.this);
                                            ad.setTitle("??????");
                                            ad.setMessage("???????????? ???????????? ?????????????????????");
                                            ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                                    //??????
                                }
                            });

                        } else {
                            //????????? ??????
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkDelete(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(MyBookMarkActivity.this);
                                            ad.setTitle("??????");
                                            ad.setMessage("?????? ???????????? ???????????? ??????????????????");
                                            ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                    overridePendingTransition(0,0);
                                                    Intent intent = getIntent();
                                                    startActivity(intent);
                                                    overridePendingTransition(0,0);
                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();
                                        }else {
                                            Toast.makeText(MyBookMarkActivity.this, "???????????? ????????????", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VodData> call, Throwable t) {
                                    //??????
                                }
                            });

                        }
                    }
                });


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        showpDialog();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodDataArray> call = apiInterface.getBookMarkList(user_id);
        call.enqueue(new Callback<VodDataArray>() {
            @Override
            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    vodArray = response.body().getVodDataArray();
                    vodUploaderAdapter.setArrayList(vodArray);
                    vodUploaderAdapter.notifyDataSetChanged();

                    //?????? ?????????
                    hidepDialog();
                }
            }

            @Override
            public void onFailure(Call<VodDataArray> call, Throwable t) {
                Toast.makeText(MyBookMarkActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void initDialog(){
        prDialog = new ProgressDialog(this);
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