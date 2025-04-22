package com.example.techstore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.ProductInCart;

import java.util.List;

public class OnGoingAdapter extends RecyclerView.Adapter<OnGoingAdapter.OnGoingViewHolder> {
    Context context;
    List<ProductInCart> list;
    OnClickWidgetItem onClickWidgetItem;

    public OnGoingAdapter(Context context, List<ProductInCart> list, OnClickWidgetItem onClickWidgetItem) {
        this.context = context;
        this.list = list;
        this.onClickWidgetItem = onClickWidgetItem;
    }

    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_item, parent, false);
        return new OnGoingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OnGoingViewHolder holder, int position) {
        ProductInCart product = list.get(position);
        Glide.with(holder.itemView.getContext())
                .load(product.getImg())
                .error(R.drawable.background_error_load)
                .placeholder(R.drawable.background_image_default)
                .into(holder.ivImg);
        holder.tvTitle.setText(product.getTitle());
        holder.tvSize.setText("Size = " + product.getSize());
        holder.tvQuantity.setText("Qty = " + product.getQuantity());
        holder.tvStatus.setText("Status");
        holder.tvPrice.setText(product.getPrice() + "$");
        holder.cvColor.setCardBackgroundColor(product.getColor());
        holder.btnTrack.setOnClickListener(track -> {
            onClickWidgetItem.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OnGoingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        CardView cvColor;
        TextView tvTitle, tvSize, tvQuantity, tvStatus, tvPrice, btnTrack;
        public OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img_product);
            cvColor = itemView.findViewById(R.id.cv_color_product);
            tvTitle = itemView.findViewById(R.id.tv_cart_title);
            tvSize = itemView.findViewById(R.id.tv_size_product);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPrice = itemView.findViewById(R.id.tv_price_total);
            btnTrack = itemView.findViewById(R.id.btn_track_order);
        }
    }
}
