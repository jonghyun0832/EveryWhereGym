package com.example.everywheregym;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewFragInfo extends Fragment {

    private TextView tv_showpf_intro;//프로필 소개
    private TextView tv_showpf_expert; //전문분야
    private TextView tv_showpf_career; //경력사항
    private TextView tv_showpf_certify; //자격사항

    private String intro;
    private String expert;
    private String career;
    private String certify;

    public static ViewFragInfo newInstance(int number) {
        ViewFragInfo fragmentinfo = new ViewFragInfo();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragmentinfo.setArguments(bundle);
        return fragmentinfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("why", "FragonCreate: ");

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }

        intro = ((ShowProfileActivity)getActivity()).tr_intro;
        expert = ((ShowProfileActivity)getActivity()).tr_expert;
        career = ((ShowProfileActivity)getActivity()).tr_career;
        certify = ((ShowProfileActivity)getActivity()).tr_certify;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_frag_info,container, false);

//        Log.d("why", "FragonCreateView: ");

        tv_showpf_intro = v.findViewById(R.id.textview_showPF_intro_fg);

        tv_showpf_expert = v.findViewById(R.id.textview_showPF_expert_fg);
        tv_showpf_career = v.findViewById(R.id.textview_showPF_career_fg);
        tv_showpf_certify = v.findViewById(R.id.textview_showPF_certify_fg);

        tv_showpf_intro.setText(intro);
        tv_showpf_expert.setText(expert);
        tv_showpf_career.setText(career);
        tv_showpf_certify.setText(certify);


        return v;
    }
}