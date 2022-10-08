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

public class TopicListAdapter extends RecyclerView.Adapter <TopicListAdapter.ViewHolder> {
    Context context;
    JSONArray Content;

    public TopicListAdapter(Context context, JSONArray content){
        this.context = context;
       this.Content = content;

    }
    @NonNull
    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_single_item, parent, false);
        return new TopicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicListAdapter.ViewHolder viewHolder, int position) {
        try {
            JSONObject jsonObject = Content.getJSONObject(position);
            String topic_name = jsonObject.getString("topic_name");
            viewHolder.topic_name_tv.setText(topic_name);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topic_name_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topic_name_tv = itemView.findViewById(R.id.topic_name_tv);
        }
    }

    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TopicListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TopicListAdapter.ClickListener clickListener) {
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
