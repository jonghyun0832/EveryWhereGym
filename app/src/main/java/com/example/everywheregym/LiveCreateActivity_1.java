package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class LiveCreateActivity_1 extends AppCompatActivity {

    private MaterialCalendarView calendar;
    //private CalendarView calendar;
    private TextView tv_date;
    private TextView tv_count;
    private TextView tv_btn_detail;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_create1);

        calendar = (MaterialCalendarView) findViewById(R.id.cv_c1_live);
        tv_date = (TextView) findViewById(R.id.tv_c1_live_date);
        tv_count = (TextView) findViewById(R.id.tv_c1_live_count);
        tv_btn_detail = (TextView) findViewById(R.id.tv_c1_live_btn_detail);
        btn_next = (Button) findViewById(R.id.btn_c1_live_next);

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd", Locale.KOREA);
//        String today = sdf.format(date);
//
//        tv_date.setText(today);

        //오늘 날짜로 레트로핏으로 몇개의 나의 수업이 있는지 체크 해서 tv_count.set(갯수)해주기
        if(tv_count.getText().toString().equals("없음")){
            tv_btn_detail.setVisibility(View.INVISIBLE);
        }

        calendar.setSelectedDate(CalendarDay.today());
        //위쪽에서 레트로핏해서 수업있는 날짜들 전부 넣어서 점찍어주기
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.today());


        //커스터마이징
        calendar.addDecorators(
                new DecoratorSaturday(),
                new DecoratorSunday(),
                new DecoratorSelector(this),
                new DecoratorToday(),
                new DecoratorEvent(Color.RED, dates)
        );

        int y = CalendarDay.today().getYear();
        int m = CalendarDay.today().getMonth();
        int d = CalendarDay.today().getDay();

        String today = y + "." +  (m+1) + "." + d;

        tv_date.setText(today);

        calendar.state().edit()
                .setMinimumDate(CalendarDay.from(y,m,d))
                .setMaximumDate(CalendarDay.from(y,(m+1),d))
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendar.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calendar.setDynamicHeightEnabled(true);
        calendar.setPadding(0,-20,0,30);

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year = widget.getSelectedDate().getYear();
                int month = widget.getSelectedDate().getMonth();
                int day = widget.getSelectedDate().getDay();

                String select_day = year + "." +  (month+1) + "." + day;
                tv_date.setText(select_day);
            }
        });

//        calendar.setMinDate(System.currentTimeMillis());
//        calendar.setMaxDate(System.currentTimeMillis()+(24L *60*60*1000*28));

//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
//                month = month + 1;
//                String select_date = year + "." + month + "." + day;
//                tv_date.setText(select_date);
//            }
//        });


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
                finish();

            }
        });


    }
}