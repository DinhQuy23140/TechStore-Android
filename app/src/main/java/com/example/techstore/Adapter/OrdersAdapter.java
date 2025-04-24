package com.example.techstore.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.untilities.Constants;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    Context context;
    List<ProductOrders> listOrders;

    public OrdersAdapter(Context context, List<ProductOrders> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
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
        List<ProductInCart> products = productOrders.getProducts();
        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        OnCompletedAdapter onCompletedAdapter = new OnCompletedAdapter(context, products);
        holder.rvProduct.setAdapter(onCompletedAdapter);
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvProduct;
        ConstraintLayout ctrViewStatus;
        TextView tvRating;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            rvProduct = itemView.findViewById(R.id.rv_product);
            ctrViewStatus = itemView.findViewById(R.id.ctr_view_status);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }
    }
}
