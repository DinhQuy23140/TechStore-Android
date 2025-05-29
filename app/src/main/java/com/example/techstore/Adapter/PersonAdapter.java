package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Enum.ActionType;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickWidgetItem;

import java.util.List;

public class PersonAdapter extends RecyclerView .Adapter<PersonAdapter.PersonViewHolder>{
    Context context;
    List<Integer> listIcon;
    List<String> listTitle;
    OnClickWidgetItem listener;

    public PersonAdapter(Context context, List<Integer> listIcon, List<String> listTitle, OnClickWidgetItem listener) {
        this.context = context;
        this.listIcon = listIcon;
        this.listTitle = listTitle;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        String title = listTitle.get(position);
        int pathImg = listIcon.get(position);
        holder.tvTitle.setText(title);
        holder.ivIcon.setImageResource(pathImg);
        holder.ivShowMore.setOnClickListener(click -> listener.onClick(position, ActionType.VIEW_STATUS));
    }

    @Override
    public int getItemCount() {
        return listTitle.size();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon, ivShowMore;
        TextView tvTitle;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            ivShowMore = itemView.findViewById(R.id.iv_show_more);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
