package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TrainerHomeActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView; //바텀 네이게이션 뷰 (하단바)
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragLive frag_live; //라이브
    private FragVideo frag_video; //VOD
    private FragTrHome frag_tr_home; //트레이너 홈
    private FragTrMypage frag_tr_mypage; //트레이너 마이페이지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_home);


        bottomNavigationView = findViewById(R.id.bottomNavi_tr);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_live:
                        setFrag(1);
                        break;
                    case R.id.action_video:
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
                ft.replace(R.id.main_frame_tr,frag_tr_home);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame_tr,frag_live);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame_tr,frag_video);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame_tr,frag_tr_mypage);
                ft.commit();
                break;
        }
    }


}