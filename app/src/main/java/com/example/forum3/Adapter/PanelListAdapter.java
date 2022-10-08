package com.example.forum3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PanelListAdapter extends RecyclerView.Adapter<PanelListAdapter.MyViewHolder> {
    Context context;
    JSONArray Content;

    public PanelListAdapter(Context context, JSONArray content){
        this.context = context;
        this.Content = content;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_list_single_item, parent, false);
        return new PanelListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        try {
            JSONObject jsonObject = Content.getJSONObject(position);
            String panel_name = jsonObject.getString("panellist_name");
            viewHolder.panel_name_tv.setText(panel_name);

             /*if(!panel_name.equalsIgnoreCase("null")){
                viewHolder.panel_name_tv.setText(panel_name);
            }else{
                viewHolder.panel_name_tv.setText("No Panellist found");
            }*/

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Content.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView panel_name_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            panel_name_tv = itemView.findViewById(R.id.panel_name_tv);
        }
    }
}
