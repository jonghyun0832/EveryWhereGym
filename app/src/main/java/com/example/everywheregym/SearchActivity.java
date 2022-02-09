package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ImageView iv_back;
    private EditText et_search;
    private TextView tv_delete_all;
    private TextView tv_empty;

    private String user_id;

    private RecyclerView recyclerView;
    private ArrayList<SearchData> searchArray;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        iv_back = findViewById(R.id.iv_search_back);
        et_search = findViewById(R.id.et_search);
        tv_delete_all = findViewById(R.id.tv_search_delete_all);
        tv_empty = findViewById(R.id.tv_search_empty);

        tv_empty.setVisibility(View.INVISIBLE);

        et_search.requestFocus();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_search_history);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchArray = new ArrayList<>();
        searchAdapter = new SearchAdapter(searchArray, this);
        recyclerView.setAdapter(searchAdapter);

        SharedPreferences sharedPreferences= getSharedPreferences("info", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","0");

        //resume에서 리스트 받아옴

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER){

                    String search_text = et_search.getText().toString();

                    if(!search_text.equals("")){
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<SearchDataArray> call = apiInterface.addSearchHistory(user_id,search_text);
                        call.enqueue(new Callback<SearchDataArray>() {
                            @Override
                            public void onResponse(Call<SearchDataArray> call, Response<SearchDataArray> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if(response.body().isSuccess()){
                                        Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                                        intent.putExtra("search_text",search_text);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SearchDataArray> call, Throwable t) {
                                //실패
                            }
                        });
                        return true;

                    } else {
                        return false;
                    }

                } else {
                    return false;
                }

            }
        });


        tv_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad = new AlertDialog.Builder(SearchActivity.this);
                ad.setTitle("알림");
                ad.setMessage("검색 기록을 모두 삭제하시겠습니까?");
                ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<SearchDataArray> call = apiInterface.deleteSearchHistoryAll(user_id);
                        call.enqueue(new Callback<SearchDataArray>() {
                            @Override
                            public void onResponse(Call<SearchDataArray> call, Response<SearchDataArray> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    searchArray = response.body().getSearchDataArray();
                                    searchAdapter.setArrayList(searchArray);
                                    searchAdapter.notifyDataSetChanged();
                                    //결과가 없으면 없다고 띄워줌
                                    if(!response.body().isSuccess()){
                                        recyclerView.setVisibility(View.INVISIBLE);
                                        tv_empty.setVisibility(View.VISIBLE);
                                        tv_delete_all.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SearchDataArray> call, Throwable t) {
                                //실패
                            }
                        });
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

        searchAdapter.setOnDeleteClickListener(new SearchAdapter.myRecyclerViewDeleteClickListener() {
            @Override
            public void whenDeleteClick(int position) {
                final SearchData item = searchArray.get(position);
                String sh_id = item.getSearch_id();

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<SearchDataArray> call = apiInterface.deleteSearchHistory(user_id,sh_id);
                call.enqueue(new Callback<SearchDataArray>() {
                    @Override
                    public void onResponse(Call<SearchDataArray> call, Response<SearchDataArray> response) {
                        if (response.isSuccessful() && response.body() != null){
                            searchArray = response.body().getSearchDataArray();
                            searchAdapter.setArrayList(searchArray);
                            searchAdapter.notifyDataSetChanged();

                            if(!response.body().isSuccess()){
                                recyclerView.setVisibility(View.INVISIBLE);
                                tv_empty.setVisibility(View.VISIBLE);
                                tv_delete_all.setVisibility(View.INVISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                tv_empty.setVisibility(View.INVISIBLE);
                                tv_delete_all.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchDataArray> call, Throwable t) {
                        //실패
                    }
                });

            }
        });

        searchAdapter.setOnClickListener(new VodAdapter.myRecyclerViewClickListener() {
            @Override
            public void whenItemClick(int position) {

                String search_text = searchArray.get(position).getSearch_text();

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<SearchDataArray> call = apiInterface.addSearchHistory(user_id,search_text);
                call.enqueue(new Callback<SearchDataArray>() {
                    @Override
                    public void onResponse(Call<SearchDataArray> call, Response<SearchDataArray> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                                intent.putExtra("search_text",search_text);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchDataArray> call, Throwable t) {
                        //실패
                    }
                });
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SearchDataArray> call = apiInterface.getSearchHistory(user_id);
        call.enqueue(new Callback<SearchDataArray>() {
            @Override
            public void onResponse(Call<SearchDataArray> call, Response<SearchDataArray> response) {
                if (response.isSuccessful() && response.body() != null){
                    searchArray = response.body().getSearchDataArray();
                    searchAdapter.setArrayList(searchArray);
                    searchAdapter.notifyDataSetChanged();

                    if(!response.body().isSuccess()){
                        recyclerView.setVisibility(View.INVISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_delete_all.setVisibility(View.INVISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.INVISIBLE);
                        tv_delete_all.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchDataArray> call, Throwable t) {
                //실패
            }
        });
    }
}