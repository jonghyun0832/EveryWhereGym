package com.example.everywheregym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveCreateActivity_2 extends AppCompatActivity {

    private TextView tv_date;
    private TextView tv_btn_detail;

    private EditText et_title;
    private TextView tv_title_limit;

    private TextView tv_start_time;
    private TextView tv_spend_time;
    private TextView tv_calorie;
    private TextView tv_join_limit;
    private TextView tv_material;

    private Button btn_save;

    private String getted_date;

    private int starthour = 12;
    private int startminute = 0;
    private int spend_hour = 0;
    private int spend_minute = 0;
    private int spend_time;

    private String get_calorie;
    private String get_material;

    private int limit_num = 1;

    private String user_id;

    private ProgressDialog prDialog;

    private boolean isToday;
    private boolean isOkay = true;

    private Context context;

    private ArrayList<LiveData> liveArray;

    //수정시 받아오는 데이터
    private String li_id;
    private int edit_score;

    //타임피커 리스너
    TimePickerDialog.OnTimeSetListener startlistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            starthour = hour;
            startminute = minute;


            if (minute < 10){
                tv_start_time.setText(hour + " : 0" +minute);
            } else{
                tv_start_time.setText(hour + " : " + minute);
            }

        }
    };

//    private TimePickerDialog.OnTimeSetListener spendlistener = new TimePickerDialog.OnTimeSetListener() {
//        @Override
//        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//            spend_hour = hour;
//            spend_minute = minute;
//            spend_time = hour * 60 + minute;
//
//            tv_spend_time.setText(spend_time + "분");
//        }
//    };

    DatePickerDialog.OnDateSetListener md = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int hour, int minute, int dayOfMonth){
            spend_hour = hour;
            spend_minute = minute;
            spend_time = hour * 60 + minute;
            tv_spend_time.setText(spend_time + "분");
        }
    };

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int num, int monthOfYear, int dayOfMonth){
            limit_num = num;
            tv_join_limit.setText(num + "명");
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
        tv_spend_time = (TextView) findViewById(R.id.tv_c2_live_spend_time);
        tv_calorie = (TextView) findViewById(R.id.tv_c2_live_calorie);
        tv_join_limit = (TextView) findViewById(R.id.tv_c2_live_join_limit);
        tv_material = (TextView) findViewById(R.id.tv_c2_live_material);

        btn_save = (Button) findViewById(R.id.btn_c2_live_save);

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");

        context = this;
        liveArray= new ArrayList<>();

        initDialog();


        Intent get_intent = getIntent();
        boolean isEdit = get_intent.getBooleanExtra("isEdit",false);
        if(isEdit){
            getted_date = get_intent.getStringExtra("li_date");
            li_id = get_intent.getStringExtra("li_id");
            String li_title = get_intent.getStringExtra("li_title");
            String li_start_hour = get_intent.getStringExtra("li_start_hour");
            String li_start_minute = get_intent.getStringExtra("li_start_minute");
            String li_spend_time = get_intent.getStringExtra("li_spend_time");
            String li_calorie = get_intent.getStringExtra("li_calorie");
            String li_limit_join = get_intent.getStringExtra("li_limit_join");
            String li_material = get_intent.getStringExtra("li_material");
            edit_score = get_intent.getIntExtra("edit_score",0);

            et_title.setText(li_title);

            starthour = Integer.parseInt(li_start_hour);
            startminute = Integer.parseInt(li_start_minute);
            if (startminute < 10){
                tv_start_time.setText(starthour + " : 0" +startminute);
            } else{
                tv_start_time.setText(starthour + " : " + startminute);
            }

            spend_time = Integer.parseInt(li_spend_time);
            spend_hour = spend_time/60;
            spend_minute = spend_time&60;
            tv_spend_time.setText(spend_time + "분");

            get_calorie = li_calorie;
            tv_calorie.setText(get_calorie + "Kcal");

            limit_num = Integer.parseInt(li_limit_join);
            tv_join_limit.setText(limit_num + "명");

            get_material = li_material;
            tv_material.setText(get_material);

            btn_save.setText("라이브 일정 수정하기");

        }else{
            getted_date = get_intent.getStringExtra("selected_date");
        }
        tv_date.setText(getted_date);

        //선택한 일에 해당하는 라이브 arraylist 불러오기
        getAllLive(getted_date);

        tv_btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog(liveArray);
            }
        });

        //선택일자와 오늘이 같은 날인지 확인 (시작시간 제한용)
        isToday = checkTime();

//        if(year == Integer.parseInt(split[0]) && min == Integer.parseInt(split[1]) && day == Integer.parseInt(split[2])){
//            //현재시간보다 빠르면 안됨
//            isToday = true;
//        } else {
//            isToday = false;
//        }


        tv_title_limit.setText(et_title.getText().toString().length() + " / 30");
        //tv_title_limit.setText("0 / 30");

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

        tv_spend_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickerDialogMinute pdm = new PickerDialogMinute(spend_hour,spend_minute);
                pdm.setListener(md);
                pdm.show(getSupportFragmentManager(),"test");
            }
        });

        tv_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
                ad.setTitle("소모 칼로리 입력");
                final EditText et_tmp = new EditText(LiveCreateActivity_2.this);
                et_tmp.setText(get_calorie);
                ad.setMessage("예상되는 소모 칼로리를 입력해주세요 (Kcal)");
                ad.setView(et_tmp);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        get_calorie = et_tmp.getText().toString();
                        tv_calorie.setText(get_calorie + " Kcal");
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


        tv_join_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickerDialogNumber npd = new PickerDialogNumber(limit_num);
                npd.setListener(d);
                npd.show(getSupportFragmentManager(),"test");
            }
        });




        tv_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
                LayoutInflater inflater = (LayoutInflater) LiveCreateActivity_2.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_material, null);
                ad.setView(dialogView);
                ad.setTitle("운동 준비물 추가하기");

                EditText et_material = dialogView.findViewById(R.id.et_dialog_vod_material);

                et_material.setText(get_material);

                ad.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        get_material = et_material.getText().toString();

                        tv_material.setText(get_material);

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


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isToday){
                    isOkay = checkTimeLimit();
                }
                if(isOkay){
                    if(tv_date.getText().toString().equals("") ||
                            tv_spend_time.getText().toString().equals("") ||
                            tv_calorie.getText().toString().equals("") ||
                            tv_join_limit.getText().toString().equals("") ||
                            et_title.getText().toString().equals("")
                    ){
                        AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
                        ad.setTitle("알림");
                        ad.setMessage("세부 필수 정보들을 모두 입력해주세요\n(필수항목 : 제목,시작시간,소요시간,소모칼로리,제한인원)");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }

                        });
                        AlertDialog alertDialog = ad.create();
                        alertDialog.show();
                    }else {
                        showpDialog();

                        HashMap<String, RequestBody> map = new HashMap<>();

                        if (get_material == null) {
                            get_material = "";
                        }

//                    String pdname = user_id + "_" + Calendar.getInstance().getTimeInMillis();
//                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), pdname);
                        RequestBody live_date = RequestBody.create(MediaType.parse("text/plain"), tv_date.getText().toString());
                        RequestBody live_title = RequestBody.create(MediaType.parse("text/plain"), et_title.getText().toString());
                        RequestBody live_start_hour = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(starthour));
                        RequestBody live_start_minute = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(startminute));
                        RequestBody live_spend_time = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(spend_time));
                        RequestBody live_calorie = RequestBody.create(MediaType.parse("text/plain"), get_calorie);
                        RequestBody live_limit_join = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(limit_num));
                        RequestBody live_material = RequestBody.create(MediaType.parse("text/plain"), get_material);
                        RequestBody live_user_id = RequestBody.create(MediaType.parse("text/plain"), user_id);

                        map.put("date", live_date);
                        map.put("title", live_title);
                        map.put("start_hour", live_start_hour);
                        map.put("start_minute", live_start_minute);
                        map.put("spend_time", live_spend_time);
                        map.put("calorie", live_calorie);
                        map.put("limit_join", live_limit_join);
                        map.put("material", live_material);
                        map.put("user_id", live_user_id);


                        if(!isEdit){ //새로 추가할때
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<LiveData> call = apiInterface.uploadLiveData(map);
                            call.enqueue(new Callback<LiveData>() {
                                @Override
                                public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().isSuccess()) {
                                            hidepDialog();

                                            AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
                                            ad.setTitle("알림");
                                            ad.setMessage("라이브 일정 업로드를 완료되었습니다!");
                                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();

                                        } else {
                                            hidepDialog();
                                            Toast.makeText(LiveCreateActivity_2.this, "서버단쪽 sql에 문제생김", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        hidepDialog();
                                        Toast.makeText(LiveCreateActivity_2.this, " 업로드에 문제생김", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<LiveData> call, Throwable t) {
                                    hidepDialog();
                                    Toast.makeText(LiveCreateActivity_2.this, "라이브 업로드 실패", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else { //수정일떄
                            AlertDialog.Builder ad4 = new AlertDialog.Builder(LiveCreateActivity_2.this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView2 = inflater.inflate(R.layout.dialog_notify_edit_message, null);
                            ad4.setView(dialogView2);
                            ad4.setTitle("수정 사유를 작성해주세요");

                            EditText et_message = dialogView2.findViewById(R.id.et_dialog_notify_edit_message);

                            ad4.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String get_message = et_message.getText().toString();

                                    RequestBody live_id = RequestBody.create(MediaType.parse("text/plain"), li_id);
                                    RequestBody live_message = RequestBody.create(MediaType.parse("text/plain"), get_message);
                                    RequestBody live_score = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(edit_score));

                                    map.put("live_id",live_id);
                                    map.put("message",live_message);
                                    map.put("score",live_score);

                                    sendEditInfo(map);

                                }
                            });

                            ad4.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            AlertDialog alertDialog4 = ad4.create();
                            alertDialog4.show();
                        }

                    }
                } else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
                    ad.setTitle("알림");
                    ad.setMessage("설정 시간이 현재 시간보다 앞에 있습니다\n다시 설정해주세요");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tv_start_time.setText(null);
                        }
                    });
                    AlertDialog alertDialog = ad.create();
                    alertDialog.show();
                }


            }
        });





    }

    protected void initDialog(){
        prDialog = new ProgressDialog(this);
        prDialog.setMessage("loading");
        prDialog.setCancelable(true);
    }

    protected void showpDialog(){
        if(!prDialog.isShowing()){
            prDialog.show();
        }
    }

    protected void hidepDialog(){
        if(prDialog.isShowing()){
            prDialog.dismiss();
        }
    }

    public void startTimePicker(){
        TimePickerDialog dialog = new TimePickerDialog(LiveCreateActivity_2.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,startlistener,starthour,startminute,true);
        dialog.setTitle("시작시간");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private boolean checkTime(){
        int y = CalendarDay.today().getYear();
        int m = CalendarDay.today().getMonth() + 1;
        int d = CalendarDay.today().getDay();

        String[] split = getted_date.split("\\.");

        if(y == Integer.parseInt(split[0]) && m == Integer.parseInt(split[1]) && d == Integer.parseInt(split[2])){
            //현재시간보다 빠르면 안됨
            return true;
        } else {
            return false;
        }
    }

    private boolean checkTimeLimit(){
        //현재시간보다 빠르면 안됨
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("mm");

            int nowHour = Integer.parseInt(now.format(formatter));
            int nowMinute = Integer.parseInt(now.format(formatter2));

            if(starthour < nowHour){
                return false;
            } else if(starthour == nowHour && startminute < nowMinute){
                return false;
            } else {
                //통과
                return true;
            }
        } else {
            return false;
        }

        //        long now =System.currentTimeMillis();
//        Date mDate = new Date(now);
//        SimpleDateFormat hourData = new SimpleDateFormat("HH");
//        SimpleDateFormat minuteData = new SimpleDateFormat("mm");
//        //시
//        int nowHour = Integer.parseInt(hourData.format(mDate));
//        //분
//        int nowMinute = Integer.parseInt(minuteData.format(mDate));
//        if(starthour < nowHour){
//            return false;
//        } else if(starthour == nowHour && startminute < nowMinute){
//            return false;
//        } else {
//            //통과
//            return true;
//        }

    }

    private void getAllLive(String put_date){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveDataArray> call = apiInterface.getliveList(put_date);
        call.enqueue(new Callback<LiveDataArray>() {
            @Override
            public void onResponse(Call<LiveDataArray> call, Response<LiveDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveArray = response.body().getLiveDataArray();
//                    liveAdapter.setArrayList(liveArray);
//                    liveAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<LiveDataArray> call, Throwable t) {
                Toast.makeText(LiveCreateActivity_2.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeDialog(ArrayList<LiveData> array){
        AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
        LayoutInflater inflater = (LayoutInflater) LiveCreateActivity_2.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ad.setTitle("전체 라이브 일정");
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }

    private void sendEditInfo(HashMap<String, RequestBody> map){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call = apiInterface.sendEditAlarm(map);
        call.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        hidepDialog();

                        AlertDialog.Builder ad = new AlertDialog.Builder(LiveCreateActivity_2.this);
                        ad.setTitle("알림");
                        ad.setMessage("라이브 일정 수정이 완료되었습니다!");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = ad.create();
                        alertDialog.show();

                    } else {
                        hidepDialog();
                        Toast.makeText(LiveCreateActivity_2.this, "서버단쪽 sql에 문제생김", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hidepDialog();
                    Toast.makeText(LiveCreateActivity_2.this, " 업로드에 문제생김", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LiveData> call, Throwable t) {
                hidepDialog();
                Toast.makeText(LiveCreateActivity_2.this, "라이브 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public void spendTimePicker(){
//        TimePickerDialog dialog = new TimePickerDialog(LiveCreateActivity_2.this,
//                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,spendlistener,spend_hour,spend_minute,true);
//        dialog.setTitle("예상 소요 시간");
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.show();
//    }
}