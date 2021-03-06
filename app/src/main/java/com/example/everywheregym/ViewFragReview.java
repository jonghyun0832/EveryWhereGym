package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewFragReview extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ReviewData> reviewArray;
    private ReviewAdapter reviewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String user_id;
    private String uploader;

    private NestedScrollView nsv;
    private int page = 1;
    private String cursor = "0";
    private boolean isEnd = false;
    private ProgressBar pbBar;

    public static ViewFragReview newInstance(int number) {
        ViewFragReview fragmentReview = new ViewFragReview();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragmentReview.setArguments(bundle);
        return fragmentReview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("review", "onCreate: ");

        page = 1;
        cursor = "0";

        uploader = ((ShowProfileActivity)getActivity()).show_uploader_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_frag_review, container, false);

        Log.d("review", "onCreateView: ");

        nsv = (NestedScrollView) v.findViewById(R.id.nsv_review);
        pbBar = (ProgressBar) v.findViewById(R.id.pb_review);

        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!nsv.canScrollVertically(1)){
                    Log.d("?????????", "??????????????? ????????? ");
                    if(!isEnd){
                        page++;
                        getReview(uploader);
                    }
                }
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_frag_review);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        reviewArray = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewArray, getActivity());
        recyclerView.setAdapter(reviewAdapter);

        //????????????
        reviewAdapter.setOnEditClickListener(new ReviewAdapter.myRecyclerViewEditClickListener() {
            @Override
            public void whenEditClick(int position) {
                String review_text = reviewArray.get(position).getRv_text();
                String review_title = reviewArray.get(position).getRv_title();
                String review_id = reviewArray.get(position).getRv_id();
                String review_score = reviewArray.get(position).getRv_score();


                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("??????");
                ad.setMessage("????????? ?????? ???????????????????");
                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //intent??? ???????????? ??????
                        Intent intent = new Intent(getContext(),reviewActivity.class);
                        intent.putExtra("Edit",true);
                        intent.putExtra("text",review_text);
                        intent.putExtra("title",review_title);
                        intent.putExtra("id",review_id);
                        intent.putExtra("score",review_score);
                        getContext().startActivity(intent);
                    }

                });
                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });

        //????????????
        reviewAdapter.setOnDeleteClickListener(new ReviewAdapter.myRecyclerViewDeleteClickListener() {
            @Override
            public void whenDeleteClick(int position) {
                String rv_id = reviewArray.get(position).getRv_id();

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("??????");
                ad.setMessage("????????? ?????? ???????????????????");
                ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //?????????????????? ?????? ??????
                        deleteReview(rv_id);
                    }

                });
                ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = ad.create();
                alertDialog.show();
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("review", "onResume: ");
        pbBar.setVisibility(View.VISIBLE);

        reviewArray = new ArrayList<>();
        page = 1;
        cursor = "0";

        getReview(uploader);
        ((ShowProfileActivity)getActivity()).getReviewScore(uploader);
    }

    private void getReview(String uploader_id){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReviewDataArray> call2 = apiInterface2.getReview(uploader_id,page,cursor);
        call2.enqueue(new Callback<ReviewDataArray>() {
            @Override
            public void onResponse(Call<ReviewDataArray> call2, Response<ReviewDataArray> response2) {
                if (response2.isSuccessful() && response2.body() != null){

                    isEnd = response2.body().isEnd();
                    if(isEnd){
                        pbBar.setVisibility(View.GONE);
                    }
                    cursor = response2.body().getCursor();
                    ArrayList<ReviewData> reviewArray_tmp = response2.body().getReviewDataArray();
                    reviewArray.addAll(reviewArray_tmp);

                    //reviewArray = response2.body().getReviewDataArray();
                    reviewAdapter.setArrayList(reviewArray);
                    reviewAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ReviewDataArray> call2, Throwable t) {
                Toast.makeText(getContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteReview(String review_id){
        ApiInterface apiInterface2 = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReviewData> call2 = apiInterface2.deleteReivew(review_id);
        call2.enqueue(new Callback<ReviewData>() {
            @Override
            public void onResponse(Call<ReviewData> call2, Response<ReviewData> response2) {
                if (response2.isSuccessful() && response2.body() != null){
                    if(response2.body().isSuccess()){
                        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                        ad.setTitle("??????");
                        ad.setMessage("????????? ?????????????????????.");
                        ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                final FragmentTransaction ftt = getActivity().getSupportFragmentManager().beginTransaction();
//                                ftt.replace(R.id.main_frame_tr, new FragVideo());
//                                ftt.commit();
                                pbBar.setVisibility(View.VISIBLE);

                                reviewArray = new ArrayList<>();
                                page = 1;
                                cursor = "0";

                                getReview(uploader);
                                ((ShowProfileActivity)getActivity()).getReviewScore(uploader);
                            }

                        });
                        AlertDialog alertDialog = ad.create();
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewData> call2, Throwable t) {
                Toast.makeText(getContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
