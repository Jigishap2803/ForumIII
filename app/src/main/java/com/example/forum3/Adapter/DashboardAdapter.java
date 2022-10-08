package com.example.forum3.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.R;
//import com.runtime.hforlife.R;

public class DashboardAdapter extends RecyclerView.Adapter< DashboardAdapter.MyViewHolder>{

    String[] name;
    int[] icon;
    Context context;
    private View View;


    public DashboardAdapter(Context context, String[] name, int[] icon){
        this.context = context;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item, parent, false);
        return new MyViewHolder( view);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView list_iv;
        TextView list_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            list_iv = (ImageView) itemView.findViewById(R.id.list_iv);
            list_tv = itemView.findViewById( R.id.list_tv );

        }

    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        viewHolder.list_iv.setImageResource(icon[position]);
        viewHolder.list_tv.setText(name[position]);
    }

    @Override
    public int getItemCount() {
        return icon.length;
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
