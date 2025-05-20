package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.interfaces.OnItemClickListener;
import com.example.techstore.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    Context context;
    List<Address> listAddress;
    OnItemClickListener listener;

    public AddressAdapter(Context context, List<Address> listAddress, OnItemClickListener listener) {
        this.context = context;
        this.listAddress = listAddress;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
        Address address = listAddress.get(position);
        holder.tvType.setText(address.getType());
        holder.tvUsername.setText(address.getName());
        holder.tvPhone.setText(address.getPhone());
        holder.tvAddress.setText(address.toString());
        holder.tvDetail.setText(address.getDetail());

        holder.itemView.setOnClickListener(click -> {
            if (listener != null) listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return listAddress.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvUsername, tvPhone, tvAddress, tvDetail;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDetail = itemView.findViewById(R.id.tv_detail);
        }
    }
}
