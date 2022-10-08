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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResourcesListAdapter extends RecyclerView.Adapter <ResourcesListAdapter.MyViewHolder> {
    String[] name;
    Context context;
    private View view;
    private JSONArray Content;

    public ResourcesListAdapter(Context context, String[] name){
        this.context = context;
        this.name = name;
    }

    public ResourcesListAdapter(Context context, JSONArray content){
        this.context = context;
        this.Content = content;

    }
    @NonNull
    @Override
    public ResourcesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resources_single_item, parent, false);
        return new ResourcesListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesListAdapter.MyViewHolder viewHolder, int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            String resource_title = data.getString("resource_title");
            viewHolder.resources_name_tv.setText(resource_title);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView resources_name_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            resources_name_tv = itemView.findViewById(R.id.resources_name_tv);
        }
    }
    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ResourcesListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ResourcesListAdapter.ClickListener clickListener) {
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
