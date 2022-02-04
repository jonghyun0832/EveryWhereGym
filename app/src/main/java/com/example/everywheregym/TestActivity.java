package com.example.everywheregym;

import android.app.ProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.RoundedCorner;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

public class TestActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    private TextView tv_category;
    private TextView tv_title;
    private TextView tv_view;
    private TextView tv_difficulty;
    private TextView tv_calorie;
    private TextView tv_material;

    private ImageView iv_uploader_img;
    private TextView tv_uploader_name;
    private ImageView iv_arrow;

    private ImageView iv_loading_thumbnail;

    private TextView tv_explain;

    private FrameLayout fr_show_profile;

    private String user_id;
    private String vod_uploader_img_url;



    private ProgressDialog prDialog;
    private ProgressBar prBar;

    public String SAMPLE_VIDEO_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//            setContentView(R.layout.activity_test);
//        } else {
//            setContentView(R.layout.activity_test);
//        }




        videoView = findViewById(R.id.videoView_vod_show);

        tv_category = findViewById(R.id.tv_vod_show_category);
        tv_title = findViewById(R.id.tv_vod_show_title);
        tv_view = findViewById(R.id.tv_vod_show_view);
        tv_difficulty = findViewById(R.id.tv_vod_show_difficulty);
        tv_calorie = findViewById(R.id.tv_vod_show_calorie);
        tv_material = findViewById(R.id.tv_vod_show_material);

        iv_uploader_img = findViewById(R.id.iv_vod_show_pf_img);
        tv_uploader_name = findViewById(R.id.tv_vod_show_name);
        iv_arrow = findViewById(R.id.iv_vod_show_arrow);

        tv_explain = findViewById(R.id.tv_vod_show_explain);

        fr_show_profile = findViewById(R.id.fr_vod_show);

        prBar = findViewById(R.id.progressBar);
        iv_loading_thumbnail = findViewById(R.id.iv_loading_thumbnail);


//        //아직 안쓰는데 쓰면 이사람 영상가져올떄쓸듯
//        SharedPreferences sharedPreferences= this.getSharedPreferences("info", Context.MODE_PRIVATE);
//        user_id = sharedPreferences.getString("user_id","0");

//        initDialog();
//        showpDialog();

        Intent intent = getIntent();
        String vod_path = intent.getStringExtra("vod_path");
        String vod_title = intent.getStringExtra("vod_title");
        String vod_difficulty = intent.getStringExtra("vod_difficulty");
        String vod_id = intent.getStringExtra("vod_id");
        int vod_view = intent.getIntExtra("vod_view",-1);
        String vod_cal = intent.getStringExtra("vod_calorie");
        String vod_category = intent.getStringExtra("vod_category");
        String vod_material = intent.getStringExtra("vod_material");
        String vod_uploader_img = intent.getStringExtra("vod_uploader_img");
        String vod_uploader_name = intent.getStringExtra("vod_uploader_name");
        String vod_explain = intent.getStringExtra("vod_explain");
        String vod_uploader_id = intent.getStringExtra("vod_uploader_id");
        String vod_thumbnail = intent.getStringExtra("vod_thumbnail");

        String vod_thumbnail_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + vod_thumbnail;

        Log.d("IMG", "onCreate전: ");
        Glide.with(TestActivity.this).load(vod_thumbnail_url).into(iv_loading_thumbnail);
        Log.d("IMG", "onCreate후: ");
        //제대로된 조회수를 받아왔을때 조회수 증가
        if (vod_view == -1){
            Toast.makeText(TestActivity.this, "조회수 오류", Toast.LENGTH_SHORT).show();
        } else {
            increaseView(vod_id,vod_view);
        };
        String str_category = "집중 부위 : " + vod_category;
        tv_category.setText(str_category);
        tv_title.setText(vod_title);
        String str_view = "조회수 " + vod_view + "회";
        tv_view.setText(str_view);
        tv_difficulty.setText(vod_difficulty);
        String str_cal = vod_cal + " Kcal";
        tv_calorie.setText(str_cal);
        Log.d("IMG", "onCreate세팅끝남: ");

        if(vod_material == null){
            tv_material.setText("준비물 없음");
        }else {
            if (vod_material.equals("")){
                tv_material.setText("준비물 없음");
            } else {
                tv_material.setText(vod_material);
            }
        }


        tv_uploader_name.setText(vod_uploader_name);
        tv_explain.setText(vod_explain);


        if (vod_uploader_img.equals("")){
            vod_uploader_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        } else{
            vod_uploader_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + vod_uploader_img;
        }
        Log.d("IMG", "onCreate업로더 이미지전: ");
        Glide.with(TestActivity.this).load(vod_uploader_img_url).override(50,50).into(iv_uploader_img);
        Log.d("IMG", "onCreate업로더이미지후: ");

        SAMPLE_VIDEO_URL = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/video/" + vod_path;
//        tv_title.setText(vod_title);
//        tv_difficulty.setText(vod_difficulty);

        //mediaController = new MediaController(TestActivity.this);

//        mediaController.setAnchorView(videoView);
//        mediaController.setMediaPlayer(videoView);

        // 비디오뷰에 컨트롤러 설정
        //videoView.setMediaController(mediaController);

        // 비디오 재생경로
        videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));
        Log.d("IMG", "onCreate리스너 들어가기 전: ");
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d("IMG", "onCreate비디오 준비됨: ");
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        Log.d("IMG", "onCreate비디오 사이즈 바뀌었을때: ");
                        mediaController = new MediaController(TestActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
                videoView.seekTo(1);

                videoView.start();
//                hidepDialog();
                prBar.setVisibility(View.INVISIBLE);
                iv_loading_thumbnail.setVisibility(View.INVISIBLE);

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //끝났으면 처음으로 돌아간다
                videoView.seekTo(1);
            }
        });



        fr_show_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(TestActivity.this, ShowProfileActivity.class);
                intent2.putExtra("uploader_id",vod_uploader_id);
                startActivity(intent2);
                finish();
            }
        });




    }



    //조회수 증가
    private void increaseView(String vod_id, int prev_vod_view){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodData> call = apiInterface.addVideoView(vod_id,prev_vod_view);
        call.enqueue(new Callback<VodData>() {
            @Override
            public void onResponse(Call<VodData> call, Response<VodData> response) {
                //조회수 증가 확인
            }

            @Override
            public void onFailure(Call<VodData> call, Throwable t) {
                Toast.makeText(TestActivity.this, "조회수 증가 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    protected void initDialog(){
//        prDialog = new ProgressDialog(this);
//        prDialog.setMessage("loading");
//        prDialog.setCancelable(true);
//    }
//
//    protected void showpDialog(){
//        if(!prDialog.isShowing()){
//            prDialog.show();
//        }
//    }
//
//    protected void hidepDialog(){
//        if(prDialog.isShowing()){
//            prDialog.dismiss();
//        }
//    }









}