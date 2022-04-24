package com.example.everywheregym;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

public class MyHistoryActivity extends AppCompatActivity {

    private ImageView iv_back;

    private RecyclerView recyclerView;
    private ArrayList<VodHistoryData> vodHistoryArray;
    private VodHistoryAdapter vodHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id;

    private ProgressDialog prDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);

        iv_back = findViewById(R.id.iv_history_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.rv_history_main);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        vodHistoryArray = new ArrayList<>();
        vodHistoryAdapter = new VodHistoryAdapter(vodHistoryArray,this);
        recyclerView.setAdapter(vodHistoryAdapter);

        SharedPreferences sharedPreferences= getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

        initDialog();


        vodHistoryAdapter.setOnClickListener(new VodHistoryAdapter.vodHistoryAdapterClickListener() {
            @Override
            public void whenItemClick(int position, int positions) {
                final VodData item = vodHistoryArray.get(position).getVodData().get(positions);
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

                Intent intent = new Intent(MyHistoryActivity.this, ShowVodActivity.class);
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

        vodHistoryAdapter.setOnMoreClickListener(new VodHistoryAdapter.vodHistoryAdapterMoreClickListener() {
            @Override
            public void whenMoreClick(int position, int positions) {
                String vod_id = vodHistoryArray.get(position).getVodData().get(positions).getVod_id();

                AlertDialog.Builder ad = new AlertDialog.Builder(MyHistoryActivity.this);
                LayoutInflater inflater = (LayoutInflater) MyHistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_history_more, null);

                TextView tv_more_delete = dialogView.findViewById(R.id.tv_history_more_delete);
                TextView tv_more_bookmark = dialogView.findViewById(R.id.tv_history_more_register);
                ImageView iv_more_bookmark = dialogView.findViewById(R.id.iv_history_more_register);

                //북마크 확인
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<VodData> call = apiInterface.checkBookMark(user_id,vod_id);
                call.enqueue(new Callback<VodData>() {
                    @Override
                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                //북마크 없을때
                                tv_more_bookmark.setText("북마크 추가");
                            } else {
                                //이미 북마크 된경우
                                tv_more_bookmark.setText("북마크 해제");
                                iv_more_bookmark.setImageDrawable(ContextCompat.getDrawable(MyHistoryActivity.this,R.drawable.ic_baseline_cancel_24));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VodData> call, Throwable t) {
                        //실패
                    }
                });

                ad.setView(dialogView);

                AlertDialog alertDialog = ad.create();

                WindowManager windowManager = (WindowManager) MyHistoryActivity.this.getSystemService(Context.WINDOW_SERVICE);
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


                tv_more_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<VodData> call = apiInterface.deleteHistory(user_id,vod_id);
                        call.enqueue(new Callback<VodData>() {
                            @Override
                            public void onResponse(Call<VodData> call, Response<VodData> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if(response.body().isSuccess()){
                                        AlertDialog.Builder ad = new AlertDialog.Builder(MyHistoryActivity.this);
                                        ad.setTitle("알림");
                                        ad.setMessage("해당 영상의 시청 기록을 삭제했습니다");
                                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(MyHistoryActivity.this, "서버에서 삭제실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<VodData> call, Throwable t) {
                                //실패
                            }
                        });

                    }
                });


                //북마크 등록
                tv_more_bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        if(tv_more_bookmark.getText().toString().equals("북마크 추가")){

                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkVod(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(MyHistoryActivity.this);
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
                                            AlertDialog.Builder ad = new AlertDialog.Builder(MyHistoryActivity.this);
                                            ad.setTitle("알림");
                                            ad.setMessage("해당 동영상의 북마크를 해제했습니다");
                                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //여기선 리로드 안해도됨
                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();
                                        }else {
                                            Toast.makeText(MyHistoryActivity.this, "서버에서 삭제실패", Toast.LENGTH_SHORT).show();
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



    }

    @Override
    protected void onResume() {
        super.onResume();

        showpDialog();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodHistoryDataArray> call = apiInterface.getHistory(user_id);
        call.enqueue(new Callback<VodHistoryDataArray>() {
            @Override
            public void onResponse(Call<VodHistoryDataArray> call, Response<VodHistoryDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    System.out.println(response);
                    vodHistoryArray = response.body().getVodHistoryData();
                    vodHistoryAdapter.setArrayList(vodHistoryArray);
                    vodHistoryAdapter.notifyDataSetChanged();

                    hidepDialog();


                }else {
                    System.out.println("뭔가 잘못되고있음");
                }
            }

            @Override
            public void onFailure(Call<VodHistoryDataArray> call, Throwable t) {
                System.out.println("뭔가 잘못되고있음");
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