package com.example.techstore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickProductInCart;
import com.example.techstore.model.ProductInCart;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<ProductInCart> listProductInCart;
    OnClickProductInCart onClickProductInCart;

    public CartAdapter(Context context, List<ProductInCart> listProductInCart, OnClickProductInCart onClickProductInCart) {
        this.context = context;
        this.listProductInCart = listProductInCart;
        this.onClickProductInCart = onClickProductInCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductInCart productInCart = listProductInCart.get(position);
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
        holder.btnDecrease.setOnClickListener(decrease -> {
            int quantity = productInCart.getQuantity();
            if (quantity > 0) {
                quantity --;
                BigDecimal quantityProduct = new BigDecimal(quantity);
                BigDecimal price = new BigDecimal(Float.toString(productInCart.getPrice()));
                BigDecimal total = quantityProduct.multiply(price);
                holder.tvPrice.setText(decimalFormat.format(total) + "$");
                holder.tvQuantity.setText(Integer.toString(quantity));
                productInCart.setQuantity(quantity);
                Toast.makeText(context, Integer.toString(quantity), Toast.LENGTH_SHORT).show();
                onClickProductInCart.onDecreaseProductInCart(productInCart);
            }
        });

        holder.btnIncrease.setOnClickListener(increase -> {
            int quantity = productInCart.getQuantity();
            quantity ++;
            BigDecimal quantityProduct = new BigDecimal(quantity);
            BigDecimal price = new BigDecimal(Float.toString(productInCart.getPrice()));
            BigDecimal total = quantityProduct.multiply(price);
            holder.tvPrice.setText(decimalFormat.format(total) + "$");
            holder.tvQuantity.setText(Integer.toString(quantity));
            productInCart.setQuantity(quantity);
            Toast.makeText(context, Integer.toString(quantity), Toast.LENGTH_SHORT).show();
            onClickProductInCart.onIncreaseProductInCart(productInCart);
        });

        holder.ivDelete.setOnClickListener(delete -> {
            onClickProductInCart.onDeteleProductInCart(productInCart);
            listProductInCart.remove(productInCart);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listProductInCart.size();
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg, ivDelete;
        CardView cvColor;
        TextView tvTitle, tvSize, tvPrice, tvQuantity;
        ImageButton btnIncrease, btnDecrease;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img_product);
            cvColor = itemView.findViewById(R.id.cv_color_product);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            tvTitle = itemView.findViewById(R.id.tv_cart_title);
            tvSize = itemView.findViewById(R.id.tv_size_product);
            tvPrice = itemView.findViewById(R.id.tv_price_total);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }
}
