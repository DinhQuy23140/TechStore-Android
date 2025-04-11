package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;

import java.util.List;

public class ColorAdapter extends RecyclerView .Adapter<ColorAdapter.MyViewHolder> {
    Context context;
    List<Integer> listColor;
    private int selectedPosition = -1;

    public ColorAdapter(Context context, List<Integer> listColor) {
        this.context = context;
        this.listColor = listColor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_color, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int color = listColor.get(position);
        holder.colorCV.setCardBackgroundColor(color);

        if (selectedPosition == position) {
            holder.colorIV.setVisibility(View.VISIBLE);
        } else {
            holder.colorIV.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(click -> {
            if (selectedPosition == holder.getAdapterPosition()) {
                int previousPosition = selectedPosition;
                selectedPosition = -1;
                notifyItemChanged(previousPosition);
            } else {
                int previoutPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previoutPosition);
                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listColor.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView colorCV;
        ImageView colorIV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCV = itemView.findViewById(R.id.colorCardView);
            colorIV = itemView.findViewById(R.id.colorImgView);
        }
    }
}
