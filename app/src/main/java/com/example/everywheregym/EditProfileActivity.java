package com.example.everywheregym;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    public static Context context; //다이얼로그용 컨텍스트

    static final int PERMISSION_REQUEST_CODE = 1;

    private AlertDialog alertDialog;

    private ImageView iv_pf_edit_back;
    private Button btn_pf_edit_complete;
    private ImageView iv_pf_edit_image;
    private EditText et_pf_edit_name;

    private Button btn_galary;
    private Button btn_camera;

    private Bitmap bitmap;

    boolean ischange = false;

    private String url;
    private String prev_url = "";

    private String user_id;
    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        context = this; //컨텍스트 설정

        iv_pf_edit_back = findViewById(R.id.imageview_profile_edit_back);
        btn_pf_edit_complete = findViewById(R.id.button_profile_edit_complete);
        iv_pf_edit_image = findViewById(R.id.iv_edit_mypage_user_img);
        et_pf_edit_name = findViewById(R.id.edittext_profile_edit_name);

        SharedPreferences sharedPreferences= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");


        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        String user_img = intent.getStringExtra("user_img"); //url string

        et_pf_edit_name.setText(user_name);

        //String user_img = sharedPreferences.getString("user_img","0");
        if (user_img == null || user_img.equals("")){
            url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        } else {
            url = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + user_img;
            prev_url = user_img;
        }
        Glide.with(this).load(url).override(250,250).into(iv_pf_edit_image);
        //이전 url (서버 삭제용)





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED){
                Log.d("permission", "권한 설정 완료");
            }
            else {
                Log.d("permission", "권한 설정 요청");

                ActivityCompat.requestPermissions(EditProfileActivity.this,new String[]
                        {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
        }



        iv_pf_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_pf_edit_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ischange){
                    //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    //String filename = user_id + "_IMAGE_" + timeStamp;
                    String filename = "IMAGE";

//                    SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
//                    SharedPreferences.Editor editor= sharedPreferences.edit();
//                    editor.putString("user_img",filename + ".jpeg");
//                    editor.commit();

                    File f = new File(context.getCacheDir(),filename);
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,60,bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = null;
                    try{
                        fos = new FileOutputStream(f);

                    }catch (Exception e){
                        e.printStackTrace();
                    }try{
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile);

                    ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<UserInfo> call2 = apiInterface2.uploadFile(body,prev_url,user_id);
                    call2.enqueue(new Callback<UserInfo>() {
                        @Override
                        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                            if (response.isSuccessful()){
                                String input_name = et_pf_edit_name.getText().toString();

                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<UserInfo> call3 = apiInterface.profileEdit(user_id,input_name);
                                call3.enqueue(new Callback<UserInfo>() {
                                    @Override
                                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                        if (response.isSuccessful() && response.body() != null){
                                            if(response.body().isSuccess()){
                                                finish();
                                            } else {
                                                Toast.makeText(EditProfileActivity.this, "서버단 저장 오류", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserInfo> call, Throwable t) {
                                        Toast.makeText(EditProfileActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(EditProfileActivity.this, "수정 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfo> call, Throwable t) {
                            Toast.makeText(EditProfileActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    String input_name = et_pf_edit_name.getText().toString();

                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<UserInfo> call = apiInterface.profileEdit(user_id,input_name);
                    call.enqueue(new Callback<UserInfo>() {
                        @Override
                        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                            if (response.isSuccessful() && response.body() != null){
                                if(response.body().isSuccess()){
                                    finish();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "서버단 저장 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfo> call, Throwable t) {
                            Toast.makeText(EditProfileActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //바뀐 이미지, 텍스트 서버 통신해서 저장하기, 돌아가기

            }
        });

        iv_pf_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(EditProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_alert_menu, null);

                btn_galary = dialogView.findViewById(R.id.button_img_galary);
                btn_camera = dialogView.findViewById(R.id.button_img_camera);

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
                                Uri ProviderURI = FileProvider.getUriForFile(EditProfileActivity.this, "com.example.everywheregym.provider", photoFile);
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
                            iv_pf_edit_image.setImageBitmap(bitmap);
                            ischange = true;
                        }
                    }
                }
            }
    );


    ActivityResultLauncher<Intent> activityLauncher_gallery = registerForActivityResult( //갤러리에서 이미지 받아오기
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
                            iv_pf_edit_image.setImageBitmap(bitmap);
                            ischange = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

//    private void cropImage(Uri uri){
//        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
//                .setCropShape(CropImageView.CropShape.RECTANGLE)
//                //사각형 모양으로 자른다
//                .start(this);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //크롭 성공시
            if (resultCode == RESULT_OK) {
                iv_pf_edit_image.setImageURI(result.getUri());

                //실패시
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
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