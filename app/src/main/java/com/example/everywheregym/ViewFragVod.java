package com.example.everywheregym;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewFragVod extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<VodData> vodArray;
    private VodUploaderAdapter vodUploaderAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static ViewFragVod newInstance(int number) {
        ViewFragVod fragmentvod = new ViewFragVod();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragmentvod.setArguments(bundle);
        return fragmentvod;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("why", "onCreate: ");

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }

        //intro = ((ShowProfileActivity)getActivity()).tr_intro;
        //여기서 리스트 받는다
        vodArray = new ArrayList<>();
        vodArray = ((ShowProfileActivity)getActivity()).getVodArray;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_frag_vod,container, false);

        Log.d("why", "onCreateView22222: ");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_small);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        vodUploaderAdapter = new VodUploaderAdapter(vodArray, getActivity());
        recyclerView.setAdapter(vodUploaderAdapter);

        vodUploaderAdapter.setOnClickListener(new VodUploaderAdapter.vodUploaderAdapterClickListener() {
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

                Intent intent = new Intent(getContext(),TestActivity.class);
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






        return v;
    }
}