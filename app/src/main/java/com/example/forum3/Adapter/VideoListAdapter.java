package com.example.forum3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoListAdapter extends RecyclerView.Adapter <VideoListAdapter.MyViewHolder> {

    String[] name,designation;
    int[] icon;
    Context context;
    private View view;
    private JSONArray data;
    private MyAdapterListener myAdapterListener;

    public VideoListAdapter(Context context, JSONArray data, MyAdapterListener myAdapterListener){
        this.context = context;
        this.data = data;
        this.myAdapterListener = myAdapterListener;

    }
    @NonNull
    @Override
    public VideoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_single_item, parent, false);
        return new VideoListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.MyViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject jsonObject = data.getJSONObject(position);
            String img_url = ApiUrl.IMG_BASE_URL + jsonObject.getString("video_thumbnail");
            Picasso.get().load(img_url).into(viewHolder.speaker_image_iv);
            viewHolder.speaker_name_tv.setText(jsonObject.getString("video_title"));
            //viewHolder.speaker_designation_tv.setText(jsonObject.getString("video_title"));

            viewHolder.speaker_image_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapterListener.bannerOnClick(view,position);
                }
            });

            /*viewHolder.save_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapterListener.saveOnClick(view,position);
                }
            });*/

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView speaker_image_iv,save_iv;
        TextView speaker_name_tv,speaker_designation_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            speaker_image_iv = (ImageView) itemView.findViewById(R.id.speaker_image_iv);
            //save_iv = (ImageView) itemView.findViewById(R.id.save_iv);
            speaker_name_tv = itemView.findViewById( R.id.speaker_name_tv );
            //speaker_designation_tv = itemView.findViewById( R.id.speaker_designation_tv );
        }
    }

    public interface MyAdapterListener {

        void bannerOnClick(View v, int position);

        void saveOnClick(View v, int position);
    }
}
