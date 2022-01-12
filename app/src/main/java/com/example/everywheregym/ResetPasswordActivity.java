package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.ZoneId;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btn_reset_complete;
    EditText et_reset_password;
    EditText et_reset_repassword;
    TextView tv_password_alert;
    TextView tv_repassword_alert;

    String getted_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btn_reset_complete = (Button) findViewById(R.id.button_reset);
        et_reset_password = (EditText) findViewById(R.id.edittext_reset_password);
        et_reset_repassword = (EditText) findViewById(R.id.edittext_reset_repassword);
        tv_password_alert = (TextView) findViewById(R.id.textview_reset_password_alert);
        tv_repassword_alert = (TextView) findViewById(R.id.textview_reset_repassword_alert);

        btn_reset_complete.setEnabled(false);

        Intent intent = getIntent();
        getted_email = intent.getStringExtra("certifyEmail");

        //비밀번호 정규식 확인
        et_reset_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String input_password = et_reset_password.getText().toString();
                    if (!input_password.equals("")){ //내용이 입력되어있을때
                        checkPassword(input_password);
                    }
                }
            }
        });


        et_reset_repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input_password = et_reset_password.getText().toString();
                String input_repassword = et_reset_repassword.getText().toString();
                if(!input_repassword.equals("")){ //빈칸이 아닐때
                    checkPasswordCorrect(input_password,input_repassword);
                }
            }
        });


        btn_reset_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_password = et_reset_repassword.getText().toString();
                String convert_password = SignUpActivity.getHash(input_password);
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<UserInfo> call = apiInterface.resetPassword(getted_email,convert_password);
                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                AlertDialog.Builder ad = new AlertDialog.Builder(ResetPasswordActivity.this);
                                ad.setTitle("알림");
                                ad.setMessage("비밀번호가 재설정 되었습니다");
                                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                });
                                AlertDialog alertDialog = ad.create();
                                alertDialog.show();
                                //정상적으로 된거임
                            } else{
                                //sql오류 비정상적인 접근이라고 출력하면될듯?
                                Toast.makeText(ResetPasswordActivity.this, "비정상적인 접근입니다\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            //돌아온 데이터가 없음
                            Toast.makeText(ResetPasswordActivity.this, "통신결과가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Toast.makeText(ResetPasswordActivity.this, "통신오류 발생", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }



    private void checkPassword(String password){
        String val_symbol = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        if (Pattern.matches(val_symbol,password)){
            tv_password_alert.setText("사용 가능한 비밀번호입니다.");
            tv_password_alert.setTextColor(Color.BLUE);
            //올바른번호
        } else {
            //올바르지 않은 번호
            tv_password_alert.setTextColor(Color.RED);
            tv_password_alert.setText("영문+숫자포함 최소 6자리 이상 입력해주세요");
        }

    }

    private void checkPasswordCorrect(String password, String repassword){
        if(repassword.equals(password)){ //일치할 경우
            tv_repassword_alert.setText("비밀번호가 일치합니다");
            tv_repassword_alert.setTextColor(Color.BLUE);
            btn_reset_complete.setEnabled(true);
            btn_reset_complete.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this,R.drawable.round_button));
            btn_reset_complete.setTextColor(Color.parseColor("#FFFFFF"));
        } else { //불일치할 경우
            tv_repassword_alert.setText("비밀번호가 불일치 합니다");
            tv_repassword_alert.setTextColor(Color.RED);
            btn_reset_complete.setEnabled(false);
            btn_reset_complete.setBackground(ContextCompat.getDrawable(ResetPasswordActivity.this,R.drawable.round_button_disable));
            btn_reset_complete.setTextColor(Color.parseColor("#4D5E5E5E"));
        }
    }
}