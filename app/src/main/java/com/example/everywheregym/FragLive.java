package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private String selected_date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_live,container,false);

        calendar = (MaterialCalendarView) view.findViewById(R.id.live_calendar);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_live);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        liveArray= new ArrayList<>();
        liveAdapter = new LiveAdapter(liveArray, getActivity());
        recyclerView.setAdapter(liveAdapter);

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

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

        selected_date = y + "." +  (m+1) + "." + d;
        callList(selected_date);

        calendar.state().edit()
                .setMinimumDate(CalendarDay.from(y,m,d))
                .setMaximumDate(CalendarDay.from(y,m,d+6))
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
                new DecoratorSelector(getActivity()),
                new DecoratorToday(),
                new DecoratorEvent(Color.RED, dates)
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

                selected_date = year + "." +  (month+1) + "." + day;

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
//                        Intent intent = new Intent(getContext(), VodDetailActivity.class);
//                        intent.putExtra("vod_id",vod_id);
//                        intent.putExtra("vod_thumbnail_path",vod_thumbnail_path);
//                        intent.putExtra("vod_time",vod_time);
//                        intent.putExtra("vod_title",vod_title);
//                        intent.putExtra("vod_category",vod_category);
//                        intent.putExtra("vod_difficulty",vod_difficulty);
//                        intent.putExtra("vod_explain",vod_explain);
//                        intent.putExtra("vod_material",vod_material);
//                        intent.putExtra("vod_calorie",vod_calorie);
//                        intent.putExtra("isEdit",true);
//
//                        startActivity(intent);
                    }
                });

                //라이브 삭제
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        AlertDialog.Builder ad2 = new AlertDialog.Builder(getContext());
                        ad2.setTitle("알림");
                        ad2.setMessage("해당 라이브 일정을 삭제하시겠습니까?\n라이브 삭제는 패널티가 적용됩니다");
                        ad2.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //삭제 레트로핏
                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<LiveData> call = apiInterface.deleteLive(li_id);
                                call.enqueue(new Callback<LiveData>() {
                                    @Override
                                    public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                                        if (response.isSuccessful() && response.body() != null){
                                            if(response.body().isSuccess()){

                                                //삭제하는 이유가 뭔지 작성하게하고
                                                //작성한 내용을 알림등록 해놓은 회원들한테
                                                //notification으로 알려줘야한다.
                                                //수정의 경우에도 마찬가지다.

                                                //리로드 (트레이너만 삭제되니까 걱정안해도됨)
                                                final FragmentTransaction ftt = getActivity().getSupportFragmentManager().beginTransaction();
                                                ftt.replace(R.id.main_frame_tr, new FragLive());
                                                ftt.commit();

                                                Toast.makeText(getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LiveData> call, Throwable t) {
                                        Toast.makeText(getContext(), "삭제 통신 문제.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                        ad2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
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
                //시간에 따라 버튼 달라지니까
                //버튼 내용에 따라서 알람등록 / 방송참여 할수있게 해줘야함
                if(vh.btn_push.getText().toString().equals("알림받기")){
                    //알림 테이블에 저장
                    setAlarm(user_id,li_id,uploader_id);

                } else if (vh.btn_push.getText().toString().equals("알림해제")){
                    //라이브 시작하기
                    deleteAlarm(user_id,li_id,uploader_id);
                } else if (vh.btn_push.getText().toString().equals("라이브 시작")){
                    //라이브 시작하기
                }

            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void callList(String put_date){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LiveDataArray> call = apiInterface.getliveList(put_date);
        call.enqueue(new Callback<LiveDataArray>() {
            @Override
            public void onResponse(Call<LiveDataArray> call, Response<LiveDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveArray = response.body().getLiveDataArray();
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

}