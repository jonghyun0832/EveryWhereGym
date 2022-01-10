package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText et_signup_email;
    EditText et_signup_certify;
    EditText et_signup_password;
    EditText et_signup_repassword;
    EditText et_signup_name;
    Button btn_certify_send;
    Button btn_certify_check;
    Button btn_duplicate_check;
    Button btn_more_service;
    Button btn_more_data;
    Button btn_complete_signup;
    CheckBox cb_service;
    CheckBox cb_data;
    TextView tv_limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_signup_email = (EditText) findViewById(R.id.edittext_signup_email);
        et_signup_certify = (EditText) findViewById(R.id.edittext_signup_certification);
        et_signup_password = (EditText) findViewById(R.id.edittext_signup_password);
        et_signup_repassword = (EditText) findViewById(R.id.edittext_signup_repassword);
        et_signup_name = (EditText) findViewById(R.id.edittext_signup_name);
        btn_certify_send = (Button) findViewById(R.id.btn_certification_send);
        btn_certify_check = (Button) findViewById(R.id.btn_certificataion_check);
        btn_duplicate_check = (Button) findViewById(R.id.btn_duplication_check);
        btn_more_service = (Button) findViewById(R.id.btn_more_service);
        btn_more_data = (Button) findViewById(R.id.btn_more_data);
        btn_complete_signup = (Button) findViewById(R.id.btn_complete_signup);
        cb_service = (CheckBox) findViewById(R.id.checkBox_service);
        cb_data = (CheckBox) findViewById(R.id.checkBox_data);
        tv_limit = (TextView) findViewById(R.id.textview_time_limit);




        btn_certify_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "메일러로 인증번호 보내기", Toast.LENGTH_SHORT).show();
                sendCertifyMail(et_signup_email.getText().toString());
            }
        });

        btn_certify_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "인증번호 맞나 검사하기", Toast.LENGTH_SHORT).show();

            }
        });

        btn_duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "데이터 중복검사하기", Toast.LENGTH_SHORT).show();
            }
        });


        btn_more_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
//                ad.setTitle("서비스 이용약관");
//                LayoutInflater inflater = (LayoutInflater) SignUpActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View dialogView = inflater.inflate(R.layout.dialog_signup_more_service, null);
//                ad.setView(dialogView);

                ad.setMessage(R.string.more_service);
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(SignUpActivity.this, "확인누름", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();

//                Button btn_alert = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                btn_alert.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.round_button_gray));


            }
        });

        btn_more_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
                ad.setMessage(R.string.more_data);
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });

        btn_complete_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "회원가입 유효성 검사", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendCertifyMail(String email) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Toast.makeText(SignUpActivity.this, email+"들어감", Toast.LENGTH_SHORT).show();
        Call<UserInfo> call = apiInterface.sendMail(email);
        call.enqueue(new Callback<UserInfo>()
        {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                Toast.makeText(SignUpActivity.this, "제대로 보냈습니다", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null)
                {
                    String getted_email = response.body().getEmail();
                    String getted_cnum = response.body().getCnum();
                    if(getted_email == null){
                        Toast.makeText(SignUpActivity.this, "온게없어여", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(SignUpActivity.this, "서버에서 이메일 : " + getted_email + "숫자 : " + getted_cnum, Toast.LENGTH_SHORT).show();
                    }
                    Log.e("getNameHobby()", "서버에서 이메일 : " + getted_email + "숫자 : " + getted_cnum);
                    tv_limit.setText("카운트시작");
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}