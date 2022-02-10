package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LiveCreateActivity_1 extends AppCompatActivity {

    private CalendarView calendar;
    private TextView tv_date;
    private TextView tv_count;
    private TextView tv_btn_detail;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_create1);

        calendar = (CalendarView) findViewById(R.id.cv_c1_live);
        tv_date = (TextView) findViewById(R.id.tv_c1_live_date);
        tv_count = (TextView) findViewById(R.id.tv_c1_live_count);
        tv_btn_detail = (TextView) findViewById(R.id.tv_c1_live_btn_detail);
        btn_next = (Button) findViewById(R.id.btn_c1_live_next);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        String today = sdf.format(date);

        tv_date.setText(today);

        calendar.setMinDate(System.currentTimeMillis());
        calendar.setMaxDate(System.currentTimeMillis()+(24L *60*60*1000*28));

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String select_date = year + "." + month + "." + day;
                tv_date.setText(select_date);
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected_date = tv_date.getText().toString();
//                SharedPreferences sharedPreferences= getSharedPreferences("live", MODE_PRIVATE);
//                SharedPreferences.Editor editor= sharedPreferences.edit();
//                editor.putString("select_date",selected_date);
//                editor.commit();

                Intent intent = new Intent(LiveCreateActivity_1.this, LiveCreateActivity_2.class);
                intent.putExtra("selected_date",selected_date);
                startActivity(intent);

            }
        });


    }
}