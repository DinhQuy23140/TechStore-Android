package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techstore.R;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.MyViewHolder> {
    ArrayList<Integer> listPathImage;
    Context context;

    public CarouselAdapter(Context context, ArrayList<Integer> listImage) {
        this.context = context;
        this.listPathImage = listImage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carousel, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int image = listPathImage.get(position);
        Glide.with(holder.itemView.getContext())
                .load(image)
                .placeholder(R.drawable.background_image_default)
                .error(R.drawable.background_error_load)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (!listPathImage.isEmpty()) return listPathImage.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_iv_carousel);
        }
    }
}
