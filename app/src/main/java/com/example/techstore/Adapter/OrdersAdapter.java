package com.example.techstore.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.techstore.untilities.Constants;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    Context context;
    List<ProductOrders> listOrders;
    OnClickWidgetItem listener;

    public OrdersAdapter(Context context, List<ProductOrders> listOrders, OnClickWidgetItem listener) {
        this.context = context;
        this.listOrders = listOrders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        ProductOrders productOrders = listOrders.get(position);

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
        if (status.equals(context.getString(R.string.status_pending))) {
            holder.tvCancel.setVisibility(View.VISIBLE);
            holder.tvSubmit.setVisibility(View.GONE);
        } else if (status.equals(context.getString(R.string.status_delivered))) {
            holder.tvCancel.setVisibility(View.GONE);
            holder.tvSubmit.setVisibility(View.VISIBLE);
        } else {
            holder.tvCancel.setVisibility(View.GONE);
            holder.tvSubmit.setVisibility(View.GONE);
        }

        List<ProductInCart> products = productOrders.getProducts();
        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        OnCompletedAdapter onCompletedAdapter = new OnCompletedAdapter(context, products);
        holder.rvProduct.setAdapter(onCompletedAdapter);
        holder.ctrViewStatus.setOnClickListener(click -> {
            if (listener!= null) {
                listener.onClick(position, ActionType.VIEW_STATUS);
            }
        });

        holder.tvSubmit.setOnClickListener(submit -> {
            if (listener != null) {
                listener.onClick(position, ActionType.SUBMIT);
            }
        });

        holder.tvCancel.setOnClickListener(cancel -> {
            if (listener != null) {
                listener.onClick(position, ActionType.CANCEL);
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
        return listOrders.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvProduct;
        ConstraintLayout ctrViewStatus;
        TextView tvStatus, tvTitleStatus;
        Button tvCancel, tvSubmit;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            rvProduct = itemView.findViewById(R.id.rv_product);
            ctrViewStatus = itemView.findViewById(R.id.ctr_view_status);
            tvCancel = itemView.findViewById(R.id.tv_cancel);
            tvSubmit = itemView.findViewById(R.id.tv_submit);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTitleStatus = itemView.findViewById(R.id.tv_title_status);
        }
    }
}
