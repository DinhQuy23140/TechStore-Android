package com.example.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Enum.ActionType;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickWidgetItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    List<String> listHistory;
    OnClickWidgetItem onClickWidgetItem;

    public HistoryAdapter(Context context, List<String> listHistory, OnClickWidgetItem onClickWidgetItem) {
        this.context = context;
        this.listHistory = listHistory;
        this.onClickWidgetItem = onClickWidgetItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvHistory.setText(listHistory.get(position));
        holder.cvClear.setOnClickListener(delete -> {
            if (onClickWidgetItem != null) onClickWidgetItem.onClick(position, ActionType.VIEW_STATUS);
        });
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistory;
        CardView cvClear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistory = itemView.findViewById(R.id.tv_History);
            cvClear = itemView.findViewById(R.id.cv_Delete);
        }
    }
}
