package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.content.DialogInterface;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragLive extends Fragment {

    private MaterialCalendarView calendar;

    private RecyclerView recyclerView;
    private ArrayList<LiveData> liveArray;
    private LiveAdapter liveAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id;
    private String is_trainer;

    private String selected_date;

    private NestedScrollView nsv;
    private int page = 1, limit = 3;
    private String cursor = "0";
    private boolean isEnd = false;
    private ProgressBar pbBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
        cursor = "0";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_live,container,false);

        ted();

        calendar = (MaterialCalendarView) view.findViewById(R.id.live_calendar);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_live);

        nsv = (NestedScrollView) view.findViewById(R.id.scrollview_live_main);
        pbBar = (ProgressBar) view.findViewById(R.id.progressBar_live_main);

        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!nsv.canScrollVertically(1)){
                    Log.d("왜안됨", "더는아래로 못가요 ");
                    page++;
                    callList(selected_date);
                }
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("fcm", "onSuccess: " + s);

            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        liveArray= new ArrayList<>();
        liveAdapter = new LiveAdapter(liveArray, getActivity());
        recyclerView.setAdapter(liveAdapter);

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");
        is_trainer = sharedPreferences.getString("is_trainer","0");

        SimpleDateFormat format = new SimpleDateFormat("yy . M");

        calendar.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendar.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        calendar.setTitleFormatter(new DateFormatTitleFormatter(format));

        calendar.setLeftArrowMask(null);
        calendar.setRightArrowMask(null);

        int y = CalendarDay.today().getYear();
        int m = CalendarDay.today().getMonth();
        int d = CalendarDay.today().getDay();
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int first_day = todaysDay(dayOfWeek);

        if(d < 10){
            selected_date = y + "." +  (m+1) + ".0" + d;
        } else {
            selected_date = y + "." +  (m+1) + "." + d;
        }
        //callList(selected_date);

        calendar.state().edit()
                .setMinimumDate(CalendarDay.from(y,m,d))
                .setMaximumDate(CalendarDay.from(y,m+1,d+6))
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setFirstDayOfWeek(first_day)
                .commit();

        calendar.setSelectedDate(CalendarDay.today());
        //위쪽에서 레트로핏해서 수업있는 날짜들 전부 넣어서 점찍어주기
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.today());

        //커스터마이징
        calendar.addDecorators(
                new DecoratorSaturday(),
                new DecoratorSunday(),
                new DecoratorSelector(getActivity())
        );

        calendar.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calendar.setDynamicHeightEnabled(true);
        calendar.setPadding(0,-20,0,30);

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year = widget.getSelectedDate().getYear();
                int month = widget.getSelectedDate().getMonth();
                int day = widget.getSelectedDate().getDay();

                if(day < 10){
                    selected_date = year + "." +  (month+1) + ".0" + day;
                } else {
                    selected_date = year + "." +  (month+1) + "." + day;
                }

                System.out.println(selected_date);

                liveArray= new ArrayList<>();
                page = 1;
                cursor = "0";
                pbBar.setVisibility(View.VISIBLE);

                callList(selected_date);

            }
        });

        liveAdapter.setOnImgClickListener(new VodAdapter.myRecyclerViewImgClickListener() {
            @Override
            public void whenImgClick(int position) {
                //해당 트레이너 프로필로 이동
                Intent intent = new Intent(getContext(),ShowProfileActivity.class);
                intent.putExtra("uploader_id",liveArray.get(position).getUploader_id());
                getContext().startActivity(intent);
            }
        });

        liveAdapter.setOnMoreClickListener(new VodAdapter.myRecyclerViewMoreClickListener() {
            @Override
            public void whenMoreClick(int position) {

                String li_id = liveArray.get(position).getLive_id();
                String li_date = liveArray.get(position).getLive_date();
                String li_title = liveArray.get(position).getLive_title();
                String li_start_hour = liveArray.get(position).getLive_start_hour();
                String li_start_minute = liveArray.get(position).getLive_start_minute();
                String li_spend_time = liveArray.get(position).getLive_spend_time();
                String li_calorie = liveArray.get(position).getLive_calorie();
                String li_limit_join = liveArray.get(position).getLive_limit_join();
                String li_material = liveArray.get(position).getLive_material();
                String tr_score = liveArray.get(position).getTrainer_score();

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("알림");
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_live_more, null);
                ad.setView(dialogView);

                TextView tv_edit;
                TextView tv_delete;

                tv_edit = (TextView) dialogView.findViewById(R.id.tv_more_live_edit);
                tv_delete = (TextView) dialogView.findViewById(R.id.tv_more_live_delete);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();

                //라이브 수정
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        int tmp_score = scoreCalculate(li_date);

                        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                        ad.setTitle("알림");
                        ad.setMessage("라이브 수정 시 평판 패널티를 받습니다 \n(-" + tmp_score + "점)\n현재 내 점수 : " + tr_score + "점");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getContext(), LiveCreateActivity_2.class);
                                intent.putExtra("li_id",li_id);
                                intent.putExtra("li_date",li_date);
                                intent.putExtra("li_title",li_title);
                                intent.putExtra("li_start_hour",li_start_hour);
                                intent.putExtra("li_start_minute",li_start_minute);
                                intent.putExtra("li_spend_time",li_spend_time);
                                intent.putExtra("li_calorie",li_calorie);
                                intent.putExtra("li_limit_join",li_limit_join);
                                intent.putExtra("li_material",li_material);
                                intent.putExtra("edit_score",tmp_score);
                                intent.putExtra("isEdit",true);

                                startActivity(intent);
                            }

                        });
                        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog2 = ad.create();
                        alertDialog2.show();
                    }
                });

                //라이브 삭제
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        AlertDialog.Builder ad2 = new AlertDialog.Builder(getContext());
                        ad2.setTitle("알림");
                        ad2.setMessage("해당 라이브 일정을 삭제하시겠습니까?");
                        ad2.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //삭제 레트로핏

                                AlertDialog.Builder ad4 = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView2 = inflater.inflate(R.layout.dialog_notify_message, null);
                                ad4.setView(dialogView2);
                                ad4.setTitle("삭제 사유 작성");

                                EditText et_message = dialogView2.findViewById(R.id.et_dialog_notify_message);

                                ad4.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String get_message = et_message.getText().toString();

                                        sendNoti(user_id,li_id,get_message,getContext());

                                        final FragmentTransaction ftt = getActivity().getSupportFragmentManager().beginTransaction();
                                        ftt.replace(R.id.main_frame_tr, new FragLive());
                                        ftt.commit();

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
                        });

                        ad2.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog alertDialog2 = ad2.create();
                        alertDialog2.show();


                    }
                });


            }
        });

        liveAdapter.setOnButtonClickListener(new LiveAdapter.myRecyclerViewBtnClickListener() {
            @Override
            public void whenBtnClick(LiveAdapter.LiveViewHolder vh, int position) {
                String li_id = liveArray.get(position).getLive_id();
                String uploader_id = liveArray.get(position).getUploader_id();
                String li_title = liveArray.get(position).getLive_title();
                //시간에 따라 버튼 달라지니까
                //버튼 내용에 따라서 알람등록 / 방송참여 할수있게 해줘야함
                if(vh.btn_push.getText().toString().equals("알림받기")){
                    //알림 테이블에 저장
                    setAlarm(user_id,li_id,uploader_id);
                    vh.btn_push.setText("알림해제");
                    vh.btn_push.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_alarm_off));
                    vh.btn_push.setTextColor(Color.BLACK);
                    //reload(is_trainer);

                } else if (vh.btn_push.getText().toString().equals("알림해제")){
                    //알림 테이블에서 삭제
                    deleteAlarm(user_id,li_id,uploader_id);
                    vh.btn_push.setText("알림받기");
                    vh.btn_push.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.round_text_light_green));
                    vh.btn_push.setTextColor(Color.BLACK);
                    //reload(is_trainer);

                } else if (vh.btn_push.getText().toString().equals("라이브 시작")){
                    ted();

                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                    ad.setMessage("라이브를 시작하시겠습니까?");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //레트로핏 보내서 여기서 open으로 만들어줘야함
                            openLive(li_id,li_title);
                        }
                    });
                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = ad.create();
                    alertDialog.show();
                } else if (vh.btn_push.getText().toString().equals("참여하기")){
                    Intent intent = new Intent(getContext(),LiveWebViewActivity.class);
                    intent.putExtra("room_id",li_id);
                    startActivity(intent);
                }

            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        liveArray= new ArrayList<>();
        page = 1;
        cursor = "0";
        callList(selected_date);

    }

    private void callList(String put_date){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveDataArray> call = apiInterface.getliveList(put_date,page,limit,cursor);
        call.enqueue(new Callback<LiveDataArray>() {
            @Override
            public void onResponse(Call<LiveDataArray> call, Response<LiveDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    isEnd = response.body().isEnd();
                    if(isEnd){
                        pbBar.setVisibility(View.GONE);
                    }
                    cursor = response.body().getCursor();
                    ArrayList<LiveData> liveArray_tmp = response.body().getLiveDataArray();
                    liveArray.addAll(liveArray_tmp);

                    //liveArray = response.body().getLiveDataArray();
                    liveAdapter.setArrayList(liveArray);
                    liveAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<LiveDataArray> call, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int todaysDay(int num){
        int result = 1;
        switch(num){
            case 1:
                result = Calendar.SUNDAY;
                break;
            case 2:
                result = Calendar.MONDAY;
                break;
            case 3:
                result = Calendar.TUESDAY;
                break;
            case 4:
                result = Calendar.WEDNESDAY;
                break;
            case 5:
                result = Calendar.THURSDAY;
                break;
            case 6:
                result = Calendar.FRIDAY;
                break;
            case 7:
                result = Calendar.SATURDAY;
                break;
        }
        return result;
    }

    private void setAlarm(String user, String live_id, String uploader){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call = apiInterface.addLiveAlarm(user,live_id,uploader);
        call.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                if (response.isSuccessful() && response.body() != null){
                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                    ad.setTitle("알림이 설정되었습니다");
                    ad.setMessage("라이브가 시작되면 알림을 보내드리겠습니다");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }

                    });
                    AlertDialog alertDialog = ad.create();
                    alertDialog.show();

                }
            }

            @Override
            public void onFailure(Call<LiveData> call, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAlarm(String user, String live_id, String uploader){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call = apiInterface.deleteLiveAlarm(user,live_id,uploader);
        call.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                if (response.isSuccessful() && response.body() != null){
                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                    ad.setTitle("알림이 해제되었습니다");
                    ad.setMessage("해당 라이브가 시작되도 알림을 받지 않습니다");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }

                    });
                    AlertDialog alertDialog = ad.create();
                    alertDialog.show();

                }
            }

            @Override
            public void onFailure(Call<LiveData> call, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reload(String is_tr){
        if (is_tr.equals("1")){ //트레이너
            final FragmentTransaction ftt = getActivity().getSupportFragmentManager().beginTransaction();
            ftt.replace(R.id.main_frame_tr, new FragLive());
            ftt.commit();
        } else { //회원
            final FragmentTransaction ftt = getActivity().getSupportFragmentManager().beginTransaction();
            ftt.replace(R.id.main_frame, new FragLive());
            ftt.commit();
        }
    }

    private void sendNoti(String trainer_id, String live_id, String message,Context context){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.sendDeleteAlarm(trainer_id,live_id,message);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        AlertDialog.Builder ad3 = new AlertDialog.Builder(context);
                        ad3.setTitle("전송 완료");
                        ad3.setMessage("알림 신청한 회원들에게 삭제 알림을 보냈습니다");
                        ad3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }

                        });
                        AlertDialog alertDialog3 = ad3.create();
                        alertDialog3.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int scoreCalculate(String date){

        Date now = new Date();
        Date format1 = null;
        try {
            format1 = (new SimpleDateFormat("yyyy.M.dd")).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffSec = (format1.getTime() - now.getTime()) / 1000; //초 차이
        //long diffMin = (format1.getTime() - now.getTime()) / 60000; //분 차이
        //long diffHor = (format1.getTime() - now.getTime()) / 3600000; //시 차이
        long diffDays = diffSec / (24*60*60); //일자수 차이
        int days = (int) diffDays + 1;
        Log.d("계산", "scoreCalculate: 초 " + diffSec);
        Log.d("계산", "scoreCalculate: 일자 " + diffDays);

        int result;
        if(days <= 1){
            result = 10;
        } else if(days <=2){
            result = 5;
        } else {
            result = 2;
        }
        return result;

    }

    void ted(){
        PermissionListener permissionLitener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.d("permission", "onPermissionDenied: 요청 실패");
            }
        };

        TedPermission.with(getContext()).setPermissionListener(permissionLitener)
                .setDeniedMessage("라이브에 참여하기 위해선 카메라, 오디오 권한이 필요합니다")
                .setPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ,Manifest.permission.RECORD_AUDIO})
                .check();


    }

    private void openLive(String live_id, String title){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveData> call2 = apiInterface2.sendOpenAlarm(live_id,title);
        call2.enqueue(new Callback<LiveData>() {
            @Override
            public void onResponse(Call<LiveData> call2, Response<LiveData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        AlertDialog.Builder ad3 = new AlertDialog.Builder(getContext());
                        ad3.setTitle("전송 완료");
                        ad3.setMessage("알림 신청한 회원들에게 라이브 시작 알림을 보냈습니다.");
                        ad3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getContext(),LiveWebViewActivity.class);
                                intent.putExtra("room_id",live_id);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog3 = ad3.create();
                        alertDialog3.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveData> call2, Throwable t) {
                Toast.makeText(getContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

}