package com.example.everywheregym;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TrainerEditProfileActivity extends AppCompatActivity {

    AlertDialog alertDialog;

    Bitmap bitmap = null;
    Bitmap back_bitmap = null;
    Boolean ischange = false;
    Boolean ischange_back = false;

    ImageView iv_edit_image;
    ImageView iv_edit_backimage;
    EditText et_edit_name;
    EditText et_edit_intro;
    EditText et_edit_expert;
    EditText et_edit_career;
    EditText et_edit_certify;
    Button btn_edit_complete;
    Button btn_edit_cancel;

    File upload_file;
    File upload_file2;

    ArrayList<MultipartBody.Part> images;

    private String currentPhotoPath;


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

                        //photoUri = FileProvider.getUriForFile(this, "com.example.everywheregym.provider",filePath);
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        if(cameraIntent.resolveActivity(getPackageManager()) != null){
                            File photoFile = null;
                            try{
                                photoFile = File.createTempFile(
                                        "temp_image_file",
                                        ".jpeg",
                                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                );
                                currentPhotoPath = photoFile.getAbsolutePath();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if(photoFile!= null){
                                Uri ProviderURI = FileProvider.getUriForFile(TrainerEditProfileActivity.this,"com.example.everywheregym.provider",photoFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ProviderURI);
                                activityLauncher_back_camera.launch(cameraIntent);
                            }
                        }

                        //activityLauncher_back_camera.launch(cameraIntent);

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
                        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = File.createTempFile(
                                        "temp_image_file",
                                        ".jpeg",
                                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                );
                                currentPhotoPath = photoFile.getAbsolutePath();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (photoFile != null) {
                                Uri ProviderURI = FileProvider.getUriForFile(TrainerEditProfileActivity.this, "com.example.everywheregym.provider", photoFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ProviderURI);
                                activityLauncher_camera.launch(cameraIntent);
                            }
                        }
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
                if (ischange){
                    images = new ArrayList<>();
                    if(bitmap != null){
                        upload_file = saveImage(bitmap);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),upload_file);
                        images.add(MultipartBody.Part.createFormData("img_upload",upload_file.getName(), reqFile));
                    }
                    if (back_bitmap != null) {
                        upload_file2 = saveImage2(back_bitmap);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),upload_file2);
                        images.add(MultipartBody.Part.createFormData("img_upload2",upload_file2.getName(), reqFile));
                    }

                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<UserInfo> call = apiInterface.uploadFileTR(images,prev_img_url,prev_back_img_url,user_id);
                    call.enqueue(new Callback<UserInfo>() {
                        @Override
                        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                            if (response.isSuccessful() && response.body() != null){
                                if(response.body().isSuccess()){
                                    String input_name = et_edit_name.getText().toString();
                                    String input_intro = et_edit_intro.getText().toString();
                                    String input_expert = et_edit_expert.getText().toString();
                                    String input_career = et_edit_career.getText().toString();
                                    String input_certify = et_edit_certify.getText().toString();

                                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                    Call<TrainerInfo> call2 = apiInterface.profileEditTR(user_id,input_name,input_intro,
                                            input_expert,input_career,input_certify);
                                    call2.enqueue(new Callback<TrainerInfo>() {
                                        @Override
                                        public void onResponse(Call<TrainerInfo> call2, Response<TrainerInfo> response) {
                                            if (response.isSuccessful() && response.body() != null){
                                                if(response.body().isSuccess()){
                                                    finish();
                                                } else {
                                                    Toast.makeText(TrainerEditProfileActivity.this, "서버단 저장 오류", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<TrainerInfo> call, Throwable t) {
                                            Toast.makeText(TrainerEditProfileActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfo> call, Throwable t) {
                            Toast.makeText(TrainerEditProfileActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    String input_name = et_edit_name.getText().toString();
                    String input_intro = et_edit_intro.getText().toString();
                    String input_expert = et_edit_expert.getText().toString();
                    String input_career = et_edit_career.getText().toString();
                    String input_certify = et_edit_certify.getText().toString();

                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<TrainerInfo> call = apiInterface.profileEditTR(user_id,input_name,input_intro,
                            input_expert,input_career,input_certify);
                    call.enqueue(new Callback<TrainerInfo>() {
                        @Override
                        public void onResponse(Call<TrainerInfo> call, Response<TrainerInfo> response) {
                            if (response.isSuccessful() && response.body() != null){
                                if(response.body().isSuccess()){
                                    finish();
                                } else {
                                    Toast.makeText(TrainerEditProfileActivity.this, "서버단 저장 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TrainerInfo> call, Throwable t) {
                            Toast.makeText(TrainerEditProfileActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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
                        Uri tmp_photo_uri = Uri.fromFile(new File(currentPhotoPath));
//                        Intent cameraIntent = result.getData();
//                        bitmap = (Bitmap) cameraIntent.getExtras().get("data");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            try {
                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),tmp_photo_uri));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),tmp_photo_uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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
                        Uri tmp_photo_uri = Uri.fromFile(new File(currentPhotoPath));
                        //Intent cameraIntent = result.getData();
                        //back_bitmap = (Bitmap) cameraIntent.getExtras().get("data");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            try {
                                back_bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),tmp_photo_uri));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                back_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),tmp_photo_uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(back_bitmap != null){
                            Log.d("TAG", "onActivityResult:비트맵안비었음 ");
                            iv_edit_backimage.setImageBitmap(back_bitmap);
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
                            back_bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),galleryIntent.getData()
                            );
                            iv_edit_backimage.setImageBitmap(back_bitmap);
                            ischange = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    private File saveImage(Bitmap bitmap){
        String filename = "TRAINER_IMAGE";

        File tmp_file = new File(getApplicationContext().getCacheDir(),filename);
        try{
            tmp_file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,bos);
        byte[] bitmapdata = bos.toByteArray();

        //BufferedOutputStream buffos = null;

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(tmp_file);
        }catch (Exception e){
            e.printStackTrace();
        }try{
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tmp_file;
    }

    private File saveImage2(Bitmap bitmap){
        String filename = "TRAINER_IMAGE_2";

        File tmp_file = new File(getApplicationContext().getCacheDir(),filename);
        try{
            tmp_file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,bos);
        byte[] bitmapdata = bos.toByteArray();

        //BufferedOutputStream buffos = null;

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(tmp_file);
        }catch (Exception e){
            e.printStackTrace();
        }try{
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tmp_file;
    }



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