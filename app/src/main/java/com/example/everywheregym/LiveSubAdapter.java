package com.example.everywheregym;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LiveSubAdapter extends RecyclerView.Adapter<LiveSubAdapter.LiveSubViewHolder> {

    private ArrayList<LiveData> arrayList;
    private Context context;

    public LiveSubAdapter(ArrayList<LiveData> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setArrayList (ArrayList<LiveData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public LiveSubAdapter.LiveSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_live_sub,parent,false);
        LiveSubAdapter.LiveSubViewHolder holder = new LiveSubAdapter.LiveSubViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveSubAdapter.LiveSubViewHolder holder, int position) {
        String str_hour = arrayList.get(position).getLive_start_hour();
        String str_min = arrayList.get(position).getLive_start_minute();
        if(Integer.parseInt(arrayList.get(position).getLive_start_hour()) < 10 ){
            str_hour = "0" + arrayList.get(position).getLive_start_hour();
        }
        if(Integer.parseInt(arrayList.get(position).getLive_start_minute()) < 10 ){
            str_min = "0" + arrayList.get(position).getLive_start_minute();
        }
        String start_time = str_hour + " : " + str_min;
        holder.tv_start_time.setText(start_time);
        holder.tv_spend_time.setText(arrayList.get(position).getLive_spend_time() + "분");

        //업로더 이미지 글라이드로
        String get_uploader_img_path;
        if(arrayList.get(position).getUploader_img().equals("")){
            get_uploader_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/src/image/IMAGE_no_image.jpeg";
        }else{
            get_uploader_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/src/image/" + arrayList.get(position).getUploader_img();
        }
        Glide.with(context).load(get_uploader_img_path).into(holder.iv_uploader_img);

        holder.tv_uploader_name.setText(arrayList.get(position).getUploader_name());
        holder.tv_title.setText(arrayList.get(position).getLive_title());

        holder.tv_calorie.setText(arrayList.get(position).getLive_calorie() + "Kcal");
        int num_calorie = Integer.parseInt(arrayList.get(position).getLive_calorie());
        int num_spend_time = Integer.parseInt(arrayList.get(position).getLive_spend_time());
        int difficulty = num_calorie / num_spend_time;

        if(difficulty <= 4) {
            holder.tv_calorie.setTextColor(Color.BLUE);
            //holder.tv_calorie.setTextColor(Color.WHITE);
            //holder.tv_calorie.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_small_1));
        } else if (difficulty < 7){
            holder.tv_calorie.setTextColor(Color.GREEN);
            //holder.tv_calorie.setTextColor(Color.WHITE);
            //holder.tv_calorie.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_small_2));
        } else {
            holder.tv_calorie.setTextColor(Color.RED);
            //holder.tv_calorie.setTextColor(Color.WHITE);
            //holder.tv_calorie.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_small_3));
        }

        //칼로리를 시간으로 나눠서 적당히 분류해서 텍스트 색 다르게 표시 ㄱㄱ
        if(!arrayList.get(position).getLive_material().equals("")){
            String material = "준비물 : " + arrayList.get(position).getLive_material();
            holder.tv_material.setText(material);
        } else {
            holder.tv_material.setText("준비물 없음");
        }

        String limit = "제한 : " + arrayList.get(position).getLive_limit_join() + "명";
        holder.tv_limit.setText(limit);

        if(arrayList.get(position).getAlarm_num() != null){
            String str_alarm = "알림등록 : " + arrayList.get(position).getAlarm_num() + "명";
            holder.tv_alarm.setText(str_alarm);
        } else {
            String str_alarm = "알림등록 : 없음";
            holder.tv_alarm.setText(str_alarm);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LiveSubViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_start_time;
        public TextView tv_spend_time;
        public ImageView iv_uploader_img;
        public TextView tv_uploader_name;
        public TextView tv_title;
        public TextView tv_calorie;
        public TextView tv_material;
        public TextView tv_limit;
        public TextView tv_alarm;


        public LiveSubViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_start_time = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_start_time);
            tv_spend_time = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_spend_time);
            iv_uploader_img = (ImageView) itemView.findViewById(R.id.iv_rv_live_sub_uploader_img);
            tv_uploader_name = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_uploader_name);
            tv_title = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_title);
            tv_calorie = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_calorie);
            tv_material = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_material);
            tv_limit = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_limit);
            tv_alarm = (TextView) itemView.findViewById(R.id.tv_rv_live_sub_alarm);
        }

    }
}
