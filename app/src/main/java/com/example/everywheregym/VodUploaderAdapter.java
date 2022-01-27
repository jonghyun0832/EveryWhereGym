package com.example.everywheregym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VodUploaderAdapter extends RecyclerView.Adapter<VodUploaderAdapter.VodUploaderViewHolder> {

    public interface vodUploaderAdapterClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenItemClick(int position);
    }

    private VodUploaderAdapter.vodUploaderAdapterClickListener myListener;

    public void setOnClickListener(VodUploaderAdapter.vodUploaderAdapterClickListener listener){
        myListener = listener;
    }

    private ArrayList<VodData> arrayList;
    private Context context;

    public VodUploaderAdapter(ArrayList<VodData> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    //두개 겹침
    public void setArrayList(ArrayList<VodData> arrayList){
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public VodUploaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_upload_vod,parent,false);
        VodUploaderViewHolder holder = new VodUploaderViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VodUploaderViewHolder holder, int position) {

        String get_thumbnail;
        if(arrayList.get(position).getVod_thumbnail().equals("")){
            get_thumbnail = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        } else {
            get_thumbnail = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + arrayList.get(position).getVod_thumbnail();
        }
        Glide.with(context).load(get_thumbnail).into(holder.iv_thumbnail);

        holder.tv_length.setText(arrayList.get(position).getVod_time());
        holder.tv_title.setText(arrayList.get(position).getVod_title());

        String get_view = "조회수 " + arrayList.get(position).getVod_view() + "회";
        holder.tv_view.setText(get_view);

        String get_diff = arrayList.get(position).getVod_difficulty();
        holder.tv_difficulty.setText(get_diff);
        if(get_diff.equals("입문")){
            holder.tv_difficulty.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_small_1));
        }else if(get_diff.equals("초급")){
            holder.tv_difficulty.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_small_2));
        }else {
            holder.tv_difficulty.setBackground(ContextCompat.getDrawable(context,R.drawable.difficulty_small_3));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VodUploaderViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_thumbnail;
        public TextView tv_length;
        public TextView tv_title;
        public TextView tv_view;
        public TextView tv_difficulty;


        public VodUploaderViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_thumbnail = (ImageView) itemView.findViewById(R.id.rv_iv_uploader_vod_thumbnail);
            this.tv_length = (TextView) itemView.findViewById(R.id.rv_tv_uploader_vod_length);
            this.tv_title = (TextView) itemView.findViewById(R.id.rv_tv_uploader_vod_title);
            this.tv_view = (TextView) itemView.findViewById(R.id.rv_tv_uploader_vod_view);
            this.tv_difficulty = (TextView) itemView.findViewById(R.id.rv_tv_uploader_vod_difficulty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        myListener.whenItemClick(pos);
                    }
                }
            });
        }
    }
}
