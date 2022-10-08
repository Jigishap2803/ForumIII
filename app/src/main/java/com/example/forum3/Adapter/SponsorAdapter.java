package com.example.forum3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
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

public class SponsorAdapter extends RecyclerView.Adapter <SponsorAdapter.MyViewHolder> {
    String[] name,desc;
    Context context;
    private View view;
    private JSONArray Content;
    private MyAdapterListener myAdapterListener;

    public SponsorAdapter(Context context, String[] name, String[] desc){
        this.context = context;
        this.name = name;
        this.desc = desc;

    }

    public SponsorAdapter(Context context, JSONArray content, MyAdapterListener myAdapterListener){
        this.context = context;
        this.Content = content;
        this.myAdapterListener = myAdapterListener;
    }

    @NonNull
    @Override
    public SponsorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsors_single_item, parent, false);
        return new SponsorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SponsorAdapter.MyViewHolder viewHolder, int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            //String  name = data.getString("name");
            String company_name = data.getString("company_name");
            String sponsor_logo = data.getString("http_url") + data.getString("sponsor_logo");
            String websiteLink = data.getString("websitelink");
            viewHolder.sponsor_name_tv.setText(company_name);
            //viewHolder.sponsor_desc_tv.setText(company_name);
            viewHolder.sponsor_url_tv.setText(websiteLink);
            Picasso.get().load(sponsor_logo).into(viewHolder.sponsor_img);

            viewHolder.sponsor_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapterListener.bannerOnClick(view, position);
                }
            });

            viewHolder.visit_web_icon_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapterListener.webOnClick(view, position);
                }
            });
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sponsor_name_tv, sponsor_desc_tv, sponsor_url_tv;
        ImageView sponsor_img, visit_web_icon_iv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sponsor_name_tv = itemView.findViewById(R.id.sponsor_name_tv);
            sponsor_desc_tv = itemView.findViewById(R.id.sponsor_desc_tv);
            sponsor_url_tv = itemView.findViewById(R.id.sponsor_url_tv);
            sponsor_img = itemView.findViewById(R.id.sponsor_img);
            visit_web_icon_iv = itemView.findViewById(R.id.visit_web_icon_iv);
        }
    }

    public interface MyAdapterListener {

        void bannerOnClick(View v, int position);

        void webOnClick(View v, int position);
    }
}
