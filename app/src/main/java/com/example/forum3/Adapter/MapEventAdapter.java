package com.example.forum3.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.R;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapEventAdapter extends RecyclerView.Adapter <MapEventAdapter.MyViewHolder> {
    String[] name;
    Context context;
    private JSONArray data;

    public MapEventAdapter(Context context, JSONArray data){
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MapEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_event_single_item, parent, false);
        return new MapEventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapEventAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject jsonObject = data.getJSONObject(position);
            String agenda_category = jsonObject.getString("agenda_category");
            String pic = jsonObject.getString("http_url") + jsonObject.getString("agenda_image");
            holder.map_event_title_tv.setText(agenda_category);
            Picasso.get().load(pic).into(holder.map_event_iv);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView map_event_iv;
        private TextView map_event_title_tv, map_event_address_tv, map_event_number_tv, map_event_web_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            map_event_iv = itemView.findViewById(R.id.map_event_iv);
            map_event_title_tv = itemView.findViewById(R.id.map_event_title_tv);
            map_event_number_tv = itemView.findViewById(R.id.map_event_number_tv);
            map_event_address_tv = itemView.findViewById(R.id.map_event_address_tv);
            map_event_web_tv = itemView.findViewById(R.id.map_event_web_tv);
        }
    }

    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MapEventAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MapEventAdapter.ClickListener clickListener) {
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
