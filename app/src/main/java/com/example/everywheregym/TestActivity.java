package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class TestActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;

    TextView tv_title;
    TextView tv_category;
    TextView tv_difficulty;

    String user_id;

    private ProgressDialog prDialog;

    public String SAMPLE_VIDEO_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        videoView = findViewById(R.id.videoView_test);
        tv_title = findViewById(R.id.textView_test1);
        tv_category = findViewById(R.id.textView_test2);
        tv_difficulty = findViewById(R.id.textView_test3);


        SharedPreferences sharedPreferences= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

        initDialog();
        showpDialog();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VideoInfo> call = apiInterface.getvideoInfo(user_id);
        call.enqueue(new Callback<VideoInfo>() {
            @Override
            public void onResponse(Call<VideoInfo> call, Response<VideoInfo> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){

                        String title = response.body().getTitle();
                        String category = response.body().getCategory();
                        String difficulty = response.body().getDifficulty();
                        String video_url = response.body().getVideo_url();

                        SAMPLE_VIDEO_URL = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/video/" + video_url;

                        tv_title.setText(title);
                        tv_category.setText(category);
                        tv_difficulty.setText(difficulty);

                        mediaController = new MediaController(TestActivity.this);

                        // 비디오뷰에 컨트롤러 설정
                        videoView.setMediaController(mediaController);

                        // 비디오 재생경로
                        videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));

                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                videoView.seekTo(1);

                                videoView.start();
                                hidepDialog();

                            }
                        });

                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                //끝났으면 처음으로 돌아간다
                                videoView.seekTo(1);
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<VideoInfo> call, Throwable t) {
                Toast.makeText(TestActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });


        //SAMPLE_VIDEO_URL = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/video/4_1642659633875";

//        mediaController = new MediaController(this);
//
//        // 비디오뷰에 컨트롤러 설정
//        videoView.setMediaController(mediaController);
//
//        // 비디오 재생경로
//        videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//
//                videoView.seekTo(1);
//
//                videoView.start();
//
//            }
//        });
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                //끝났으면 처음으로 돌아간다
//                videoView.seekTo(1);
//            }
//        });

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