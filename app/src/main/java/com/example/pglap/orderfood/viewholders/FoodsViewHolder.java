package com.example.pglap.orderfood.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pglap.orderfood.common.ItemClickListener;
import com.example.pglap.orderfood.R;

public class FoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView smName;
    public ImageView smImage;
    public ImageView smLabel;
    private ItemClickListener itemClickListener;

    public FoodsViewHolder(View itemView) {
        super(itemView);
        smName = itemView.findViewById(R.id.sm_name);
        smImage = itemView.findViewById(R.id.sm_image);
        smLabel = itemView.findViewById(R.id.sm_lable);
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
