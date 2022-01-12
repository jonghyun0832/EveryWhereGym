package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    //스레드 타이머용
    private int time_sec = 0;
    private int time_min = 0;
    private int i;
    private boolean isRunning;

    Handler handler;
    TimeRunnable timeRunnable;
    Thread thread;

    //회원가입 조건
    private boolean passCertify = false; //인증
    private boolean passPassword = false; //비밀번호
    private boolean passRePassword = false; //비밀번호 확인
    private boolean passName = false; //이름 중복확인

    //전역
    private String certifyNum = "";

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

    ImageView img_certify_check;
    ImageView img_password_check;
    ImageView img_repassword_check;
    ImageView img_nickname_check;

    TextView tv_alert_email;
    TextView tv_alert_certify;
    TextView tv_alert_password;
    TextView tv_alert_repassword;
    TextView tv_alert_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


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

        img_certify_check = (ImageView) findViewById(R.id.imageview_certify_check);
        img_password_check = (ImageView) findViewById(R.id.imageview_password_check);
        img_repassword_check = (ImageView) findViewById(R.id.imageview_repassword_check);
        img_nickname_check = (ImageView) findViewById(R.id.imageview_nickname_check);

        tv_alert_email = (TextView) findViewById(R.id.textview_alert_email);
        tv_alert_certify = (TextView) findViewById(R.id.textview_alert_certify);
        tv_alert_password = (TextView) findViewById(R.id.textview_alert_password);
        tv_alert_repassword = (TextView) findViewById(R.id.textview_alert_repassword);
        tv_alert_nickname = (TextView) findViewById(R.id.textview_alert_nickname);


                //처음엔 버튼 클릭못하게 하기
        btn_certify_check.setEnabled(false);
        btn_certify_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.round_button_disable));
        btn_certify_check.setTextColor(Color.parseColor("#4D5E5E5E"));




        btn_certify_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = et_signup_email.getText().toString();
                tv_alert_email.setText("");
                if (inputEmail.equals("")){ //비었을때
                    tv_alert_email.setText("이메일을 입력해주세요");
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){ //이메일 형식일떄

                        sendCertifyMail(inputEmail);
                        //다시 버튼 활성화
                        btn_certify_check.setEnabled(true);
                        btn_certify_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.round_button_gray));
                        btn_certify_check.setTextColor(Color.parseColor("#FFFFFF"));

                    } else { //이메일 형식이 아닐때
                        tv_alert_email.setText("이메일형식(xxx@xxx.com)으로 입력해주세요");
                    }
                }
            }
        });

        btn_certify_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputcnum = et_signup_certify.getText().toString();

                if (inputcnum.equals("")) { //입력칸이 비었을때
                    tv_alert_certify.setText("인증번호를 입력해주세요");

                } else { //인증번호 입력 됬을때
                    if (isRunning == true) { //제한시간내에 입력했을때
                        validCheck(inputcnum);

                    } else { //제한시간이 지난 경우
                        tv_alert_certify.setText("인증시간이 초과되었습니다.\n인증번호를 재전송 해주세요");
                    }
                }
            }
        });


        //비밀번호 정규식 확인
        et_signup_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String input_password = et_signup_password.getText().toString();
                    if (!input_password.equals("")){ //내용이 입력되어있을때
                        checkPassword(input_password);
                    }
                }
            }
        });

        //비밀번호 재확인
        et_signup_repassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String input_password = et_signup_password.getText().toString();
                    String input_repassword = et_signup_repassword.getText().toString();
                    if(!input_repassword.equals("")){ //빈칸이 아닐때
                        checkPasswordCorrect(input_password,input_repassword);
                    }
                }
            }
        });


        et_signup_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String inputNickname = et_signup_name.getText().toString();
                if(!hasFocus){
                    if (!inputNickname.equals("")){
                        checkDulicate(inputNickname);
                    }
                }
            }
        });

        //닉네임 중복확인
        btn_duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_alert_nickname.setText(""); //알림 메세지 초기화
                tv_alert_nickname.setTextColor(Color.RED); //알림 메세지 색 초기화, 체크박스 초기화
                img_nickname_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle_disable));
                passName = false; //회원가입 조건 설정 (다시 돌아올수있으니 초기화할떄 같이 false로 초기화)

                String inputNickname = et_signup_name.getText().toString();
                if (inputNickname.equals("")){ //아무것도 입력안함
                    tv_alert_nickname.setText("닉네임을 입력해주세요");
                } else {
                    checkDulicate(inputNickname);
                }
            }
        });


        btn_more_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
                ad.setMessage(R.string.more_service);
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();


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
                //메일인증, 비밀번호 정규식, 비밀번호 일치, 닉네임 중복확인, 이용약관 동의, 정보 동의 6가지 체크
                if (passCertify && passPassword && passRePassword && passName){
                    if (cb_service.isChecked() || cb_data.isChecked()){
                        String save_email = et_signup_email.getText().toString();
                        String save_password = et_signup_password.getText().toString();
                        String save_nickname = et_signup_name.getText().toString();
                        saveSignUpInfo(save_email,save_password,save_nickname);
                    } else {
                        AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
                        ad.setMessage("이용약관과 개인정보 수집 및 이용동의에 대한 안내 모두 동의해주세요");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = ad.create();
                        alertDialog.show();
                    }
                } else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
                    ad.setMessage("조건에 맞게 회원가입 정보를 모두 입력해주세요.");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = ad.create();
                    alertDialog.show();
                }
            }
        });



    }


    //인증메일 보내기
    private void sendCertifyMail(String email) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserInfo> call = apiInterface.sendMail(email);
        call.enqueue(new Callback<UserInfo>()
        {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    String getted_email = response.body().getEmail();
                    String getted_cnum = response.body().getCnum();
                    if(getted_email == null){
                        tv_alert_email.setText("이미 가입되어있는 이메일입니다");
                        //Toast.makeText(SignUpActivity.this, "이미 가입되어있는 이메일입니다", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(SignUpActivity.this, "인증번호를 보냈습니다", Toast.LENGTH_SHORT).show();
                        btn_certify_send.setText("재전송");
                        //Toast.makeText(SignUpActivity.this, "서버에서 이메일 : " + getted_email + "숫자 : " + getted_cnum, Toast.LENGTH_SHORT).show();
                        certifyNum = getted_cnum;

                        i = 15;
                        isRunning = true;
                        tv_limit.setTextColor(Color.RED);

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

                    }
                    Log.e("getNameHobby()", "서버에서 이메일 : " + getted_email + "숫자 : " + getted_cnum);


                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //인증번호 확인
    private void validCheck(String inputcnum){
        if (inputcnum.equals(certifyNum)){ //인증번호 맞을때
            isRunning = false;
            try {
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }

            passCertify = true;

            //인증 완료 알림띄우기
            AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
            img_certify_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle));
            ad.setTitle("알림");
            ad.setMessage("인증되었습니다");
            et_signup_certify.setEnabled(false);
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //그냥 알림창이여서 넣을게 없음
                }
            });
            tv_limit.setText("");
            tv_alert_certify.setVisibility(View.INVISIBLE);
            AlertDialog alertDialog = ad.create();
            alertDialog.show();




        } else { //인증번호 틀릴때
            AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
            ad.setTitle("알림");
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //그냥 알림창이여서 넣을게 없음
                }
            });
            ad.setMessage("인증번호가 틀렸습니다\n다시 시도해주세요.");
            AlertDialog alertDialog = ad.create();
            alertDialog.show();
        }
    }


    //텍스트 변경
    private void Timerset() {
        String minute = Integer.toString(time_min);
        String second = Integer.toString(time_sec);

//        if (time_min < 10) {
//            minute = "0" + minute;
//        }
        if (time_sec < 10) {
            second = "0" + second;
        }

        tv_limit.setText(minute + " : " + second);
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
                tv_limit.setText("시간 만료");

            }

        }
    }

    private void checkPassword(String password){
        String val_symbol = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        if (Pattern.matches(val_symbol,password)){
            tv_alert_password.setText("사용 가능한 비밀번호입니다.");
            tv_alert_password.setTextColor(Color.BLUE);
            passPassword = true;
            img_password_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle));
            //올바른번호
        } else {
            //올바르지 않은 번호
            tv_alert_password.setTextColor(Color.RED);
            tv_alert_password.setText("영문+숫자포함 최소 6자리 이상 입력해주세요");
            passPassword = false;
            img_password_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle_disable));
        }

    }

    private void checkPasswordCorrect(String password, String repassword){
        if(repassword.equals(password)){ //일치할 경우
            tv_alert_repassword.setText("비밀번호가 일치합니다");
            tv_alert_repassword.setTextColor(Color.BLUE);
            passRePassword = true;
            img_repassword_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle));
        } else { //불일치할 경우
            tv_alert_repassword.setText("비밀번호가 불일치 합니다");
            tv_alert_repassword.setTextColor(Color.RED);
            passRePassword = false;
            img_repassword_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle_disable));
        }
    }


    private void checkDulicate(String nickname){
        String val_symbol = "^[가-힣ㄱ-ㅎa-zA-Z0-9]{1,12}$";
        if (Pattern.matches(val_symbol,nickname)){ //정규식 만족할 경우
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<UserInfo> call = apiInterface.sendNickName(nickname);
            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if (response.isSuccessful() && response.body() != null)
                    {
                        boolean getted_nickname = response.body().isNameDuplicate();
                        if(getted_nickname){
                            tv_alert_nickname.setText("이미 존재하는 닉네임입니다");
                            tv_alert_nickname.setTextColor(Color.RED);
                            img_nickname_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle_disable));
                            passName = false;
                        } else{
                            tv_alert_nickname.setText("사용 가능한 닉네임입니다");
                            tv_alert_nickname.setTextColor(Color.BLUE);
                            img_nickname_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle));
                            passName = true;
                        }
                    }else {
                        Toast.makeText(SignUpActivity.this, "뭔가 잘못됬음", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
                }
            });

        } else { //정규식 만족하지 않는 경우
            tv_alert_nickname.setText("공백,특수문자 미포함 12자 이하로 입력해주세요");
            tv_alert_nickname.setTextColor(Color.RED);
            img_nickname_check.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.ic_baseline_check_circle_disable));
            passName = false;
        }


    }

    private void saveSignUpInfo(String email, String password, String nickname){
        String convert_password = encodePassword(password);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserInfo> call = apiInterface.sendUserInfo(email,convert_password,nickname);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    //회원가입 마치고 돌아가기
                    boolean result = response.body().isSuccess();
                    if (result){
                        Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "회원가입 통신 오류", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "통신에 문제있음", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String encodePassword(String password){
        String convert_password = getHash(password);
        //암호화 과정 필요
        return convert_password;
    }

    public static String getHash(String str) {
        String digest = "";
        try{

            //암호화
            MessageDigest sh = MessageDigest.getInstance("SHA-256"); // SHA-256 해시함수를 사용
            sh.update(str.getBytes()); // str의 문자열을 해싱하여 sh에 저장
            byte byteData[] = sh.digest(); // sh 객체의 다이제스트를 얻는다.

            //얻은 결과를 string으로 변환
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            digest = sb.toString();
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace(); digest = null;
        }
        return digest; // 결과  return
    }



}