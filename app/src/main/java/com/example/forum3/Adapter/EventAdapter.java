package com.example.forum3.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.R;
//import com.runtime.hforlife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventAdapter extends RecyclerView.Adapter< EventAdapter.MyViewHolder>{

    String[] number,name,time;
    Context context;
    private View view;
    private JSONArray data;

    public EventAdapter(Context context, JSONArray data ){
        this.context = context;
        this.data = data;

    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_single_item, parent, false);
        return new MyViewHolder( view);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView panel_title_tv,session_time_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            //session_number_tv = itemView.findViewById( R.id.session_number_tv );
            panel_title_tv = itemView.findViewById( R.id.panel_title_tv );
            session_time_tv = itemView.findViewById( R.id.session_time_tv );

        }

    }
    @Override
    public void onBindViewHolder( MyViewHolder viewHolder, final int position) {

        try {
            JSONObject jsonObject = data.getJSONObject(position);
            String session_agendaid = jsonObject.getString("session_agendaid");
            String session_title = jsonObject.getString("session_title");
            String session_time = jsonObject.getString("session_time");
            //viewHolder.session_number_tv.setText("Session" + " " + session_agendaid);
            viewHolder.panel_title_tv.setText(session_title);
            viewHolder.session_time_tv.setText(session_time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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
