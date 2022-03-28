package com.example.everywheregym;

import android.app.ProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
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
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class TestActivity extends AppCompatActivity {

    private ConstraintLayout con_layout;

    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(this, "blackJin");

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {

            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);

        } else if (uri.getLastPathSegment().contains("m3u8")) {


            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);

        } else {

            return new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent))
                    .createMediaSource(uri);
        }

    }

    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;

    private boolean fullscreen = false;
    private ImageView fullscreenButton;

//    private VideoView videoView;
//    private MediaController mediaController;

    private TextView tv_category;
    private TextView tv_title;
    private TextView tv_view;
    private TextView tv_difficulty;
    private TextView tv_calorie;
    private TextView tv_material;

    private ImageView iv_uploader_img;
    private TextView tv_uploader_name;
    private ImageView iv_arrow;

    private ImageView iv_bookmark;

    private ImageView iv_loading_thumbnail;

    private TextView tv_explain;

    private FrameLayout fr_show_profile;

    private String user_id;
    private String vod_uploader_img_url;

    private boolean isMark;



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

        con_layout = findViewById(R.id.layout);


        exoPlayerView = findViewById(R.id.exoPlayerView);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);

        //videoView = findViewById(R.id.videoView_vod_show);

        tv_category = findViewById(R.id.tv_vod_show_category);
        tv_title = findViewById(R.id.tv_vod_show_title);
        tv_view = findViewById(R.id.tv_vod_show_view);
        tv_difficulty = findViewById(R.id.tv_vod_show_difficulty);
        tv_calorie = findViewById(R.id.tv_vod_show_calorie);
        tv_material = findViewById(R.id.tv_vod_show_material);

        iv_uploader_img = findViewById(R.id.iv_vod_show_pf_img);
        tv_uploader_name = findViewById(R.id.tv_vod_show_name);
        iv_arrow = findViewById(R.id.iv_vod_show_arrow);

        iv_bookmark = findViewById(R.id.iv_btn_bookmark);

        tv_explain = findViewById(R.id.tv_vod_show_explain);

        fr_show_profile = findViewById(R.id.fr_vod_show);

        //prBar = findViewById(R.id.progressBar);
        iv_loading_thumbnail = findViewById(R.id.iv_loading_thumbnail);


//        //아직 안쓰는데 쓰면 이사람 영상가져올떄쓸듯
        SharedPreferences sharedPreferences= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

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

        //시청기록에 추가하기
        addHistory(user_id,vod_id);

        //북마크 체크하기
        isBookMark(user_id,vod_id);

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

        initializePlayer();
//        tv_title.setText(vod_title);
//        tv_difficulty.setText(vod_difficulty);

        //mediaController = new MediaController(TestActivity.this);

//        mediaController.setAnchorView(videoView);
//        mediaController.setMediaPlayer(videoView);

        // 비디오뷰에 컨트롤러 설정
        //videoView.setMediaController(mediaController);

        // 비디오 재생경로
//        videoView.setVideoURI(Uri.parse(SAMPLE_VIDEO_URL));
//        Log.d("IMG", "onCreate리스너 들어가기 전: ");
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                Log.d("IMG", "onCreate비디오 준비됨: ");
//                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
//                        Log.d("IMG", "onCreate비디오 사이즈 바뀌었을때: ");
//                        mediaController = new MediaController(TestActivity.this);
//                        videoView.setMediaController(mediaController);
//                        mediaController.setAnchorView(videoView);
//                    }
//                });
//                videoView.seekTo(1);
//
//                videoView.start();
////                hidepDialog();
//                prBar.setVisibility(View.INVISIBLE);
//                iv_loading_thumbnail.setVisibility(View.INVISIBLE);
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



        fr_show_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(TestActivity.this, ShowProfileActivity.class);
                intent2.putExtra("uploader_id",vod_uploader_id);
                startActivity(intent2);
                finish();
            }
        });

        iv_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMark){
                    //한번더 누르면 북마크 해제
                    deleteBookMark(user_id,vod_id);
                    iv_bookmark.setImageDrawable(ContextCompat.getDrawable(TestActivity.this,R.drawable.ic_baseline_bookmark_empty));
                } else {
                    //한번더 누르면 북마크 추가
                    addBookMark(user_id,vod_id);
                    iv_bookmark.setImageDrawable(ContextCompat.getDrawable(TestActivity.this,R.drawable.ic_baseline_bookmark_24));
                }
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

    private void isBookMark(String user_id, String vod_id){
        //북마크 확인
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodData> call = apiInterface.checkBookMark(user_id,vod_id);
        call.enqueue(new Callback<VodData>() {
            @Override
            public void onResponse(Call<VodData> call, Response<VodData> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        //북마크 없을때
                        iv_bookmark.setImageDrawable(ContextCompat.getDrawable(TestActivity.this,R.drawable.ic_baseline_bookmark_empty));
                        isMark = false;
                    } else {
                        //이미 북마크 된경우
                        iv_bookmark.setImageDrawable(ContextCompat.getDrawable(TestActivity.this,R.drawable.ic_baseline_bookmark_24));
                        isMark = true;

                    }
                }
            }

            @Override
            public void onFailure(Call<VodData> call, Throwable t) {
                //실패
            }
        });
    }

    private void addBookMark(String user_id, String vod_id){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodData> call = apiInterface.bookmarkVod(user_id,vod_id);
        call.enqueue(new Callback<VodData>() {
            @Override
            public void onResponse(Call<VodData> call, Response<VodData> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        AlertDialog.Builder ad = new AlertDialog.Builder(TestActivity.this);
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
    }

    private void deleteBookMark(String user_id, String vod_id){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodData> call = apiInterface.bookmarkDelete(user_id,vod_id);
        call.enqueue(new Callback<VodData>() {
            @Override
            public void onResponse(Call<VodData> call, Response<VodData> response) {
                if (response.isSuccessful() && response.body() != null){
                    if(response.body().isSuccess()){
                        AlertDialog.Builder ad = new AlertDialog.Builder(TestActivity.this);
                        ad.setTitle("알림");
                        ad.setMessage("해당 동영상의 북마크를 해제했습니다");
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
                        Toast.makeText(TestActivity.this, "서버에서 삭제실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VodData> call, Throwable t) {
                //실패
            }
        });
    }

    private void addHistory(String user_id, String vod_id){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodData> call = apiInterface.addHistory(user_id,vod_id);
        call.enqueue(new Callback<VodData>() {
            @Override
            public void onResponse(Call<VodData> call, Response<VodData> response) {
                //시청기록 추가완료
            }

            @Override
            public void onFailure(Call<VodData> call, Throwable t) {
                Toast.makeText(TestActivity.this, "시청기록 추가 실패", Toast.LENGTH_SHORT).show();
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



    private void initializePlayer() {
        if (player == null) {

            player = ExoPlayerFactory.newSimpleInstance(this.getApplicationContext());

            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    switch (playbackState) {

                        case Player.STATE_IDLE: // 1
                            //재생 실패
                            break;
                        case Player.STATE_BUFFERING: // 2
                            // 재생 준비
                            break;
                        case Player.STATE_READY: // 3
                            // 재생 준비 완료
                            iv_loading_thumbnail.setVisibility(View.INVISIBLE);
                            break;
                        case Player.STATE_ENDED: // 4
                            // 재생 마침
                            break;
                        default:
                            break;
                    }
                }
            });


            fullscreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fullscreen) {
                        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(TestActivity.this, R.drawable.ic_baseline_fullscreen_30));
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().show();
                        }
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
                        params.width = params.MATCH_PARENT;
                        params.height = (int) (225 * getApplicationContext().getResources().getDisplayMetrics().density);
                        exoPlayerView.setLayoutParams(params);
                        fullscreen = false;
                        con_layout.setBackgroundColor(Color.WHITE);
                    } else {
                        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(TestActivity.this, R.drawable.ic_baseline_fullscreen_exit_24));
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().hide();
                        }
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
                        params.width = params.MATCH_PARENT;
                        params.height = params.MATCH_PARENT;
                        exoPlayerView.setLayoutParams(params);
                        fullscreen = true;
                        con_layout.setBackgroundColor(Color.BLACK);
                    }
                }
            });

            //플레이어 연결
            exoPlayerView.setPlayer(player);

        }

        String sample = SAMPLE_VIDEO_URL;

        MediaSource mediaSource = buildMediaSource(Uri.parse(sample));

        //prepare
        player.prepare(mediaSource, true, false);

        //start,stop
        player.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();

            exoPlayerView.setPlayer(null);
            player.release();
            player = null;

        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}