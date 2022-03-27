package com.example.everywheregym;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public interface myRecyclerViewEditClickListener{
        void whenEditClick(int position);;
    }

    public interface myRecyclerViewDeleteClickListener{
        void whenDeleteClick(int position);;
    }

    private myRecyclerViewDeleteClickListener myDeleteListener;
    private myRecyclerViewEditClickListener myEditListener;

    public void setOnDeleteClickListener(myRecyclerViewDeleteClickListener listener){
        myDeleteListener = listener;
    }

    public void setOnEditClickListener(myRecyclerViewEditClickListener listener){
        myEditListener = listener;
    }

    private ArrayList<ReviewData> arrayList;
    private Context context;

    public ReviewAdapter(ArrayList<ReviewData> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setArrayList (ArrayList<ReviewData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_review_item,parent,false);
        ReviewAdapter.ReviewViewHolder holder = new ReviewAdapter.ReviewViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {


        String get_img_path;
        if(arrayList.get(position).getImg_path().equals("")){
            get_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/IMAGE_no_image.jpeg";
        }else{
            get_img_path = "http://ec2-54-180-29-233.ap-northeast-2.compute.amazonaws.com/image/" + arrayList.get(position).getImg_path();
        }
        Glide.with(context).load(get_img_path).into(holder.iv_user);

        String get_name = arrayList.get(position).getName();
        holder.tv_name.setText(get_name);

        //날짜는 고민좀
        holder.tv_date.setText(arrayList.get(position).getDate());

        float score = Float.parseFloat(arrayList.get(position).getRv_score());
        holder.rb.setRating(score);

        String review = arrayList.get(position).getRv_text();
        holder.tv_text.setText(review);

        String title = arrayList.get(position).getRv_title();
        holder.tv_title.setText(title);

        SharedPreferences sharedPreferences= context.getSharedPreferences("info", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id","");

        if (arrayList.get(position).getUser_id().equals(user_id)){
            holder.tv_btn_delete.setVisibility(View.VISIBLE);
            holder.tv_btn_edit.setVisibility(View.VISIBLE);
        } else {
            holder.tv_btn_delete.setVisibility(View.INVISIBLE);
            holder.tv_btn_edit.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_user;
        private TextView tv_name;
        private TextView tv_date;
        private RatingBar rb;
        private TextView tv_text;
        private TextView tv_btn_delete;
        private TextView tv_btn_edit;
        private TextView tv_title;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_user = (ImageView) itemView.findViewById(R.id.iv_review_user_img);
            tv_name = (TextView) itemView.findViewById(R.id.tv_rv_review_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_rv_review_date);
            rb = (RatingBar) itemView.findViewById(R.id.rb_rv_review_score);
            tv_text = (TextView) itemView.findViewById(R.id.tv_rv_review_text);
            tv_btn_delete = (TextView) itemView.findViewById(R.id.tv_btn_delete);
            tv_btn_edit = (TextView) itemView.findViewById(R.id.tv_btn_edit);
            tv_title = (TextView) itemView.findViewById(R.id.tv_rv_review_title);

            tv_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(myDeleteListener != null){
                        myDeleteListener.whenDeleteClick(pos);
                    }
                }
            });

            tv_btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(myEditListener != null){
                        myEditListener.whenEditClick(pos);
                    }
                }
            });

        }
    }
}
