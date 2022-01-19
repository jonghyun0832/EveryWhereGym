package com.example.everywheregym;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoCheck extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    public String SAMPLE_VIDEO_URL = "empty";

    Button btn_next;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_check);

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


        //SAMPLE_VIDEO_URL = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/video/VID_20220119_020817.mp4";

        // 비디오 재생
        videoView = (VideoView) findViewById(R.id.videoView);

        // 비디오 컨트롤러 UI
        mediaController = new MediaController(this);

        // 비디오뷰에 컨트롤러 설정
        videoView.setMediaController(mediaController);

        // 비디오 재생경로
        videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));

        // 비디오 뷰 포커싱
        videoView.requestFocus();

        // 비디오 재생
        videoView.start();


    }
}