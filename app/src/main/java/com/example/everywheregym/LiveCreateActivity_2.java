package com.example.everywheregym;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class LiveCreateActivity_2 extends AppCompatActivity {

    private TextView tv_date;
    private TextView tv_btn_detail;

    private EditText et_title;
    private TextView tv_title_limit;

    private TextView tv_start_time;
    private Spinner sp_spend_time;
    private TextView tv_calorie;
    private TextView tv_join_limit;
    private TextView tv_material;

    private Button btn_save;

    private String getted_date;

    private int starthour;
    private int startminute;
    private int endhour;
    private int endminute;

    //타임피커 리스너
    private TimePickerDialog.OnTimeSetListener startlistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            starthour = hour;
            startminute = minute;
//            pickerHour = starthour;
//            pickerMinute = startminute;
            if (minute < 10){
                tv_start_time.setText(hour + " : 0" +minute);
            } else{
                tv_start_time.setText(hour + " : " + minute);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_create2);

        tv_date = (TextView) findViewById(R.id.tv_c2_live_date);
        tv_btn_detail = (TextView) findViewById(R.id.tv_btn_c2_live_detail);

        et_title = (EditText) findViewById(R.id.et_c2_live_title);
        tv_title_limit = (TextView) findViewById(R.id.tv_c2_live_title_limit);

        tv_start_time = (TextView) findViewById(R.id.tv_c2_live_start_time);
        sp_spend_time = (Spinner) findViewById(R.id.sp_c2_live_spend_time);
        tv_calorie = (TextView) findViewById(R.id.tv_c2_live_calorie);
        tv_join_limit = (TextView) findViewById(R.id.tv_c2_live_join_limit);
        tv_material = (TextView) findViewById(R.id.tv_c2_live_material);

        btn_save = (Button) findViewById(R.id.btn_c2_live_save);


        tv_btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭시 리사이클러뷰로 오늘 날짜의 수업들 전부 보여주기
            }
        });

        Intent get_intent = getIntent();
        getted_date = get_intent.getStringExtra("selected_date");

        tv_date.setText(getted_date);


        //tv_title_limit.setText(et_title.getText().toString().length() + " / 30");
        tv_title_limit.setText("0 / 30");

        //공통사용가능
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = et_title.getText().toString();
                if (input.length() < 30){
                    tv_title_limit.setText(input.length() + " / 30");
                    tv_title_limit.setTextColor(Color.BLACK);
                } else {
                    tv_title_limit.setText("30자 이하로 입력해주세요");
                    tv_title_limit.setTextColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePicker();
            }
        });





    }



    public void startTimePicker(){
        TimePickerDialog dialog = new TimePickerDialog(LiveCreateActivity_2.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,startlistener,0,0,true);
        dialog.setTitle("시작시간");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}