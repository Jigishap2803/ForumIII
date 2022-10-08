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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BlogAdapter extends RecyclerView.Adapter <BlogAdapter.MyViewHolder> {

    private View view;
    private JSONArray Content;
    private Context context;


    public BlogAdapter( Context context, JSONArray Content){
        this.context = context;
        this.Content = Content;
    }


    @NonNull
    @Override
    public BlogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_single_item, parent, false);
        return new BlogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.MyViewHolder viewHolder, int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            String  blog_topic = data.getString("blog_topic");
            String  blog_category = data.getString("blog_category");
            String  blog_date = data.getString("blog_date");
            String  blog_image = data.getString("http_url") + data.getString("blog_image");
            viewHolder.blog_title_tv.setText(blog_topic);
            viewHolder.blog_date_tv.setText(blog_date);
            viewHolder.blog_category_tv.setText(blog_category);
            Picasso.get().load(blog_image).into(viewHolder.blog_image_iv);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView blog_title_tv,blog_date_tv,blog_category_tv;
        ImageView blog_image_iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            blog_title_tv = itemView.findViewById(R.id.blog_title_tv);
            blog_date_tv = itemView.findViewById( R.id.blog_date_tv );
            blog_category_tv = itemView.findViewById( R.id.blog_category_tv );
            blog_image_iv = itemView.findViewById( R.id.blog_image_iv );
        }
    }
    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private BlogAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BlogAdapter.ClickListener clickListener) {
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
