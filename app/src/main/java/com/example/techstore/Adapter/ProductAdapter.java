package com.example.techstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.activity.ViewProductActivity;
import com.example.techstore.model.Product;
import com.example.techstore.untilities.Constants;
import com.google.gson.Gson;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    List<Product> listProduct;

    public ProductAdapter(Context context, List<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = listProduct.get(position);
        String image = product.getImage();
        Glide.with(holder.itemView.getContext())
                        .load(image)
                                .into(holder.imageProduct);
        holder.nameProduct.setText(product.getTitle());
        holder.priceProduct.setText(String.valueOf(product.getPrice()) + " $");
        holder.ratingProduct.setText(String.valueOf(product.getRating().getCount()));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewProductActivity.class);
            Gson gson = new Gson();
            String productStr = gson.toJson(product);
            intent.putExtra(Constants.KEY_SHARE_PRODUCT, productStr);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView favorite, imageProduct, rating;
        TextView nameProduct, priceProduct, soldProduct, ratingProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            favorite = itemView.findViewById(R.id.product_iv_favorite);
            imageProduct = itemView.findViewById(R.id.product_iv_image);
            rating = itemView.findViewById(R.id.product_iv_rating);
            nameProduct = itemView.findViewById(R.id.product_tv_name);
            priceProduct = itemView.findViewById(R.id.product_tv_price);
            soldProduct = itemView.findViewById(R.id.product_tv_sold);
            ratingProduct = itemView.findViewById(R.id.product_tv_rating);
        }
    }
}
