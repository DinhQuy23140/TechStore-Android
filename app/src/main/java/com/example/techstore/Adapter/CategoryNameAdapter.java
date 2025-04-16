package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class CategoryNameAdapter extends RecyclerView.Adapter<CategoryNameAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> listCategory;
    OnItemClickListener listener;

    public CategoryNameAdapter(Context context, ArrayList<String> listCategory, OnItemClickListener listener) {
        this.context = context;
        this.listCategory = listCategory;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_name, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String categoryName = listCategory.get(position);
        holder.categoryName.setText(categoryName);
        holder.itemView.setOnClickListener(click-> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
