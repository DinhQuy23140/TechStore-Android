package com.example.techstore.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techstore.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<Integer> listPathImageCategory;
    ArrayList<String> listNameCategory;

    public CategoryAdapter(Context context, ArrayList<String> listNameCategory, ArrayList<Integer> listPathImageCategory) {
        context = context;
        this.listNameCategory = listNameCategory;
        this.listPathImageCategory = listPathImageCategory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int getIdImage = listPathImageCategory.get(position);
        String getNameCategory = listNameCategory.get(position);
        holder.tvCategory.setText(getNameCategory);
        Glide.with(holder.itemView.getContext())
                .load(getIdImage)
                .placeholder(R.drawable.baseline_category_24)
                .error(R.drawable.baseline_error_24)
                .into(holder.ivCategory);
    }

    @Override
    public int getItemCount() {
        return listNameCategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        ImageView ivCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.item_tv_nameCategory);
            ivCategory = itemView.findViewById(R.id.item_iv_imgCategory);
        }
    }
}
