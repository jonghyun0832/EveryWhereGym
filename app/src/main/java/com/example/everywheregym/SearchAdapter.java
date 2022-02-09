package com.example.everywheregym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private VodAdapter.myRecyclerViewClickListener myListener;

    public interface myRecyclerViewDeleteClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenDeleteClick(int position);
    }

    private myRecyclerViewDeleteClickListener myDeleteListener;

    public void setOnDeleteClickListener(myRecyclerViewDeleteClickListener listener){
        myDeleteListener = listener;
    }

    public void setOnClickListener(VodAdapter.myRecyclerViewClickListener listener){
        myListener = listener;
    }


    private ArrayList<SearchData> arrayList;
    private Context context;

    public SearchAdapter(ArrayList<SearchData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setArrayList (ArrayList<SearchData> arrayList){
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_item,parent,false);
        SearchAdapter.SearchViewHolder holder = new SearchAdapter.SearchViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.tv_search_text.setText(arrayList.get(position).getSearch_text());
        holder.tv_search_date.setText(arrayList.get(position).getSearch_date());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_search_text;
        public TextView tv_search_date;
        public ImageView iv_delete;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_search_text = itemView.findViewById(R.id.rv_tv_search_history);
            tv_search_date = itemView.findViewById(R.id.rv_tv_search_date);
            iv_delete = itemView.findViewById(R.id.rv_iv_search_delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        myListener.whenItemClick(pos);
                    }
                }
            });

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAbsoluteAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        myDeleteListener.whenDeleteClick(pos);
                    }
                }
            });

        }
    }
}
