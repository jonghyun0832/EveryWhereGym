package com.example.everywheregym;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class TrainerEditProfileActivity extends AppCompatActivity {

    AlertDialog alertDialog;

    Bitmap bitmap;
    Boolean ischange;

    ImageView iv_edit_image;
    ImageView iv_edit_backimage;
    EditText et_edit_name;
    EditText et_edit_intro;
    EditText et_edit_expert;
    EditText et_edit_career;
    EditText et_edit_certify;
    Button btn_edit_complete;
    Button btn_edit_cancel;


    static final int PERMISSION_REQUEST_CODE = 1;

    String img_url; //프로필 이미지
    String back_img_url; //배경 이미지
    String prev_img_url = "";
    String prev_back_img_url = "";

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_edit_profile);

        iv_edit_image = findViewById(R.id.iv_tr_mypage_img_edit);
        iv_edit_backimage = findViewById(R.id.iv_tr_mypage_background_edit);
        et_edit_name = findViewById(R.id.edittext_tr_profile_name_edit);
        et_edit_intro = findViewById(R.id.edittext_tr_profile_intro_edit);
        et_edit_expert = findViewById(R.id.edittext_tr_profile_expert_edit);
        et_edit_career = findViewById(R.id.edittext_tr_profile_career_edit);
        et_edit_certify = findViewById(R.id.edittext_tr_profile_certiry_edit);
        btn_edit_complete = findViewById(R.id.btn_mypage_edit_complete);
        btn_edit_cancel = findViewById(R.id.btn_mypage_edit_cancel);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED){
                Log.d("permission", "권한 설정 완료");
            }
            else {
                Log.d("permission", "권한 설정 요청");

                ActivityCompat.requestPermissions(TrainerEditProfileActivity.this,new String[]
                        {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
        }


        SharedPreferences sharedPreferences= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");


        //수정용으로 이전 데이터들 intent로 받아옴
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        String user_img = intent.getStringExtra("user_img"); //url string
        String tr_img = intent.getStringExtra("tr_img");
        String tr_intro = intent.getStringExtra("tr_intro");
        String tr_expert = intent.getStringExtra("tr_expert");
        String tr_career = intent.getStringExtra("tr_career");
        String tr_certify = intent.getStringExtra("tr_certify");


        //프로필 편집 기본값 불러오기

        et_edit_name.setText(user_name);
        et_edit_intro.setText(tr_intro);
        et_edit_expert.setText(tr_expert);
        et_edit_career.setText(tr_career);
        et_edit_certify.setText(tr_certify);

        //배경 이미지 설정
        if (tr_img == null){
            back_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_back_image.jpeg";
        } else {
            back_img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + tr_img;
            prev_back_img_url = tr_img;
        }
        Glide.with(this).load(back_img_url).into(iv_edit_backimage);
        //Glide.with(this).load(back_img_url).override(250,250).into(iv_edit_backimage);

        //프로필 이미지 설정
        if (user_img == null){
            img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        } else {
            img_url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + user_img;
            prev_img_url = user_img;
        }
        Glide.with(this).load(img_url).override(250,250).into(iv_edit_image);


        //배경화면 이미지 클릭시 선택
        iv_edit_backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TrainerEditProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) TrainerEditProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_alert_menu, null);

                Button btn_galary = dialogView.findViewById(R.id.button_img_galary);
                Button btn_camera = dialogView.findViewById(R.id.button_img_camera);

                //갤러리에서 이미지 선택
                btn_galary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK); //MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        galleryIntent.setType("image/*");
                        //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        activityLauncher_back_gallery.launch(galleryIntent);
                    }
                });

                //카메라로 찍어서 이미지 업로드
                btn_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        activityLauncher_back_camera.launch(cameraIntent);

                    }
                });

                ad.setTitle("프로필 사진 설정");
                ad.setView(dialogView);
                alertDialog = ad.create();
                alertDialog.show();

            }
        });

        //프로필 이미지 클릭시 선택
        iv_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TrainerEditProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) TrainerEditProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_alert_menu, null);

                Button btn_galary = dialogView.findViewById(R.id.button_img_galary);
                Button btn_camera = dialogView.findViewById(R.id.button_img_camera);

                //갤러리에서 이미지 선택
                btn_galary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK); //MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        galleryIntent.setType("image/*");
                        //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        activityLauncher_gallery.launch(galleryIntent);
                    }
                });

                //카메라로 찍어서 이미지 업로드
                btn_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        activityLauncher_camera.launch(cameraIntent);




                    }
                });

                ad.setTitle("프로필 사진 설정");
                ad.setView(dialogView);
                alertDialog = ad.create();
                alertDialog.show();

            }
        });



        btn_edit_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //레트로핏으로 보내서 정보들 저장하기
                //여기서 그 ischange = true일때 2번
                //아닐때 1번 으로 해결 했었음 여기도 그렇게
            }
        });


        btn_edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Log.d("permission", "PERMISSION: " + permissions[0] + "was" + grantResults[0]);
        }else {
            //Toast.makeText(this, "다시시도해주세요", Toast.LENGTH_SHORT).show();
        }
    }


    //프로필 이미지 사진찍어서 가져오기
    ActivityResultLauncher<Intent> activityLauncher_camera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("TAG", "onActivityResult:LAUNCH ");
                    if (result.getResultCode() == RESULT_OK){ //코드가 맞을경우
                        Log.d("TAG", "onActivityResult:RESULT OK ");
                        Intent cameraIntent = result.getData();
                        bitmap = (Bitmap) cameraIntent.getExtras().get("data");
                        if(bitmap != null){
                            Log.d("TAG", "onActivityResult:비트맵안비었음 ");
                            iv_edit_image.setImageBitmap(bitmap);
                            ischange = true;
                        }
                    }
                }
            }
    );


    //프로필 이미지 갤러리에서 받아오기
    ActivityResultLauncher<Intent> activityLauncher_gallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("TAG", "gallery_pick ");
                    Intent galleryIntent = result.getData();
                    if(galleryIntent != null){
                        try {
                            //resolver, uri 넣어서 비트맵으로
//                            Uri uri = galleryIntent.getData();
//                            iv_pf_edit_image.setImageURI(uri);
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),galleryIntent.getData()
                            );
                            iv_edit_image.setImageBitmap(bitmap);
                            ischange = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    //배경이미지 카메라로 받아오기
    ActivityResultLauncher<Intent> activityLauncher_back_camera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("TAG", "onActivityResult:LAUNCH ");
                    if (result.getResultCode() == RESULT_OK){ //코드가 맞을경우
                        Log.d("TAG", "onActivityResult:RESULT OK ");
                        Intent cameraIntent = result.getData();
                        bitmap = (Bitmap) cameraIntent.getExtras().get("data");
                        if(bitmap != null){
                            Log.d("TAG", "onActivityResult:비트맵안비었음 ");
                            iv_edit_backimage.setImageBitmap(bitmap);
                            ischange = true;
                        }
                    }
                }
            }
    );

    //배경이미지 갤러리로 받아오기
    ActivityResultLauncher<Intent> activityLauncher_back_gallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("TAG", "gallery_pick ");
                    Intent galleryIntent = result.getData();
                    if(galleryIntent != null){
                        try {
                            //resolver, uri 넣어서 비트맵으로
//                            Uri uri = galleryIntent.getData();
//                            iv_pf_edit_image.setImageURI(uri);
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),galleryIntent.getData()
                            );
                            iv_edit_backimage.setImageBitmap(bitmap);
                            ischange = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );





    @Override
    protected void onPause() {
        super.onPause();
        try{
            alertDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}