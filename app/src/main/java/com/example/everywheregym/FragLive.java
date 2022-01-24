package com.example.everywheregym;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragLive extends Fragment {

//    private FloatingActionButton fab_main;
//    private FloatingActionButton fab_video;
//    private FloatingActionButton fab_live;
//
//    private TextView tv_fb_video;
//    private TextView tv_fb_live;
//
//    private Boolean fab_main_status = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_live,container,false);

//        fab_main = view.findViewById(R.id.fabMain);
//        fab_video = view.findViewById(R.id.fabVideo);
//        fab_live = view.findViewById(R.id.fabLive);
//
//        tv_fb_live = view.findViewById(R.id.tv_fb_live);
//        tv_fb_video = view.findViewById(R.id.tv_fb_video);
//
//        tv_fb_live.setVisibility(View.INVISIBLE);
//        tv_fb_video.setVisibility(View.INVISIBLE);
//
//        fab_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleFab();
//            }
//        });
//
//        fab_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "비디오 추가", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        fab_live.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "라이브 추가", Toast.LENGTH_SHORT).show();
//            }
//        });



        return view;
    }


//    public void toggleFab() {
//        if(fab_main_status) {
//            ObjectAnimator fv_animation = ObjectAnimator.ofFloat(fab_video,"translationY",0f);
//            ObjectAnimator fv_tv_animation = ObjectAnimator.ofFloat(tv_fb_video,"translationY",0f);
//            fv_animation.start();
//            fv_tv_animation.start();
//            ObjectAnimator fl_animation = ObjectAnimator.ofFloat(fab_live,"translationY",0f);
//            ObjectAnimator fl_tv_animation = ObjectAnimator.ofFloat(tv_fb_live,"translationY",0f);
//            fl_animation.start();
//            fl_tv_animation.start();
//            //플로팅 이미지 변경
//            tv_fb_video.setVisibility(View.INVISIBLE);
//            tv_fb_live.setVisibility(View.INVISIBLE);
//            fab_main.setImageResource(R.drawable.ic_baseline_add_24);
//        }else {
//            ObjectAnimator fv_animation = ObjectAnimator.ofFloat(fab_video,"translationY",-200f);
//            ObjectAnimator fv_tv_animation = ObjectAnimator.ofFloat(tv_fb_video,"translationY",-200f);
//            fv_animation.start();
//            fv_tv_animation.start();
//            ObjectAnimator fl_animation = ObjectAnimator.ofFloat(fab_live,"translationY",-400f);
//            ObjectAnimator fl_tv_animation = ObjectAnimator.ofFloat(tv_fb_live,"translationY",-400f);
//            fl_animation.start();
//            fl_tv_animation.start();
//            //플로팅 이미지 변경
//            tv_fb_video.setVisibility(View.VISIBLE);
//            tv_fb_live.setVisibility(View.VISIBLE);
//            fab_main.setImageResource(R.drawable.ic_baseline_clear_24);
//        }
//
//        fab_main_status = !fab_main_status;
//    }

}