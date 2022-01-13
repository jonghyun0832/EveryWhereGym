package com.example.everywheregym;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragLive extends Fragment {

    TextView tv_live;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_live,container,false);

        tv_live = view.findViewById(R.id.textView_live);


        //코드

        SharedPreferences sharedPreferences= getContext().getSharedPreferences("info", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","0");

        tv_live.setText(user_id);


        return view;
    }
}