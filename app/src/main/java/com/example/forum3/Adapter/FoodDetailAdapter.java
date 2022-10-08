package com.example.forum3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.Activity.FoodModel;
import com.example.forum3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FoodDetailAdapter extends RecyclerView.Adapter <FoodDetailAdapter.ViewHolder> {


    JSONArray Content;
//    String[] data;
    Context context;
    Boolean isVisible = true;

    public FoodDetailAdapter(Context context, JSONArray content){
        this.context = context;
        this.Content = content;

    }

//    public FoodDetailAdapter(String[] data1) {
//        this.data = data1;
//    }

    @NonNull
    @Override
    public FoodDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_detail_single_item, parent, false);
        return new FoodDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDetailAdapter.ViewHolder viewHolder, int position) {
        try {
            JSONObject data = Content.getJSONObject(position);
            String food_date = data.getString("food_date");
            String food_category = data.getString("food_category");
            String food_description = data.getString("food_description");
            viewHolder.food_date_tv.setText(food_date);
            viewHolder.menu_category_tv.setText(food_category);
            viewHolder.food_desc_tv.setText(food_description);

            viewHolder.food_content_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.food_content_cv.setSelected(!viewHolder.food_content_cv.isSelected());
                    if (viewHolder.food_content_cv.isSelected()){
                        viewHolder.desc_ll.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.desc_ll.setVisibility(View.GONE);

                    }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView food_date_tv, menu_category_tv, food_desc_tv;
        CardView food_content_cv;
        LinearLayout desc_ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            food_date_tv = itemView.findViewById(R.id.food_date_tv);
            desc_ll = itemView.findViewById(R.id.desc_ll);
            food_content_cv = itemView.findViewById(R.id.food_content_cv);
            menu_category_tv = itemView.findViewById(R.id.menu_category_tv);
            food_desc_tv = itemView.findViewById(R.id.food_desc_tv);

        }
    }
}
