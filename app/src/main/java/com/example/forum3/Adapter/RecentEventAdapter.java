package com.example.forum3.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecentEventAdapter extends RecyclerView.Adapter <RecentEventAdapter.MyViewHolder> {
    Context context;
    private View view;
    JSONArray Content;

    public RecentEventAdapter(Context context, JSONArray content){
        this.context = context;
        this.Content = content;
    }

    @NonNull
    @Override
    public RecentEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_event_list_item, parent, false);
        return new RecentEventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentEventAdapter.MyViewHolder viewHolder, int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            String event_title = data.getString("event_title");
            String event_date = data.getString("event_date");
            String event_location = data.getString("event_location");
            viewHolder.event_date_tv.setText(event_date);
            viewHolder.event_title_tv.setText(event_title);
            viewHolder.event_location_tv.setText(event_location);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView event_title_tv, event_date_tv, event_location_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            event_title_tv = itemView.findViewById(R.id.event_title_tv);
            event_date_tv = itemView.findViewById(R.id.event_date_tv);
            event_location_tv = itemView.findViewById(R.id.event_location_tv);
        }
    }
    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RecentEventAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecentEventAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp( MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
