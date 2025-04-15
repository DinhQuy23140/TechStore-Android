package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder>{
    Context context;
    List<String> listSize;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String size = listSize.get(position);
        holder.tvSize.setText(size);
    }

    @Override
    public int getItemCount() {
        return listSize.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvSize;
        TextView tvSize;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSize = itemView.findViewById(R.id.cv_color);
            tvSize = itemView.findViewById(R.id.tv_size);
        }
    }
}
