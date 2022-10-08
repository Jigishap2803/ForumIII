package com.example.forum3.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.Activity.HotelGalleryModel;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotelIfoAdapter extends RecyclerView.Adapter <HotelIfoAdapter.ViewHolder> {
    private JSONArray data;
    Context context;

    public HotelIfoAdapter(Context context, JSONArray data){
        this.context = context;
        this.data = data;

    }
    @NonNull
    @Override
    public HotelIfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_gallery_single_item, parent, false);
        return new HotelIfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelIfoAdapter.ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = data.getJSONObject(position);
            String hotelgallery_pic = ApiUrl.IMG_BASE_URL + jsonObject.getString("hotelgallery_pic");
            Picasso.get().load(hotelgallery_pic).into(holder.gallery_pic);

        } catch (JSONException e){
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gallery_pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gallery_pic = itemView.findViewById(R.id.gallery_pic);

        }
    }

    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HotelIfoAdapter.ClickListener clickListener) {
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

