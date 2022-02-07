package com.example.everywheregym;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VodHistoryAdapter extends RecyclerView.Adapter<VodHistoryAdapter.VodHistoryViewHolder>{

    public interface vodHistoryAdapterClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenItemClick(int position, int positions);
    }

    public interface vodHistoryAdapterMoreClickListener{
        //내가누른 아이템의 포지션을 외부에서 알수있게 전달하겠다
        void whenMoreClick(int position, int positions);
    }

    private VodHistoryAdapter.vodHistoryAdapterClickListener myListener;
    private VodHistoryAdapter.vodHistoryAdapterMoreClickListener myMoreListener;

    public void setOnClickListener(VodHistoryAdapter.vodHistoryAdapterClickListener listener){
        myListener = listener;
    }

    public void setOnMoreClickListener(VodHistoryAdapter.vodHistoryAdapterMoreClickListener listener){
        myMoreListener = listener;
    }

    private ArrayList<VodHistoryData> arrayList; //VodData 바꿔줘야함
    private Context context;

    public VodHistoryAdapter(ArrayList<VodHistoryData> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    //두개 겹침
    public void setArrayList(ArrayList<VodHistoryData> arrayList){
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public VodHistoryAdapter.VodHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_sub,parent,false);
        VodHistoryViewHolder holder = new VodHistoryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VodHistoryAdapter.VodHistoryViewHolder holder, int position) {
        holder.tv_history_date.setText(arrayList.get(position).getDate());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rv_history_sub.getContext());

        VodUploaderAdapter vodUploaderAdapter = new VodUploaderAdapter(arrayList.get(position).getVodData(),holder.rv_history_sub.getContext());
        holder.rv_history_sub.setLayoutManager(layoutManager);
        holder.rv_history_sub.setAdapter(vodUploaderAdapter);

        vodUploaderAdapter.setOnClickListener(new VodUploaderAdapter.vodUploaderAdapterClickListener() {
            @Override
            public void whenItemClick(int positions) {
                holder.get_positions = positions;
                final int pos = holder.getAbsoluteAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    myListener.whenItemClick(pos,positions);
                }


            }
        });

        vodUploaderAdapter.setOnMoreClickListener(new VodUploaderAdapter.vodUploaderAdapterMoreClickListener() {
            @Override
            public void whenMoreClick(int positions) {
                holder.get_positions = positions;
                final int pos = holder.getAbsoluteAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    myMoreListener.whenMoreClick(pos,positions);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class VodHistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_history_date;
        public RecyclerView rv_history_sub;
        public int get_positions;


        public VodHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_history_date = itemView.findViewById(R.id.tv_history_sub_date);
            rv_history_sub = itemView.findViewById(R.id.rv_history_sub);

        }
    }
}
