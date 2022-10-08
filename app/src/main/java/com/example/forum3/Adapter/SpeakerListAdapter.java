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

//import com.runtime.hforlife.R;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpeakerListAdapter extends RecyclerView.Adapter< SpeakerListAdapter.MyViewHolder>{

    Context context;
    private View view;
    private JSONArray Content;


    public SpeakerListAdapter(Context context, JSONArray Content){
        this.context = context;
        this.Content = Content;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.speaker_single_item, parent, false);
        return new MyViewHolder( view);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView speaker_image_iv;
        TextView speaker_name_tv,speaker_designation_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            speaker_image_iv = (ImageView) itemView.findViewById(R.id.speaker_image_iv);
            speaker_name_tv = itemView.findViewById( R.id.speaker_name_tv );
            speaker_designation_tv = itemView.findViewById( R.id.speaker_designation_tv );

        }

    }
    @Override
    public void onBindViewHolder( MyViewHolder viewHolder, final int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            String  img_url = ApiUrl.IMG_BASE_URL + data.getString("profile_pic");
            Picasso.get().load(img_url).into(viewHolder.speaker_image_iv);
            //Picasso.get().setLoggingEnabled(true);
            viewHolder.speaker_name_tv.setText(data.getString("name"));
            viewHolder.speaker_designation_tv.setText(data.getString("company_name"));
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
