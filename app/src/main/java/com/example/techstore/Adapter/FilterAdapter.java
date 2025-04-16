package com.example.techstore.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewholder> {
    Context context;
    ArrayList<String> listFilter;
    OnItemClickListener onItemClickListener;

    public FilterAdapter(Context context, ArrayList<String> listFilter, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listFilter = listFilter;
        this.onItemClickListener = onItemClickListener;
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
        holder.itemView.setOnClickListener(filter -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
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
