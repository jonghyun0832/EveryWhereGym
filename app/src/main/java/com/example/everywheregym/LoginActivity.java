package com.example.everywheregym;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    Handler handler;
    TimeRunnable timeRunnable;
    Thread thread;

    private int time_sec = 0;
    private int time_min = 0;
    private int i;
    private boolean isRunning;

    private String certifyNum = "";
    private String certifyEmail = "";

    //다이얼로그 삭제용
    AlertDialog alertDialog;
    AlertDialog alertDialog2;

    //다이얼로그용
    TextView tv_find_pw_alert;
    TextView tv_find_pw_limit;
    Button btn_find_pw_send;
    Button btn_find_pw_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_email = (EditText) findViewById(R.id.edittext_login_email);
        et_login_password = (EditText) findViewById(R.id.edittext_login_passward);
        btn_find_password = (Button) findViewById(R.id.btn_find_passward);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);


        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","");
        if(!user_id.equals("")){
            //로그인 정보가 있으면 자동 로그인
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        btn_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this, "비밀번호찾기", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_find_password, null);
                ad.setView(dialogView);
                EditText et_find_pw_email = dialogView.findViewById(R.id.edittext_find_password_email);
                EditText et_find_pw_certify = dialogView.findViewById(R.id.edittext_find_password_certify);
                btn_find_pw_send = dialogView.findViewById(R.id.btn_find_certification_send);
                btn_find_pw_check = dialogView.findViewById(R.id.btn_find_certificataion_check);
                tv_find_pw_alert = dialogView.findViewById(R.id.textview_find_pw_alert);
                tv_find_pw_limit = dialogView.findViewById(R.id.textview_find_pw_limit);

                btn_find_pw_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inputEmail = et_find_pw_email.getText().toString();
                        tv_find_pw_alert.setText("");

                        if (inputEmail.equals("")){ //비었을때
                            tv_find_pw_alert.setText("이메일을 입력해주세요");
                        } else {
                            if (Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){ //이메일 형식일떄
                                //btn_certify_send.setText("재전송");
                                sendFindMail(inputEmail);
                                //다시 버튼 활성화
                                btn_find_pw_check.setEnabled(true);
                                btn_find_pw_check.setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.round_button));
                                btn_find_pw_check.setTextColor(Color.parseColor("#FFFFFF"));

                            } else { //이메일 형식이 아닐때
                                tv_find_pw_alert.setText("이메일형식(xxx@xxx.com)으로 입력해주세요");
                            }
                        }
                    }
                });

                btn_find_pw_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inputcnum = et_find_pw_certify.getText().toString();

                        if (isRunning == true) { //제한시간내에 입력했을때
                            if(validCheck(inputcnum)){
                                Intent intent = new Intent(dialogView.getContext(), ResetPasswordActivity.class);
                                intent.putExtra("certifyEmail",certifyEmail);
                                startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder ad2 = new AlertDialog.Builder(dialogView.getContext());
                                ad2.setTitle("알림");
                                ad2.setMessage("인증번호가 틀렸습니다\n다시 시도해주세요.");
                                ad2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }

                                });
                                AlertDialog alertDialog2 = ad2.create();
                                alertDialog2.show();
                            }

                        } else { //제한시간이 지난 경우
                            AlertDialog.Builder ad2 = new AlertDialog.Builder(dialogView.getContext());
                            ad2.setTitle("알림");
                            ad2.setMessage("인증시간이 초과되었습니다.\n인증번호를 재전송 해주세요");
                            ad2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }

                            });
                            alertDialog2 = ad2.create();
                            alertDialog2.show();
                            //tv_alert_certify.setText("인증시간이 초과되었습니다.\n인증번호를 재전송 해주세요");
                        }
                    }
                });



//                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//
//                });
                alertDialog = ad.create();
                alertDialog.show();

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = et_login_email.getText().toString();
                String login_password = et_login_password.getText().toString();
                String convert_password = SignUpActivity.getHash(login_password);
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<UserInfo> call = apiInterface.userLogin(login_email,convert_password);
                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if(response.body().isSuccess()){
                            String user_id = response.body().getUser_id();
                            SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putString("user_id",user_id);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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
            public void onClick(View view) { //회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            alertDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendFindMail(String email){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserInfo> call = apiInterface.sendFindMail(email);
        call.enqueue(new Callback<UserInfo>()
        {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    String getted_email = response.body().getEmail();
                    String getted_cnum = response.body().getCnum();
                    if(getted_email != null){
                        Toast.makeText(LoginActivity.this, "안증번호를 전송했습니다", Toast.LENGTH_SHORT).show();
                        btn_find_pw_send.setText("재전송");

                        certifyNum = getted_cnum;
                        certifyEmail = getted_email;

                        i = 30;
                        isRunning = true;
                        tv_find_pw_limit.setTextColor(Color.RED);

                        try { //이전 스레드가 있으면 인터럽트로 제거
                            thread.interrupt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        handler = new Handler();
                        timeRunnable = new TimeRunnable();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(isRunning){
                                    try{
                                        Thread.sleep(1000);
                                        handler.post(timeRunnable);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        break;
                                    }
                                }
                            }
                        });
                        thread.start();
                    } else{
                        tv_find_pw_alert.setText("가입되지 않은 이메일입니다.");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Timerset() {
        String minute = Integer.toString(time_min);
        String second = Integer.toString(time_sec);

        if (time_sec < 10) {
            second = "0" + second;
        }



        tv_find_pw_limit.setText(minute + " : " + second);
    }

    //runnable 설정
    private class TimeRunnable implements Runnable {
        @Override
        public void run() {
            if (i > 0){
                i -= 1;
                time_sec = i%60;
                time_min = (i/60)%60;
                Timerset();
            }
            else {
                isRunning = false;
                tv_find_pw_limit.setText("시간 만료");

            }

        }
    }



    private boolean validCheck(String inputcnum){
        if (inputcnum.equals(certifyNum)){ //인증번호 맞을때
            isRunning = false;
            try {
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }

            tv_find_pw_limit.setText("");

            return true;

        } else { //인증번호 틀릴때
            return false;
        }
    }

}