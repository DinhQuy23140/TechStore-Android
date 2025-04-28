package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.model.OrderStatus;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrderStatusViewHolder> {
    Context context;
    List<OrderStatus> orderStatuses;

    public OrderStatusAdapter(Context context, List<OrderStatus> orderStatuses) {
        this.context = context;
        this.orderStatuses = orderStatuses;
    }

    @NonNull
    @Override
    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_status, parent, false);
        return new OrderStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusViewHolder holder, int position) {
        OrderStatus status = orderStatuses.get(position);
        holder.tvStatusName.setText(status.getStatus());
        holder.tvStatusTime.setText(status.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return orderStatuses.size();
    }

    public class OrderStatusViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatusName, tvStatusTime;
        ImageView imgStatusIcon;
        public OrderStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatusName = itemView.findViewById(R.id.tv_status_name);
            tvStatusTime = itemView.findViewById(R.id.tv_status_time);
            imgStatusIcon = itemView.findViewById(R.id.img_status_icon);
        }
    }
}
