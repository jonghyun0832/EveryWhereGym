package com.example.everywheregym;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoCheck extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    public String SAMPLE_VIDEO_URL = "empty";

    private Button btn_next;

    private static final String PLAYBACK_TIME = "play_time";

    //millisecond단위임
    private int mCurrentPosition = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_check);
        Log.d("save", "onCreate: ");

        btn_next = findViewById(R.id.btn_video_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoCheck.this,VodDetailActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences= this.getSharedPreferences("video", Context.MODE_PRIVATE);
        String get_uri = sharedPreferences.getString("v_uri","0");

        if(get_uri.equals("0")){
            Toast.makeText(this, "비디오를 다시 선택 해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            SAMPLE_VIDEO_URL = get_uri;
        }


        //SAMPLE_VIDEO_URL = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/src/video/4_1642659633875";

        // 비디오 재생
        videoView = (VideoView) findViewById(R.id.videoView);


        //중단점 있으면 가져오기
        if(savedInstanceState != null){
            Log.d("save", "onCreate: 중단점 불러오기" + mCurrentPosition);
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        // 비디오 컨트롤러 UI
        mediaController = new MediaController(this);

        //mediaController.setMediaPlayer(videoView);

        // 비디오뷰에 컨트롤러 설정
        videoView.setMediaController(mediaController);

        // 비디오 재생경로
        videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if(mCurrentPosition > 0){
                    videoView.seekTo(mCurrentPosition);
                } else {
                    videoView.seekTo(1);
                }

                videoView.start();

            }
        });

        // 비디오 뷰 포커싱
        //videoView.requestFocus();

        // 비디오 재생
        //videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //끝났으면 처음으로 돌아간다
                videoView.seekTo(0);
            }
        });

    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        if(savedInstanceState != null){
//            Log.d("save", "onCreate: 중단점 불러오기" + mCurrentPosition);
//            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
//        }
//
//    }

    private void relesePlayer(){
        videoView.stopPlayback();
    }



    @Override
    protected void onPause() {
        super.onPause();

        Log.d("save", "onPause: ");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            videoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("save", "onStop: ");

        relesePlayer();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("save", "onSaveInstanceState: 저장하는곳");

        outState.putInt(PLAYBACK_TIME,videoView.getCurrentPosition());
    }



}