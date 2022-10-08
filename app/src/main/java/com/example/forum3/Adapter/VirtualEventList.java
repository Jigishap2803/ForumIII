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

import org.json.JSONArray;

public class VirtualEventList extends RecyclerView.Adapter <VirtualEventList.MyViewHolder> {
    String[] stream_type, name;
    int[] image;
    Context context;
    View view;

    public VirtualEventList(Context context, String[] stream_type, String[] name, int[] image){
        this.context = context;
        this.stream_type = stream_type;
        this.name = name;
        this.image = image;
    }

    @NonNull
    @Override
    public VirtualEventList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.virtual_event_single_item, parent, false);
        return new VirtualEventList.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VirtualEventList.MyViewHolder viewHolder, int position) {
        viewHolder.event_name_tv.setText(name[position]);
        viewHolder.event_type_tv.setText(stream_type[position]);
        viewHolder.event_image_iv.setImageResource(image[position]);


    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView event_image_iv;
        TextView event_name_tv, event_type_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            event_image_iv = itemView.findViewById(R.id.event_image_iv);
            event_name_tv = itemView.findViewById(R.id.event_name_tv);
            event_type_tv = itemView.findViewById(R.id.event_type_tv);
        }
    }

    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private VirtualEventList.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final VirtualEventList.ClickListener clickListener) {
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
