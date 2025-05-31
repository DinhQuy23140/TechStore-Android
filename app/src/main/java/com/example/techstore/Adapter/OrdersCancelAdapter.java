package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersCancelAdapter extends RecyclerView.Adapter<OrdersCancelAdapter.OrdersCancelViewHolder> {
    Context context;
    List<ProductOrders> listCancelOrders;
    OnClickWidgetItem listener;

    public OrdersCancelAdapter(Context context, List<ProductOrders> listCancelOrders, OnClickWidgetItem listener) {
        this.context = context;
        this.listCancelOrders = listCancelOrders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersCancelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_orders, parent, false);
        return new OrdersCancelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersCancelViewHolder holder, int position) {
        ProductOrders productOrders = listCancelOrders.get(position);

        List<OrderStatus> statusList = new ArrayList<>();
        for (OrderStatus status : productOrders.getOrdersStatus()) {
            statusList.add(new OrderStatus(status.getStatus(), status.getTimestamp()));
        }
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
        holder.tvTitleStatus.setText(status);

        List<ProductInCart> products = productOrders.getProducts();
        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        OnCompletedAdapter onCompletedAdapter = new OnCompletedAdapter(context, products);
        holder.rvProduct.setAdapter(onCompletedAdapter);
        holder.ctrViewStatus.setOnClickListener(click -> {
            if (listener!= null) {
                listener.onClick(position, ActionType.VIEW_STATUS);
            }
        });

        holder.btnBuyBack.setOnClickListener( buyBack -> {
            if (listener != null) {
                listener.onClick(position, ActionType.BUYBACK);
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
        return listCancelOrders.size();
    }

    public class OrdersCancelViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvProduct;
        ConstraintLayout ctrViewStatus;
        TextView tvStatus, tvTitleStatus;
        Button btnBuyBack;

        public OrdersCancelViewHolder(@NonNull View itemView) {
            super(itemView);
            rvProduct = itemView.findViewById(R.id.rv_product);
            ctrViewStatus = itemView.findViewById(R.id.ctr_view_status);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTitleStatus = itemView.findViewById(R.id.tv_title_status);
            btnBuyBack = itemView.findViewById(R.id.btn_buy_back);
        }
    }
}
