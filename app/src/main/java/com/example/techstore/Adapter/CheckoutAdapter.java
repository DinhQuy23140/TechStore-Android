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

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.model.ProductInCart;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    Context context;
    List<ProductInCart> listProduct;

    public CheckoutAdapter(Context context, List<ProductInCart> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new CheckoutViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        ProductInCart productInCart = listProduct.get(position);
        String image = productInCart.getImg();
        Glide.with(holder.itemView.getContext())
                .load(image)
                .into(holder.ivImg);

        holder.tvTitle.setText(productInCart.getTitle());
        holder.tvSize.setText("Size = " + productInCart.getSize());
        holder.tvQuantity.setText(Integer.toString(productInCart.getQuantity()));
        float pricePro = productInCart.getQuantity() * productInCart.getPrice();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.tvPrice.setText(decimalFormat.format(pricePro) + "$");
        holder.cvColor.setCardBackgroundColor(productInCart.getColor());
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        CardView cvColor;
        TextView tvTitle, tvSize, tvPrice, tvQuantity;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img_product);
            cvColor = itemView.findViewById(R.id.cv_color_product);
            tvTitle = itemView.findViewById(R.id.tv_cart_title);
            tvSize = itemView.findViewById(R.id.tv_size_product);
            tvPrice = itemView.findViewById(R.id.tv_price_total);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
