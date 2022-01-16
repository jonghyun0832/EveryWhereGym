package com.example.everywheregym;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    public static Context context; //다이얼로그용 컨텍스트

    static final int PERMISSION_REQUEST_CODE = 1;

    AlertDialog alertDialog;

    ImageView iv_pf_edit_back;
    Button btn_pf_edit_complete;
    ImageView iv_pf_edit_image;
    EditText et_pf_edit_name;

    Button btn_galary;
    Button btn_camera;
    Button btn_default;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        context = this; //컨텍스트 설정

        iv_pf_edit_back = findViewById(R.id.imageview_profile_edit_back);
        btn_pf_edit_complete = findViewById(R.id.button_profile_edit_complete);
        iv_pf_edit_image = findViewById(R.id.circle_iv_edit_profile);
        et_pf_edit_name = findViewById(R.id.edittext_profile_edit_name);

        SharedPreferences sharedPreferences= this.getSharedPreferences("info", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","0");


        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        int user_img = intent.getIntExtra("user_img",0); //진짜 url오면 받는걸로 바꿔야함

        et_pf_edit_name.setText(user_name);
        Glide.with(this).load(user_img).override(250,250).into(iv_pf_edit_image);



        iv_pf_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_pf_edit_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //바뀐 이미지, 텍스트 서버 통신해서 저장하기, 돌아가기
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
        });

        iv_pf_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(EditProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_alert_menu, null);

                btn_galary = dialogView.findViewById(R.id.button_img_galary);
                btn_camera = dialogView.findViewById(R.id.button_img_camera);
                btn_default = dialogView.findViewById(R.id.button_img_default);

                //갤러리에서 이미지 선택
                btn_galary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "갤러리", Toast.LENGTH_SHORT).show();
                    }
                });

                //카메라로 찍어서 이미지 업로드
                btn_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "카메라", Toast.LENGTH_SHORT).show();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                                Log.d("permission", "권한 설정 완료");
//                            }
//                            else{
//                                Log.d("permission", "권한 설정 요청");
//
//                                ActivityCompat.requestPermissions(EditProfileActivity.this,new String[]
//                                        {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
//                            }
//                        }
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        activityLauncher_camera.launch(cameraIntent);



                    }
                });

                //기본 이미지로 설정
                btn_default.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "기본", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "권한을 허용해주세요\n기능 사용이 제한될 수 있습니다.", Toast.LENGTH_SHORT).show();
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
                        Intent cameraIntent = result.getData();
                        Bitmap bitmap = (Bitmap) cameraIntent.getExtras().get("data");
                        if(bitmap != null){
                            Log.d("TAG", "onActivityResult:비트맵안비었음 ");
                            iv_pf_edit_image.setImageBitmap(bitmap);
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