package com.example.pglap.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.common.ItemClickListener;
import com.example.pglap.orderfood.models.Category;
import com.example.pglap.orderfood.models.Request;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.example.pglap.orderfood.viewholders.CategoryViewHolder;
import com.example.pglap.orderfood.viewholders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Setting Up TOOLBAR
        Toolbar toolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        // Set recycler view
        mRecyclerView = findViewById(R.id.order_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        loadOrderHistory(Common.currentUser.getPhone());
    }

    private void loadOrderHistory(String userPhone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_item,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(userPhone))
            {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, int position) {
                viewHolder.mOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.mStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.mAddress.setText(model.getAddress());
                viewHolder.mAmount.setText(model.getTotal());
                viewHolder.mDate.setText(getDate(adapter.getRef(position).getKey(), 2));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                        intent.putExtra(CallbackKey.ORDER_ID, model.getName());
                        startActivity(intent);
                    }
                });

            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Return date in specified format.
     * @param milliSec Date in milliseconds
     * @return String representing date in specified format
     */
    public static String getDate(String milliSec, int type) { // 0 = DATE,  1 = TIME,  2 = BOTH

        long milliSeconds = Long.valueOf(milliSec);
        // Create a DateFormatter object for displaying date in specified format.
        String dateFormat = "EEE, MMM d, ''yyyy";
        String timeFormat = "h:mm a";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        SimpleDateFormat timeFormatter = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String stringDate = dateFormatter.format(calendar.getTime());
        String stringTime = timeFormatter.format(calendar.getTime());

        if(type == 0) {
            return stringDate;
        } else if(type == 1) {
            return stringTime;
        } else {
            return stringDate + "  " + stringTime;
        }
    }

    private String convertCodeToStatus(String status) {
        switch (status) {
            case "0" :
                return "Pending";
            case "1" :
                return "On my way..";
            case "2" :
                return "Delivered";
            default:
                return "Pending";
        }
    }
}
