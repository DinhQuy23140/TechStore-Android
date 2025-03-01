package com.example.techstore.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewholder> {
    Context context;
    ArrayList<String> listFilter;

    public FilterAdapter(Context context, ArrayList<String> listFilter) {
        this.context = context;
        this.listFilter = listFilter;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.tvFilter.setText(listFilter.get(position));
    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView tvFilter;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvFilter = itemView.findViewById(R.id.item_tv_filter);
        }
    }
}
