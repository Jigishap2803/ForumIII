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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionAdapter extends RecyclerView.Adapter <SessionAdapter.MyViewHolder> {

    String[] name;
    Context context;
    private View view;
    JSONArray Content;

    public SessionAdapter(Context context, JSONArray content){
        this.context = context;
        this.Content = content;

    }
    @NonNull
    @Override
    public SessionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_session_single_item, parent, false);
        return new SessionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionAdapter.MyViewHolder viewHolder, int position) {
        try {
            JSONObject jsonObject = Content.getJSONObject(position);
            String session_time = jsonObject.getString("session_time");
            String session_title = jsonObject.getString("session_title");
            viewHolder.agenda_session_title_tv.setText(session_title);
            viewHolder.agenda_session_time_tv.setText(session_time);

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView agenda_session_time_tv, agenda_session_title_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            agenda_session_time_tv = itemView.findViewById(R.id.agenda_session_time_tv);
            agenda_session_title_tv = itemView.findViewById(R.id.agenda_session_title_tv);
        }
    }

    public interface ClickListener {
        View onClick(View view, int position);

        Void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private SessionAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SessionAdapter.ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                   View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                   if (child != null && clickListener != null){
                       clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                   }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
