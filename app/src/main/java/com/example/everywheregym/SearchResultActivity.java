package com.example.everywheregym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonClickListener;
import com.skydoves.balloon.ViewExtensionKt;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    private ImageView iv_back;
    private EditText et_search;
    private TextView tv_empty;

    private RecyclerView recyclerView;
    private ArrayList<VodData> vodArray;
    private VodAdapter vodAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id;

    private String send_search_text;

    private ProgressDialog prDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        iv_back = findViewById(R.id.iv_search_result_back);
        et_search = findViewById(R.id.et_search_result);
        tv_empty = findViewById(R.id.tv_search_result_empty);

        recyclerView = (RecyclerView) findViewById(R.id.rv_search_result);
        linearLayoutManager = new LinearLayoutManager(SearchResultActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        vodArray = new ArrayList<>();
        vodAdapter = new VodAdapter(vodArray, this);
        recyclerView.setAdapter(vodAdapter);

        initDialog();

        tv_empty.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try{
            Intent get_intent = getIntent();
            String getted_text = get_intent.getStringExtra("search_text");
            et_search.setText(getted_text);
            send_search_text = getted_text;
        }catch (Exception e){
            e.printStackTrace();
        }

        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Intent intent = new Intent(SearchResultActivity.this,SearchActivity.class);
                    String search_text = et_search.getText().toString();
                    intent.putExtra("search_text",search_text);
                    startActivity(intent);
                }
            }
        });

//        et_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SearchResultActivity.this,SearchActivity.class);
//                String search_text = et_search.getText().toString();
//                intent.putExtra("search_text",search_text);
//                startActivity(intent);
//            }
//        });

//        et_search.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER){
//
//                    String search_text = et_search.getText().toString();
//
//                    if(!search_text.equals("")){
//                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//                        Call<SearchDataArray> call = apiInterface.addSearchHistory(user_id,search_text);
//                        call.enqueue(new Callback<SearchDataArray>() {
//                            @Override
//                            public void onResponse(Call<SearchDataArray> call, Response<SearchDataArray> response) {
//                                if (response.isSuccessful() && response.body() != null){
//                                    if(response.body().isSuccess()){
//                                        send_search_text = search_text;
//                                        getSearchResult(send_search_text);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<SearchDataArray> call, Throwable t) {
//                                //실패
//                            }
//                        });
//                        hideKeyboard();
//                        return true;
//
//                    } else {
//                        hideKeyboard();
//                        return false;
//                    }
//
//                } else {
//                    return false;
//                }
//
//            }
//        });

        vodAdapter.setOnClickListener(new VodAdapter.myRecyclerViewClickListener() {
            @Override
            public void whenItemClick(int position) {
                final VodData item = vodArray.get(position);
                String vod_path = item.getVod_path();
                String vod_title = item.getVod_title();
                String vod_difficulty = item.getVod_difficulty();
                String vod_id = item.getVod_id();
                String vod_calorie = item.getVod_calorie();
                String vod_category = item.getVod_category();
                String vod_material = item.getVod_materail();
                String vod_uploader_name = item.getVod_uploader_name();
                String vod_explain = item.getVod_explain();
                String vod_uploader_img = item.getVod_uploader_img();
                String vod_uploader_id = item.getVod_uploader_id();
                String vod_thumbnail = item.getVod_thumbnail();
                int vod_view = item.getVod_view();

                Intent intent = new Intent(SearchResultActivity.this,TestActivity.class);
                intent.putExtra("vod_path",vod_path);
                intent.putExtra("vod_title",vod_title);
                intent.putExtra("vod_difficulty",vod_difficulty);
                intent.putExtra("vod_id",vod_id);
                intent.putExtra("vod_view",vod_view);
                intent.putExtra("vod_calorie",vod_calorie);
                intent.putExtra("vod_category",vod_category);
                intent.putExtra("vod_material",vod_material);
                intent.putExtra("vod_uploader_name",vod_uploader_name);
                intent.putExtra("vod_explain",vod_explain);
                intent.putExtra("vod_uploader_img",vod_uploader_img);
                intent.putExtra("vod_uploader_id",vod_uploader_id);
                intent.putExtra("vod_thumbnail",vod_thumbnail);
                startActivity(intent);
            }
        });


        vodAdapter.setOnClickImgListener(new VodAdapter.myRecyclerViewImgClickListener() {
            @Override
            public void whenImgClick(int position) {
                Intent intent = new Intent(SearchResultActivity.this,ShowProfileActivity.class);
                intent.putExtra("uploader_id",vodArray.get(position).getVod_uploader_id());
                startActivity(intent);
            }
        });


        vodAdapter.setOnClickMoreListener(new VodAdapter.myRecyclerViewMoreClickListener() {
            @Override
            public void whenMoreClick(int position) {

                String vod_id = vodArray.get(position).getVod_id(); //수정에서 보냄
                String vod_thumbnail_path = vodArray.get(position).getVod_thumbnail(); //수정에서 보냄
                String vod_time = vodArray.get(position).getVod_time(); //수정에서 보냄
                String vod_title = vodArray.get(position).getVod_title(); //수정에서 보냄
                String vod_category = vodArray.get(position).getVod_category(); //수정에서 보냄
                String vod_difficulty = vodArray.get(position).getVod_difficulty(); //수정에서 보냄
                String vod_path = vodArray.get(position).getVod_path();
                String vod_explain = vodArray.get(position).getVod_explain();
                String vod_material = vodArray.get(position).getVod_materail();
                String vod_calorie = vodArray.get(position).getVod_calorie();




                AlertDialog.Builder ad = new AlertDialog.Builder(SearchResultActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_vod_more, null);

                TextView tv_more_edit = dialogView.findViewById(R.id.tv_more_edit);
                TextView tv_more_delete = dialogView.findViewById(R.id.tv_more_delete);
                TextView tv_more_register = dialogView.findViewById(R.id.tv_more_register);
                ImageView iv_more_edit = dialogView.findViewById(R.id.iv_more_edit);
                ImageView iv_more_delete = dialogView.findViewById(R.id.iv_more_delete);
                ImageView iv_more_register = dialogView.findViewById(R.id.iv_more_register);


                //북마크 확인
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<VodData> call = apiInterface.checkBookMark(user_id,vod_id);
                call.enqueue(new Callback<VodData>() {
                    @Override
                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                //북마크 없을때
                                tv_more_register.setText("북마크 추가");
                            } else {
                                //이미 북마크 된경우
                                tv_more_register.setText("북마크 해제");
                                iv_more_register.setImageDrawable(ContextCompat.getDrawable(SearchResultActivity.this,R.drawable.ic_baseline_cancel_24));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VodData> call, Throwable t) {
                        //실패
                    }
                });



                tv_more_edit.setVisibility(View.GONE);
                tv_more_delete.setVisibility(View.GONE);
                iv_more_edit.setVisibility(View.GONE);
                iv_more_delete.setVisibility(View.GONE);

                if (user_id.equals(vodArray.get(position).getVod_uploader_id())){
                    tv_more_edit.setVisibility(View.VISIBLE);
                    tv_more_delete.setVisibility(View.VISIBLE);
                    iv_more_edit.setVisibility(View.VISIBLE);
                    iv_more_delete.setVisibility(View.VISIBLE);
                }



                ad.setView(dialogView);

                AlertDialog alertDialog = ad.create();

                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point deviceSize = new Point();
                display.getSize(deviceSize);

                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.width = deviceSize.x;
                params.horizontalMargin = 0.0f;
                alertDialog.getWindow().setAttributes(params);

                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                window.setGravity(Gravity.BOTTOM);
                alertDialog.show();

                //동영상 수정하기
                tv_more_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(SearchResultActivity.this, VodDetailActivity.class);
                        intent.putExtra("vod_id",vod_id);
                        intent.putExtra("vod_thumbnail_path",vod_thumbnail_path);
                        intent.putExtra("vod_time",vod_time);
                        intent.putExtra("vod_title",vod_title);
                        intent.putExtra("vod_category",vod_category);
                        intent.putExtra("vod_difficulty",vod_difficulty);
                        intent.putExtra("vod_explain",vod_explain);
                        intent.putExtra("vod_material",vod_material);
                        intent.putExtra("vod_calorie",vod_calorie);
                        intent.putExtra("isEdit",true);

                        startActivity(intent);
                    }
                });

                //동영상 삭제하기
                tv_more_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        AlertDialog.Builder ad2 = new AlertDialog.Builder(SearchResultActivity.this);
                        ad2.setTitle("알림");
                        ad2.setMessage("동영상을 삭제하시겠습니까?");
                        ad2.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //삭제 레트로핏
                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<VodData> call = apiInterface.deleteVod(vod_id,vod_thumbnail_path,vod_path);
                                call.enqueue(new Callback<VodData>() {
                                    @Override
                                    public void onResponse(Call<VodData> call, Response<VodData> response) {
                                        if (response.isSuccessful() && response.body() != null){
                                            if(response.body().isSuccess()){

                                                //리로드 (트레이너만 삭제되니까 걱정안해도됨)
                                                final FragmentTransaction ftt = SearchResultActivity.this.getSupportFragmentManager().beginTransaction();
                                                ftt.replace(R.id.main_frame_tr, new FragVideo());
                                                ftt.commit();

                                                Toast.makeText(SearchResultActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<VodData> call, Throwable t) {
                                        Toast.makeText(SearchResultActivity.this, "삭제 통신 문제.", Toast.LENGTH_SHORT).show();
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


                //즐겨찾기 등록
                tv_more_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();

                        if(tv_more_register.getText().toString().equals("북마크 추가")){

                            //즐겨찾기 테이블에 저장하기 ㄱㄱ
                            //레트로핏으로 해당 영상 정보 보내기
                            //리스트에 있으면 북마크 해제, 없으면 북마크 설정하기
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkVod(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(SearchResultActivity.this);
                                            ad.setTitle("알림");
                                            ad.setMessage("동영상이 북마크에 추가되었습니다");
                                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VodData> call, Throwable t) {
                                    //실패
                                }
                            });

                        } else {
                            //북마크 해제
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<VodData> call = apiInterface.bookmarkDelete(user_id,vod_id);
                            call.enqueue(new Callback<VodData>() {
                                @Override
                                public void onResponse(Call<VodData> call, Response<VodData> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().isSuccess()){
                                            AlertDialog.Builder ad = new AlertDialog.Builder(SearchResultActivity.this);
                                            ad.setTitle("알림");
                                            ad.setMessage("해당 동영상의 북마크를 해제했습니다");
                                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }

                                            });
                                            AlertDialog alertDialog = ad.create();
                                            alertDialog.show();
                                        }else {
                                            Toast.makeText(SearchResultActivity.this, "서버에서 삭제실패", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VodData> call, Throwable t) {
                                    //실패
                                }
                            });

                        }

                    }
                });

            }
        });

        vodAdapter.setOnClickPopupListener(new VodAdapter.myRecyclerViewPopUpClickListener() {
            @Override
            public void whenPopUpClick(VodAdapter.VodViewHolder vh, int position) {
                String vod_material = vodArray.get(position).getVod_materail();
                Balloon balloon = new Balloon.Builder(SearchResultActivity.this)
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setArrowPosition(0.1f)
                        .setHeight(70)
                        .setTextSize(20f)
                        .setCornerRadius(4f)
                        .setAlpha(0.7f)
                        .setText("준비물 : " + vod_material)
                        .setTextColor(ContextCompat.getColor(SearchResultActivity.this, R.color.white))
                        .setBackgroundColor(ContextCompat.getColor(SearchResultActivity.this, R.color.fb))
                        .setBalloonAnimation(BalloonAnimation.FADE)
                        .build();

                balloon.showAlignBottom(vh.iv_popup_material);
                balloon.setOnBalloonClickListener(new OnBalloonClickListener() {
                    @Override
                    public void onBalloonClick(@NonNull View view) {
                        balloon.dismiss();
                    }
                });
                balloon.dismissWithDelay(2500L);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        getSearchResult(send_search_text);
        //hideKeyboard();

    }

    @Override
    protected void onPause() {
        super.onPause();
        et_search.clearFocus();
    }

    private void getSearchResult(String search_text){

        showpDialog();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<VodDataArray> call = apiInterface.getSearchList(search_text);
        call.enqueue(new Callback<VodDataArray>() {
            @Override
            public void onResponse(Call<VodDataArray> call, Response<VodDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    //로딩 숨기기
                    hidepDialog();

                    if(response.body().isSuccess()){
                        vodArray = response.body().getVodDataArray();
                        vodAdapter.setAdapter(vodArray);
                        vodAdapter.notifyDataSetChanged();

                        tv_empty.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {

                        tv_empty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<VodDataArray> call, Throwable t) {
                Toast.makeText(SearchResultActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void initDialog(){
        prDialog = new ProgressDialog(SearchResultActivity.this);
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}