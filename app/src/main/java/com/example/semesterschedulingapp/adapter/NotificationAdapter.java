package com.example.semesterschedulingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.model.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_cardview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        holder.task_title.setText(notificationModelList.get(position).getTask_title());
        holder.task_details.setText(notificationModelList.get(position).getTask_details());
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView task_title;
        TextView task_details;

        public ViewHolder(@NonNull View view) {
            super(view);

            task_title = view.findViewById(R.id.tv_task_title);
            task_details = view.findViewById(R.id.tv_task_detail);
        }
    }
}
