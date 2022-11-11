package com.example.kmj_reco.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.R;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ServiceAccount> ServiceAccount;
    public MyRecyclerViewAdapter(ArrayList<ServiceAccount> ServiceAccount) {
        this.ServiceAccount = ServiceAccount;
    }

    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onTitleClicked(int position);
        void onContentClicked(int position);

    }

    private MyRecyclerViewClickListener mListener;

    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_service_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ServiceAccount service = ServiceAccount.get(position);
        holder.title.setText(service.getService_Title());
        holder.content.setText(service.getService_Contents());

        if (mListener != null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(pos);
                }
            });
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTitleClicked(pos);
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onContentClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ServiceAccount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
        }
    }

    // 리스트 삭제 이벤트
    public void remove(int position){
        try {
            ServiceAccount.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}