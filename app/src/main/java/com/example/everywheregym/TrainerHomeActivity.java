package com.example.everywheregym;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

public class TrainerHomeActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView; //바텀 네이게이션 뷰 (하단바)
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragLive frag_live; //라이브
    private FragVideo frag_video; //VOD
    private FragTrHome frag_tr_home; //트레이너 홈
    private FragTrMypage frag_tr_mypage; //트레이너 마이페이지


    //플로팅 버튼용
    private FloatingActionButton fab_main;
    private FloatingActionButton fab_video;
    private FloatingActionButton fab_live;
    private TextView tv_fb_video;
    private TextView tv_fb_live;
    private Boolean fab_main_status = false;

    //동영상 업로드용
    private Uri video_uri;
    private String video_path;

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


        ActivityResultLauncher<Intent> activityLauncher_getVideo = registerForActivityResult( //갤러리에서 이미지 받아오기
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Log.d("TAG", "gallery_pick ");
                            if (result.getData() != null) {
                                Intent intent = new Intent(TrainerHomeActivity.this, VideoCheck.class);
                                startActivity(intent);
                                video_uri = result.getData().getData(); //얠 보내서 동영상 재생
                                Log.d("VIDEO", "화면에 띄울 파일의 uri: " + video_uri);
                                video_path = getRealPathFromURI2(TrainerHomeActivity.this, video_uri); //파일 만들때 쓸거임
                                Log.d("VIDEO", "절대경로 : " + video_path);

                                SharedPreferences sharedPreferences= TrainerHomeActivity.this.getSharedPreferences("video", MODE_PRIVATE);
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putString("v_uri",video_uri.toString()); //쓸때는 파싱해서 써야함
                                editor.putString("v_path",video_path);
                                editor.commit();


//                                Intent intent = new Intent(TrainerHomeActivity.this, VideoCheck.class);
//                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(TrainerHomeActivity.this, "영상 선택 에러발생", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );



        fab_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ted();

                Intent vod_intent = new Intent(Intent.ACTION_PICK);
                vod_intent.setType("video/*");
                activityLauncher_getVideo.launch(vod_intent);

                toggleFab();
            }
        });

        fab_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrainerHomeActivity.this, "라이브 추가", Toast.LENGTH_SHORT).show();

                Intent live_intent = new Intent(TrainerHomeActivity.this,LiveCreateActivity_1.class);
                startActivity(live_intent);

                toggleFab();
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
                    //case R.id.action_add:

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


                        //setFrag(2);
                        //break;
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

    //fragment 전환용
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
//            case 2:
//                ft.replace(R.id.main_frame_tr,frag_tr_home);
//                ft.commit();
//                break;
            case 3:
                ft.replace(R.id.main_frame_tr,frag_tr_mypage);
                ft.commit();
                break;
        }
    }

    //플로팅 버튼 액션 만들어주기
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



    public static String getRealPathFromURI2(Context context, Uri uri){

        if(DocumentsContract.isDocumentUri(context,uri)){

            //External storage provider
            if(isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                Log.d("VIDEO", "split전 데이터: " + docId);
                final String[] split = docId.split(":");
                final String type = split[0];
                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    String SDcardpath = getRemovableSDCardPath(context).split("/Android")[0];
                    return SDcardpath + "/" + split[1];
                }
            }

            //Downloads provider
            else if (isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id)
                );

                return getDataColumn(context, contentUri, null, null);
            }

            //MediaProvider
            else if (isMediaDocumnet(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if("video".equals(type)){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            //remote address를 리턴한다
            if(isGooglePhotosUri(uri)){
                return uri.getLastPathSegment();
            }
            return getDataColumn(context,uri,null,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocumnet(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getRemovableSDCardPath(Context context){
        File[] storage = ContextCompat.getExternalFilesDirs(context,null);
        Log.d("VIDEO", "getRemovableSDCardPath: " + storage);
        if(storage.length > 1 && storage[0] != null && storage[1] != null){
            return storage[1].toString();
        } else {
            return "";
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try{
            cursor = context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
            if(cursor!=null && cursor.moveToFirst()){
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return null;
    }


    void ted(){
        PermissionListener permissionLitener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.d("permission", "onPermissionDenied: 요청 실패");
            }
        };

        TedPermission.with(TrainerHomeActivity.this).setPermissionListener(permissionLitener)
                .setDeniedMessage("동영상을 업로드 하기 위해 접근 권한이 필요합니다.")
                .setPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .check();


    }


}