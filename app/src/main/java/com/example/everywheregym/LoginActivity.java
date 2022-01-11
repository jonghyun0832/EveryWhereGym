package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText et_login_email;
    EditText et_login_password;
    Button btn_find_password;
    Button btn_login;
    Button btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_email = (EditText) findViewById(R.id.edittext_login_email);
        et_login_password = (EditText) findViewById(R.id.edittext_login_passward);
        btn_find_password = (Button) findViewById(R.id.btn_find_passward);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);


        btn_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "비밀번호찾기", Toast.LENGTH_SHORT).show();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                String login_email = et_login_email.getText().toString();
                String login_password = et_login_password.getText().toString();
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<UserInfo> call = apiInterface.userLogin(login_email,login_password);
                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if(response.body().isSuccess()){
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);
                            ad.setMessage("아이디 또는 비밀번호가 잘못 입력 되었습니다.\n" +
                                    "아이디와 비밀번호를 정확히 입력해 주세요.");
                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //비밀번호 공란으로 만들기
                                    et_login_password.setText("");
                                }
                            });
                            AlertDialog alertDialog = ad.create();
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "응답 에러", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "회원가입", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}