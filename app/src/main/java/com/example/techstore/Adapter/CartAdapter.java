package com.example.techstore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickCheckBox;
import com.example.techstore.interfaces.OnClickProductInCart;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.ProductInCart;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<ProductInCart> listProductInCart;
    OnClickProductInCart onClickProductInCart;
    OnClickCheckBox onClickCheckBox;

    public CartAdapter(Context context, List<ProductInCart> listProductInCart, OnClickProductInCart onClickProductInCart, OnClickCheckBox onClickCheckBox) {
        this.context = context;
        this.listProductInCart = listProductInCart;
        this.onClickProductInCart = onClickProductInCart;
        this.onClickCheckBox = onClickCheckBox;
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
        if (productInCart.getSize().isEmpty()) {
            holder.tvSize.setVisibility(View.GONE);
            holder.lnSpace.setVisibility(View.GONE);
        } else {
            holder.tvSize.setText("Size = " + productInCart.getSize());
        }
        holder.edtQuantity.setText(Integer.toString(productInCart.getQuantity()));
        float pricePro = productInCart.getQuantity() * productInCart.getPrice();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.tvPrice.setText(decimalFormat.format(pricePro) + "$");
        if (productInCart.getColor() == 0) {
            holder.cvColor.setVisibility(View.GONE);
            holder.tvColor.setVisibility(View.GONE);
            holder.lnSpace.setVisibility(View.GONE);
        } else {
            holder.cvColor.setCardBackgroundColor(productInCart.getColor());
        }
        holder.btnDecrease.setOnClickListener(decrease -> {
            int quantity = productInCart.getQuantity();
            if (quantity > 0) {
                quantity --;
                BigDecimal quantityProduct = new BigDecimal(quantity);
                BigDecimal price = new BigDecimal(Float.toString(productInCart.getPrice()));
                BigDecimal total = quantityProduct.multiply(price);
                holder.tvPrice.setText(decimalFormat.format(total) + "$");
                holder.edtQuantity.setText(Integer.toString(quantity));
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
            holder.edtQuantity.setText(Integer.toString(quantity));
            productInCart.setQuantity(quantity);
            Toast.makeText(context, Integer.toString(quantity), Toast.LENGTH_SHORT).show();
            onClickProductInCart.onIncreaseProductInCart(productInCart);
        });

        holder.ivDelete.setOnClickListener(delete -> {
            onClickProductInCart.onDeteleProductInCart(productInCart);
            listProductInCart.remove(productInCart);
            notifyDataSetChanged();
        });

        holder.cbSelectProduct.setOnClickListener(select -> {
            if (holder.cbSelectProduct.isChecked()) {
                onClickCheckBox.onCheckBoxClick(position);
            } else {
                onClickCheckBox.onUnCheckBoxClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProductInCart.size();
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg, ivDelete;
        CardView cvColor;
        TextView tvTitle, tvSize, tvPrice, tvColor;
        EditText edtQuantity;
        ImageButton btnIncrease, btnDecrease;
        CheckBox cbSelectProduct;
        LinearLayout lnSpace;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img_product);
            cvColor = itemView.findViewById(R.id.cv_color_product);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            tvTitle = itemView.findViewById(R.id.tv_cart_title);
            tvSize = itemView.findViewById(R.id.tv_size_product);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvPrice = itemView.findViewById(R.id.tv_price_total);
            edtQuantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            cbSelectProduct = itemView.findViewById(R.id.cb_select_product);
            lnSpace = itemView.findViewById(R.id.ln_space);
        }
    }
}
