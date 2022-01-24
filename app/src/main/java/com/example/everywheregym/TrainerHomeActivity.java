package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class TrainerHomeActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView; //바텀 네이게이션 뷰 (하단바)
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragLive frag_live; //라이브
    private FragVideo frag_video; //VOD
    private FragTrHome frag_tr_home; //트레이너 홈
    private FragTrMypage frag_tr_mypage; //트레이너 마이페이지


    private FloatingActionButton fab_main;
    private FloatingActionButton fab_video;
    private FloatingActionButton fab_live;

    private TextView tv_fb_video;
    private TextView tv_fb_live;

    private Boolean fab_main_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_home);


        fab_main = findViewById(R.id.fabMain);
        fab_video = findViewById(R.id.fabVideo);
        fab_live = findViewById(R.id.fabLive);

        tv_fb_live = findViewById(R.id.tv_fb_live);
        tv_fb_video = findViewById(R.id.tv_fb_video);

        tv_fb_live.setVisibility(View.INVISIBLE);
        tv_fb_video.setVisibility(View.INVISIBLE);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });

        fab_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrainerHomeActivity.this, "비디오 추가", Toast.LENGTH_SHORT).show();
            }
        });

        fab_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrainerHomeActivity.this, "라이브 추가", Toast.LENGTH_SHORT).show();
            }
        });


        bottomNavigationView = findViewById(R.id.bottomNavi_tr);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_video:
                        setFrag(0);
                        break;
                    case R.id.action_live:
                        setFrag(1);
                        break;
                    case R.id.action_add:

//                        AlertDialog.Builder ad = new AlertDialog.Builder(TrainerHomeActivity.this);
//                        ad.setTitle("알림");
//                        ad.setMessage("체크체크");
//                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//
//                        });
//                        AlertDialog alertDialog = ad.create();
//                        alertDialog.show();
                        //여기에다가 두개 추가해서 쓰기


                        setFrag(2);
                        break;
                    case R.id.action_mypage:
                        setFrag(3);
                        break;

                }
                return true;
            }
        });

        frag_live = new FragLive();
        frag_video = new FragVideo();
        frag_tr_home = new FragTrHome();
        frag_tr_mypage = new FragTrMypage();
        setFrag(0);


    }

    public void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame_tr,frag_video);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame_tr,frag_live);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame_tr,frag_tr_home);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame_tr,frag_tr_mypage);
                ft.commit();
                break;
        }
    }

    public void toggleFab() {
        if(fab_main_status) {
            ObjectAnimator fv_animation = ObjectAnimator.ofFloat(fab_video,"translationY",0f);
            ObjectAnimator fv_tv_animation = ObjectAnimator.ofFloat(tv_fb_video,"translationY",0f);
            fv_animation.start();
            fv_tv_animation.start();
            ObjectAnimator fl_animation = ObjectAnimator.ofFloat(fab_live,"translationY",0f);
            ObjectAnimator fl_tv_animation = ObjectAnimator.ofFloat(tv_fb_live,"translationY",0f);
            fl_animation.start();
            fl_tv_animation.start();
            //플로팅 이미지 변경
            tv_fb_video.setVisibility(View.INVISIBLE);
            tv_fb_live.setVisibility(View.INVISIBLE);
            fab_main.setImageResource(R.drawable.ic_baseline_add_24);
        }else {
            ObjectAnimator fv_animation = ObjectAnimator.ofFloat(fab_video,"translationY",-200f);
            ObjectAnimator fv_tv_animation = ObjectAnimator.ofFloat(tv_fb_video,"translationY",-200f);
            fv_animation.start();
            fv_tv_animation.start();
            ObjectAnimator fl_animation = ObjectAnimator.ofFloat(fab_live,"translationY",-400f);
            ObjectAnimator fl_tv_animation = ObjectAnimator.ofFloat(tv_fb_live,"translationY",-400f);
            fl_animation.start();
            fl_tv_animation.start();
            //플로팅 이미지 변경
            tv_fb_video.setVisibility(View.VISIBLE);
            tv_fb_live.setVisibility(View.VISIBLE);

            fab_main.setImageResource(R.drawable.ic_baseline_clear_24);
        }

        fab_main_status = !fab_main_status;
    }


}