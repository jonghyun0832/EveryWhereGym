package com.example.everywheregym;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {


    public BottomNavigationView bottomNavigationView; //바텀 네이게이션 뷰 (하단바)
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragLive frag_live; //라이브
    private FragVideo frag_video; //VOD
    private FragMypage frag_mypage; //마이페이지

    static final int PERMISSION_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("TAG", "HomeonCreate: ");


        //권한 획득 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED){
                Log.d("permission", "권한 설정 완료");
            }
            else {
                Log.d("permission", "권한 설정 요청");


//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//
//                    // 이전에 거부한 경우 권한 필요성 설명 및 권한 요청
//
//                } else {
//
//                    // 처음 요청하는 경우 그냥 권한 요청
//
//                }

                ActivityCompat.requestPermissions(HomeActivity.this,new String[]
                        {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
        }







        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_live:
                        setFrag(0);
                        break;
                    case R.id.action_video:
                        setFrag(1);
                        break;
                    case R.id.action_mypage:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        frag_live = new FragLive();
        frag_video = new FragVideo();
        frag_mypage = new FragMypage();
        setFrag(0);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "HOMEonPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "HOMEonDestroy: ");
    }

    public void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame,frag_live);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame,frag_video);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame,frag_mypage);
                ft.commit();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Log.d("permission", "PERMISSION: " + permissions[0] + "was" + grantResults[0]);
        }else {
            Toast.makeText(this, "권한을 허용해주세요\n기능 사용이 제한될 수 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}