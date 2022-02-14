package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private ArrayList<LiveData> liveArray;

    private String user_id;

    private Context context;

    private HashSet<CalendarDay> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_create1);

        calendar = (MaterialCalendarView) findViewById(R.id.cv_c1_live);
        tv_date = (TextView) findViewById(R.id.tv_c1_live_date);
        tv_count = (TextView) findViewById(R.id.tv_c1_live_count);
        tv_btn_detail = (TextView) findViewById(R.id.tv_c1_live_btn_detail);
        btn_next = (Button) findViewById(R.id.btn_c1_live_next);

        tv_btn_detail.setVisibility(View.INVISIBLE);

        context = this;
        liveArray= new ArrayList<>();

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd", Locale.KOREA);
//        String today = sdf.format(date);
//
//        tv_date.setText(today);

        //오늘 날짜로 레트로핏으로 몇개의 나의 수업이 있는지 체크 해서 tv_count.set(갯수)해주기
        //위쪽에서 레트로핏해서 수업있는 날짜들 전부 넣어서 점찍어주기
        dates = new HashSet<>();
        //라이브 있는 요일 가져와서 넣고 점찍기
        //커스터마이징까지 해줌
        getEvent(user_id);

        calendar.setSelectedDate(CalendarDay.today());


        int y = CalendarDay.today().getYear();
        int m = CalendarDay.today().getMonth();
        int d = CalendarDay.today().getDay();

        String today = y + "." +  (m+1) + "." + d;

        tv_date.setText(today);
        getLive(today,user_id);

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
                getLive(select_day,user_id);
            }
        });

        tv_btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog(liveArray);
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
                finish();

            }
        });


    }

    private void getLive(String put_date, String put_id){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveDataArray> call = apiInterface.getMyliveList(put_date,put_id);
        call.enqueue(new Callback<LiveDataArray>() {
            @Override
            public void onResponse(Call<LiveDataArray> call, Response<LiveDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveArray = response.body().getLiveDataArray();
                    if(liveArray.size() == 0){
                        tv_count.setText("없음");
                    } else {
                        tv_count.setText(liveArray.size() + "개");
                    }
                    if(!tv_count.getText().toString().equals("없음")){
                        tv_btn_detail.setVisibility(View.VISIBLE);
                    } else{
                        tv_btn_detail.setVisibility(View.INVISIBLE);
                    }

//                    liveAdapter.setArrayList(liveArray);
//                    liveAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<LiveDataArray> call, Throwable t) {
                Toast.makeText(LiveCreateActivity_1.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeDialog(ArrayList<LiveData> array){
        AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_1.this);
        LayoutInflater inflater = (LayoutInflater) LiveCreateActivity_1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_show_live, null);

        RecyclerView recyclerView;
        LiveSubAdapter liveSubAdapter;
        LinearLayoutManager linearLayoutManager;

        recyclerView = dialogView.findViewById(R.id.rv_show_live);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        liveSubAdapter = new LiveSubAdapter(array, context);
        recyclerView.setAdapter(liveSubAdapter);

        ad.setView(dialogView);
        ad.setTitle("내 라이브 일정");
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }

    private void getEvent(String put_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveDataArray> call = apiInterface.getMyliveDateList(put_id);
        call.enqueue(new Callback<LiveDataArray>() {
            @Override
            public void onResponse(Call<LiveDataArray> call, Response<LiveDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    String[][] eventArray = response.body().getEventArray();
                    for (int i = 0; i < eventArray.length; i++){
                        int get_year = Integer.parseInt(eventArray[i][0]);
                        int get_month = Integer.parseInt(eventArray[i][1])-1;
                        int get_day = Integer.parseInt(eventArray[i][2]);
                        dates.add(CalendarDay.from(get_year,get_month,get_day));
                    }
                    calendar.addDecorators(
                            new DecoratorSaturday(),
                            new DecoratorSunday(),
                            new DecoratorSelector(LiveCreateActivity_1.this),
                            new DecoratorToday(),
                            new DecoratorEvent(Color.RED, dates)
                    );

                }
            }

            @Override
            public void onFailure(Call<LiveDataArray> call, Throwable t) {
                Toast.makeText(LiveCreateActivity_1.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });

//        String[][] eventArray = call.execute().body().getEventArray();
//        for (int i = 0; i < eventArray.length; i++){
//            int get_year = Integer.parseInt(eventArray[i][0]);
//            int get_month = Integer.parseInt(eventArray[i][1]);
//            int get_day = Integer.parseInt(eventArray[i][2]);
//            dates.add(CalendarDay.from(get_year,get_month,get_day));
//        }
    }


}