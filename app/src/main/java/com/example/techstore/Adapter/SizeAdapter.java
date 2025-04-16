package com.example.techstore.Adapter;

import android.annotation.SuppressLint;
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
import com.example.techstore.interfaces.OnItemClickListener;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder>{
    Context context;
    List<String> listSize;
    OnItemClickListener listener;
    private int selectedItem = -1;

    public SizeAdapter(Context context, List<String> listSize, OnItemClickListener listener) {
        this.context = context;
        this.listSize = listSize;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String size = listSize.get(position);
        holder.tvSize.setText(size);

        if (selectedItem == position) {
            holder.cvSize.setCardBackgroundColor(context.getResources().getColor(R.color.gray, null));
        } else {
            holder.cvSize.setCardBackgroundColor(context.getResources().getColor(R.color.white, null));
        }

        holder.itemView.setOnClickListener(click -> {
//            holder.cvSize.setCardBackgroundColor(context.getResources().getColor(R.color.gray, null));
            if (selectedItem == position) {
                int previoutPosition = selectedItem;
                selectedItem = -1;
                notifyItemChanged(previoutPosition);
            } else {
                int previoutPosition = selectedItem;
                selectedItem = position;
                notifyItemChanged(previoutPosition);
                notifyItemChanged(selectedItem);
            }
        });

        if (listener != null) {
            listener.onItemClick(position);
        }
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
