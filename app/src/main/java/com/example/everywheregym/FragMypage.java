package com.example.everywheregym;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragMypage extends Fragment {

    Button btn_setting;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_mypage,container,false);
        Log.d("TAG", "MYPAGEonCreateView: ");

        btn_setting = view.findViewById(R.id.button_setting);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);

            }
        });



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "MYPAGEonPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "MYPAGEonDestroy: ");
    }
}
