package com.example.techstore.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Enum.ActionType;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.OrderStatus;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class OrdersCompletedAdapter extends RecyclerView.Adapter<OrdersCompletedAdapter.OrdersCompleteViewHolder>{
    Context context;
    List<ProductOrders> list;
    OnClickWidgetItem listener;

    public OrdersCompletedAdapter(Context context, List<ProductOrders> list, OnClickWidgetItem listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersCompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_completed, parent, false);
        return new OrdersCompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersCompleteViewHolder holder, int position) {
        ProductOrders productOrders = list.get(position);

        List<OrderStatus> statusList = productOrders.getOrdersStatus();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        OrderStatus pendingStatus = productOrders.getOrdersStatus().get(0);
        LocalDateTime pendingTime = LocalDateTime.parse(pendingStatus.getTimestamp(), formatter);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(pendingTime, now);
        long hours = duration.toHours();
        if (hours >= 1 && !containsStatus(statusList, context.getString(R.string.status_confirmed))) {
            statusList.add(new OrderStatus(context.getString(R.string.status_confirmed), pendingTime.plusHours(1).format(formatter)));
        }
        if (hours >= 2 && !containsStatus(statusList, context.getString(R.string.status_shipping))) {
            statusList.add(new OrderStatus(context.getString(R.string.status_shipping), pendingTime.plusHours(2).format(formatter)));
        }
        if (hours >= 12 && !containsStatus(statusList, context.getString(R.string.status_delivered))) {
            statusList.add(new OrderStatus(context.getString(R.string.status_delivered), pendingTime.plusHours(12).format(formatter)));
        }

        Collections.sort(statusList, (s1, s2) -> {
            LocalDateTime t1 = LocalDateTime.parse(s1.getTimestamp(), formatter);
            LocalDateTime t2 = LocalDateTime.parse(s2.getTimestamp(), formatter);
            return t2.compareTo(t1);
        });

        String status = statusList.get(0).getStatus();
        holder.tvStatus.setText(status);

        List<ProductInCart> products = productOrders.getProducts();
        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        OnCompletedAdapter onCompletedAdapter = new OnCompletedAdapter(context, products);
        holder.rvProduct.setAdapter(onCompletedAdapter);
        holder.ctrViewStatus.setOnClickListener(click -> {
            if (listener!= null) {
                listener.onClick(position, ActionType.VIEW_STATUS);
            }
        });
    }

    private boolean containsStatus(List<OrderStatus> list, String status) {
        for (OrderStatus s : list) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrdersCompleteViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvProduct;
        ConstraintLayout ctrViewStatus;
        TextView tvRating, tvStatus;
        public OrdersCompleteViewHolder(@NonNull View itemView) {
            super(itemView);
            rvProduct = itemView.findViewById(R.id.rv_product);
            ctrViewStatus = itemView.findViewById(R.id.ctr_view_status);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
