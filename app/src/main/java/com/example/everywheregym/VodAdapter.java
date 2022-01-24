package com.example.everywheregym;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VodAdapter extends RecyclerView.Adapter<VodAdapter.VodViewHolder> {

    //리사이클러뷰에는 온클릭리스너가 없으니까 인터페이스로 구현해보자
    public interface myRecyclerViewClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenItemClick(int position);
    }

    public interface myRecyclerViewImgClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenImgClick(int position);
    }

    public interface myRecyclerViewMoreClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenMoreClick(int position);
    }

    private myRecyclerViewClickListener myListener;
    private myRecyclerViewImgClickListener myImgListener;
    private myRecyclerViewMoreClickListener myMoreListener;

    public void setOnClickListener(myRecyclerViewClickListener listener){
        myListener = listener;
    }
    public void setOnClickImgListener(myRecyclerViewImgClickListener listener){
        myImgListener = listener;
    }
    public void setOnClickMoreListener(myRecyclerViewMoreClickListener listener){
        myMoreListener = listener;
    }


    private ArrayList<VodData> arrayList;
    private Context context;

    public VodAdapter(ArrayList<VodData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setAdapter(ArrayList<VodData> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VodAdapter.VodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vod,parent,false);
        VodViewHolder holder = new VodViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VodAdapter.VodViewHolder holder, int position) {
        holder.tv_difficulty.setText(arrayList.get(position).getVod_difficulty());
        if(arrayList.get(position).getVod_difficulty().equals("입문")){
            holder.tv_difficulty.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_1));
        }
        else if(arrayList.get(position).getVod_difficulty().equals("초급")){
            holder.tv_difficulty.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_2));
        } else {
            holder.tv_difficulty.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_3));
        }

        String get_thumbnail_path;
        if(arrayList.get(position).getVod_thumbnail().equals("")){
            get_thumbnail_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        }else{
            get_thumbnail_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + arrayList.get(position).getVod_thumbnail();
        }
        Glide.with(context).load(get_thumbnail_path).into(holder.iv_thumbnail);


        holder.tv_time.setText(arrayList.get(position).getVod_time());


        String get_uploader_img_path;
        if(arrayList.get(position).getVod_uploader_img().equals("")){
            get_uploader_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        }else {
            get_uploader_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + arrayList.get(position).getVod_uploader_img();
        }
        Glide.with(context).load(get_uploader_img_path).override(50,50).into(holder.iv_uploader_img);


        holder.tv_title.setText(arrayList.get(position).getVod_title());
        holder.tv_uploader_name.setText(arrayList.get(position).getVod_uploader_name());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VodViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_difficulty;
        public ImageView iv_thumbnail;
        public TextView tv_time;
        public ImageView iv_uploader_img;
        public TextView tv_title;
        public TextView tv_uploader_name;
        public ImageView iv_more;

        public VodViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_difficulty = (TextView) itemView.findViewById(R.id.tv_rv_difficulty);
            this.iv_thumbnail = (ImageView) itemView.findViewById(R.id.iv_rv_thumbnail);
            this.tv_time = (TextView) itemView.findViewById(R.id.tv_rv_vod_time);
            this.iv_uploader_img = (ImageView) itemView.findViewById(R.id.iv_rv_user_img);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_rv_vod_title);
            this.tv_uploader_name = (TextView) itemView.findViewById(R.id.tv_rv_vod_uploader);
            this.iv_more = (ImageView) itemView.findViewById(R.id.iv_vod_more);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        myListener.whenItemClick(pos);
                    }
                }
            });

            iv_uploader_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if (myImgListener != null){
                        myImgListener.whenImgClick(pos);
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
