package com.example.forum3.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
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

public class AgendaListAdapter extends RecyclerView.Adapter <AgendaListAdapter.MyViewHolder> {

    String[] name;
    Context context;
    private View view;
    JSONArray Content;

    public AgendaListAdapter(Context context, JSONArray content){
        this.context = context;
        this.Content = content;
    }

    @NonNull
    @Override
    public AgendaListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_list_single_item, parent, false);
        return new AgendaListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaListAdapter.MyViewHolder viewHolder, int position) {
        try{
            JSONObject jsonObject = Content.getJSONObject(position);
            String agenda_category = jsonObject.getString("agenda_category");
            String agenda_day = jsonObject.getString("agenda_day");
            String agenda_date = jsonObject.getString("agenda_date");
//            String pic = jsonObject.getString("http_url") + jsonObject.getString("agenda_image");
//            viewHolder.agenda_tv.setText(agenda_category);
            viewHolder.agenda_day_tv.setText(agenda_day);
            viewHolder.agenda_date_tv.setText(agenda_date);
//            Picasso.get().load(pic).into(viewHolder.agenda_pic_iv);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Content.length();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView agenda_day_tv, agenda_date_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            agenda_day_tv = itemView.findViewById(R.id.agenda_day_tv);
            agenda_date_tv = itemView.findViewById(R.id.agenda_date_tv);

        }
    }

    public interface ClickListener {
        View onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private AgendaListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AgendaListAdapter.ClickListener clickListener) {
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
