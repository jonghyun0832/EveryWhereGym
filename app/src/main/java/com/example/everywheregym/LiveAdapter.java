package com.example.everywheregym;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.LiveViewHolder> {

    public interface myRecyclerViewBtnClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenBtnClick(LiveViewHolder vh, int position);;
    }

    private VodAdapter.myRecyclerViewImgClickListener myImgListener;
    private myRecyclerViewBtnClickListener myBtnListener;
    private VodAdapter.myRecyclerViewMoreClickListener myMoreListener;

    public void setOnImgClickListener(VodAdapter.myRecyclerViewImgClickListener listener){
        myImgListener = listener;
    }

    public void setOnButtonClickListener(myRecyclerViewBtnClickListener listener){
        myBtnListener = listener;
    }

    public void setOnMoreClickListener(VodAdapter.myRecyclerViewMoreClickListener listener){
        myMoreListener = listener;
    }

    private ArrayList<LiveData> arrayList;
    private Context context;

    public LiveAdapter(ArrayList<LiveData> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setArrayList (ArrayList<LiveData> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public LiveAdapter.LiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_live_main,parent,false);
        LiveAdapter.LiveViewHolder holder = new LiveAdapter.LiveViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveAdapter.LiveViewHolder holder, int position) {
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
            get_uploader_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        }else{
            get_uploader_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + arrayList.get(position).getUploader_img();
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

        SharedPreferences sharedPreferences= context.getSharedPreferences("info", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","");

        String live_id = arrayList.get(position).getLive_id();
        String uploader_id = arrayList.get(position).getUploader_id();
        String limit_join = arrayList.get(position).getLive_limit_join();

        if(user_id.equals(arrayList.get(position).getUploader_id())){
            if(arrayList.get(position).getEnable().equals("1")){
                String limit = "현재 : " + arrayList.get(position).getLive_join() + "명\n" + "제한 : " + arrayList.get(position).getLive_limit_join() + "명";
                holder.tv_limit.setText(limit);
                holder.btn_push.setText("라이브 시작");
                holder.btn_push.setBackground(ContextCompat.getDrawable(context,R.drawable.round_text_blue));
                holder.btn_push.setTextColor(Color.WHITE);
                holder.btn_push.setEnabled(true);
            } else {
                String limit = "제한 : " + arrayList.get(position).getLive_limit_join() + "명";
                holder.tv_limit.setText(limit);
                holder.btn_push.setText("라이브 시작");
                holder.btn_push.setBackground(ContextCompat.getDrawable(context,R.drawable.round_text_gray));
                holder.btn_push.setEnabled(false);
                holder.btn_push.setTextColor(Color.WHITE);
            }
            holder.iv_more.setVisibility(View.VISIBLE);
        } else {
            //트레이너가 열었을때 부터 참석가능해야함
            if(arrayList.get(position).getEnable().equals("1")){
                String limit = "현재 : " + arrayList.get(position).getLive_join() + "명\n" + "제한 : " + arrayList.get(position).getLive_limit_join() + "명";
                holder.tv_limit.setText(limit);
                holder.btn_push.setText("라이브 준비중");
                holder.btn_push.setBackground(ContextCompat.getDrawable(context,R.drawable.round_text_gray));
                holder.btn_push.setTextColor(Color.WHITE);
            } else {

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<LiveData> call = apiInterface.checkLiveAlarm(user_id,live_id,uploader_id);
                call.enqueue(new Callback<LiveData>() {
                    @Override
                    public void onResponse(Call<LiveData> call, Response<LiveData> response) {
                        if (response.isSuccessful() && response.body() != null){
                            if(response.body().isSuccess()){
                                String limit = "제한 : " + limit_join + "명";
                                holder.tv_limit.setText(limit);
                                holder.btn_push.setText("알림해제");
                                holder.btn_push.setBackground(ContextCompat.getDrawable(context,R.drawable.round_text_alarm_off));
                                holder.btn_push.setTextColor(Color.BLACK);
                            } else {
                                String limit = "제한 : " + limit_join + "명";
                                holder.tv_limit.setText(limit);
                                holder.btn_push.setText("알림받기");
                                holder.btn_push.setBackground(ContextCompat.getDrawable(context,R.drawable.round_text_light_green));
                                holder.btn_push.setTextColor(Color.BLACK);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LiveData> call, Throwable t) {
                        Toast.makeText(context, "통신 오류", Toast.LENGTH_SHORT).show();
                    }
                });


//                String limit = "제한 : " + arrayList.get(position).getLive_limit_join() + "명";
//                holder.tv_limit.setText(limit);
//                holder.btn_push.setText("알림받기");
//                holder.btn_push.setBackground(ContextCompat.getDrawable(context,R.drawable.round_text_light_green));
//                holder.btn_push.setTextColor(Color.BLACK);
            }
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LiveViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_start_time;
        public TextView tv_spend_time;
        public ImageView iv_uploader_img;
        public TextView tv_uploader_name;
        public TextView tv_title;
        public TextView tv_calorie;
        public TextView tv_material;
        public TextView tv_limit;
        public Button btn_push;
        public ImageView iv_more;

        public LiveViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_start_time = (TextView) itemView.findViewById(R.id.tv_rv_live_start_time);
            tv_spend_time = (TextView) itemView.findViewById(R.id.tv_rv_live_spend_time);
            iv_uploader_img = (ImageView) itemView.findViewById(R.id.iv_rv_live_uploader_img);
            tv_uploader_name = (TextView) itemView.findViewById(R.id.tv_rv_live_uploader_name);
            tv_title = (TextView) itemView.findViewById(R.id.tv_rv_live_title);
            tv_calorie = (TextView) itemView.findViewById(R.id.tv_rv_live_calorie);
            tv_material = (TextView) itemView.findViewById(R.id.tv_rv_live_material);
            tv_limit = (TextView) itemView.findViewById(R.id.tv_rv_live_limit);
            btn_push = (Button) itemView.findViewById(R.id.btn_rv_live_push);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_rv_live_more);

            iv_more.setVisibility(View.INVISIBLE);

            //뺴야되는거 : 프로필 사진 클릭시 이동, 버튼 클릭시 이동 or notification발송 총 2개

            iv_uploader_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(myImgListener != null){
                        myImgListener.whenImgClick(pos);
                    }
                }
            });

            btn_push.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(myBtnListener != null){
                        myBtnListener.whenBtnClick(LiveViewHolder.this,pos);
                    }
                }
            });

            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(myMoreListener != null){
                        myMoreListener.whenMoreClick(pos);
                    }
                }
            });



        }
    }
}
