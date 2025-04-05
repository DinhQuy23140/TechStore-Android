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
import com.example.techstore.activity.NotificationActivity;
import com.example.techstore.activity.ViewNotifiActivity;
import com.example.techstore.model.Notifi;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewholder> {
    Context context;
    ArrayList<Notifi> listNotifi;

    public NotificationAdapter(Context context, ArrayList<Notifi> listNotifi) {
        this.context = context;
        this.listNotifi = listNotifi;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Notifi notifi = listNotifi.get(position);
        holder.notification_tv_title.setText(notifi.getTitle());
        holder.notification_tv_des.setText(notifi.getDes());
        Glide.with(holder.itemView.getContext())
                .load(notifi.getImg())
                .placeholder(R.drawable.background_image_default)
                .error(R.drawable.background_error_load)
                .into(holder.notification_iv_img);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewNotifiActivity  .class);
            Gson gson = new Gson();
            String json = gson.toJson(notifi);
            intent.putExtra("notifi", json);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listNotifi.size();
    }


    public class MyViewholder extends RecyclerView.ViewHolder {
        ImageView notification_iv_img;
        TextView notification_tv_title, notification_tv_des;


        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            notification_iv_img = itemView.findViewById(R.id.item_iv_notification);
            notification_tv_title = itemView.findViewById(R.id.item_tv_notification_title);
            notification_tv_des = itemView.findViewById(R.id.item_tv_notification);
        }
    }
}
