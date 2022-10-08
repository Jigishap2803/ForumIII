package com.example.forum3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.R;
//import com.runtime.hforlife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventTabAdapter extends RecyclerView.Adapter< EventTabAdapter.MyViewHolder>{

    Context context;
    private View view;
    private JSONArray Content;
    private int row_index = -1;


    public EventTabAdapter(Context context, JSONArray Content){
        this.context = context;
        this.Content = Content;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_tab_single_item, parent, false);
        return new MyViewHolder( view);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout event_tab_ll;
        TextView event_tab_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            event_tab_ll = itemView.findViewById(R.id.event_tab_ll);
            event_tab_tv = itemView.findViewById(R.id.event_tab_tv);

        }

    }
    @Override
    public void onBindViewHolder( MyViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            String event_tab_title =  data.getString("eventtabs_title");
            viewHolder.event_tab_tv.setText(event_tab_title);

            if(position==0){
                viewHolder.event_tab_ll.setBackground(context.getResources().getDrawable(R.drawable.filled_btn_background));
                viewHolder.event_tab_tv.setTextColor( Color.parseColor("#ffffff"));
            }

            viewHolder.event_tab_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position;
                    notifyDataSetChanged();

                }
            });

            if(row_index==position){
                viewHolder.event_tab_ll.setBackground(context.getResources().getDrawable(R.drawable.filled_btn_background));
                viewHolder.event_tab_tv.setTextColor( Color.parseColor("#ffffff"));
            }
            else {
                viewHolder.event_tab_ll.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.event_tab_tv.setTextColor(Color.parseColor("#168B49"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Content.length();
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
