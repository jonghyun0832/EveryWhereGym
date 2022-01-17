package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    ImageView iv_setting_back;
    TextView tv_switch_push;
    Switch sh_push;
    Button btn_logout;
    Button btn_destroy_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Log.d("TAG", "SETTINGonCreate: ");

        iv_setting_back = (ImageView) findViewById(R.id.imageview_setting_back);
        tv_switch_push = (TextView) findViewById(R.id.textview_switch_push);
        sh_push = (Switch) findViewById(R.id.switch_push);
        btn_logout = (Button) findViewById(R.id.button_logout);
        btn_destroy_account = (Button) findViewById(R.id.button_delete_account);



        iv_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sh_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    tv_switch_push.setText("푸시 알림 ON");
                } else {
                    tv_switch_push.setText("푸시 알림 OFF");
                };
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그아웃 (로그인 설정되어있는것 해제해주기
                AlertDialog.Builder ad = new AlertDialog.Builder(SettingActivity.this);
                ad.setTitle("알림");
                ad.setMessage("로그아웃 하시겠습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //쉐어드 유저정보 삭제
                        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_id");
                        editor.remove("is_trainer");
                        editor.commit();

                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });

        btn_destroy_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SettingActivity.this);
                ad.setTitle("회원탈퇴");
                ad.setMessage("정말 회원탈퇴 하시겠습니까?");
                ad.setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(SettingActivity.this, "회원탈퇴가 완료되었습니다", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
                        String user_id = sharedPreferences.getString("user_id","");

                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<UserInfo> call = apiInterface.destroyAccount(user_id);
                        call.enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if (response.isSuccessful() && response.body() != null)
                                {
                                    if(response.body().isSuccess()){
                                        //쉐어드 유저id 삭제
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("user_id");
                                        editor.remove("is_trainer");
                                        editor.commit();

                                        Toast.makeText(SettingActivity.this, "회원탈퇴가 완료되었습니다", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Toast.makeText(SettingActivity.this, "회원탈퇴 실패(id 오류)", Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(SettingActivity.this, "회원탈퇴 실패(json 오류)", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                Toast.makeText(SettingActivity.this, "회원탈퇴 실패(통신 오류)", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
                //회원탈퇴
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "SETTINGonPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "SETTINGonDestroy: ");
    }
}