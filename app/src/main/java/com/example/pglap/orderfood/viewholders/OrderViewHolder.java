package com.example.pglap.orderfood.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.pglap.orderfood.common.ItemClickListener;
import com.example.pglap.orderfood.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mOrderId;
    public TextView mDate;
    public TextView mAddress;
    public TextView mAmount;
    public TextView mStatus;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        mOrderId = itemView.findViewById(R.id.order_id);
        mDate = itemView.findViewById(R.id.order_date);
        mAddress = itemView.findViewById(R.id.order_address);
        mAmount = itemView.findViewById(R.id.order_amount);
        mStatus = itemView.findViewById(R.id.order_status);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}