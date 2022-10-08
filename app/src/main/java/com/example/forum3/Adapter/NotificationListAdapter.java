package com.example.forum3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.Activity.NotificationListActivity;
import com.example.forum3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationListAdapter extends RecyclerView.Adapter< NotificationListAdapter.MyViewHolder> {

    String[] name, stage, time;
    JSONArray notification_data;
    Context context;
    private View view;

    public NotificationListAdapter(Context context, JSONArray notification_data){
        this.context = context;
        this.notification_data = notification_data;

    }

    @NonNull
    @Override
    public NotificationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_single_item, parent, false);
        return new NotificationListAdapter.MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView notification_image_iv;
        TextView notification_title_tv, notification_session_tv, notification_time_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_image_iv = itemView.findViewById(R.id.notification_image_iv);
            notification_title_tv = itemView.findViewById(R.id.notification_title_tv);
            notification_session_tv = itemView.findViewById(R.id.notification_session_tv);
            notification_time_tv = itemView.findViewById(R.id.notification_time_tv);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.MyViewHolder holder, int position) {

        try {
            JSONObject jsonObject = notification_data.getJSONObject(position);
            String notification_section = jsonObject.getString("notification_section");
            holder.notification_title_tv.setText(jsonObject.getString("notification_title"));
            holder.notification_session_tv.setText(jsonObject.getString("notification_subtitle"));
            holder.notification_time_tv.setText(jsonObject.getString("time_ago"));
            if (notification_section.equalsIgnoreCase("Video")){
                holder.notification_image_iv.setImageResource(R.drawable.video_img);
            } else if(notification_section.equalsIgnoreCase("Agenda")){
                holder.notification_image_iv.setImageResource(R.drawable.agenda_img);
            } else if(notification_section.equalsIgnoreCase("Resources")){
                holder.notification_image_iv.setImageResource(R.drawable.resources_img);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return notification_data.length();
    }

}
