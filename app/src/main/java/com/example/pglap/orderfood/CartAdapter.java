package com.example.pglap.orderfood;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.example.pglap.orderfood.utils.DbBitmapUtility;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {

    private static final String TAG = CartAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private List<Order> mOrderList = new ArrayList<>();

    public CartAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.cart_item;
        final boolean shouldAttachedToParentImmediately = false;

        View view = mLayoutInflater.inflate(layoutIdForListItem, parent, shouldAttachedToParentImmediately);
        view.setFocusable(true);
        return new CartAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        if(!mOrderList.isEmpty()) {
            Order order = mOrderList.get(position);

//            TextDrawable drawable = TextDrawable.builder()
//                    .buildRound(""+order.getQuantity(), Color.GREEN);
            holder.mImage.setImageResource(R.drawable.ic_restaurant_menu);

            holder.itemView.setTag(order.getOrderIdString());
            Log.e(TAG, "ORDER ID : " + order.getOrderIdString());

            holder.mFoodName.setText(order.getProductName());


            String quantity = "quantity - " + order.getQuantity();
            holder.mQuantity.setText(quantity);

            String price = order.getPrice();
            int amount = Integer.valueOf(price) * Integer.valueOf(order.getQuantity());

            Locale locale = new Locale("en", "IN");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            String amtString = fmt.format(amount);
            int stringSize = amtString.length();
            String s = amtString.substring(0, stringSize-3);
            holder.mPrice.setText(s);
        }
    }

    @Override
    public int getItemCount() {
        if(!mOrderList.isEmpty()) {
            return mOrderList.size();
        }
        return 0;
    }

    public class CartAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cart_name) TextView mFoodName;
        @BindView(R.id.cart_quantity) TextView mQuantity;
        @BindView(R.id.cart_price) TextView mPrice;
        @BindView(R.id.cart_image) ImageView mImage;

        public CartAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void setOrderData(List<Order> order){
        mOrderList = order;
        notifyDataSetChanged();
    }

}