package com.example.pglap.orderfood;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.models.Request;
import com.example.pglap.orderfood.room.UserViewModel;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = CartActivity.class.getSimpleName();
    @BindView(R.id.cart_amount) TextView mAmount;
    @BindView(R.id.cart_submit_order) CardView mOrderSubmit;
    @BindView(R.id.cart_delete) TextView mClearCart;

    private FirebaseDatabase database;
    private DatabaseReference requests;

    private RecyclerView mRecyclerView;
    private UserViewModel mUserViewModel;
    private CartAdapter cartAdapter;
    private CoordinatorLayout coordinatorLayout;
    private boolean deleteConfirm = false;
    private String mUserAddress;
    private String mTotalBillAmount;
    private List<Order> mOrderedFoodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        // Setting Up TOOLBAR
        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        SnackbarLaunch(getResources().getString(R.string.snackbar_swipe_to_remove));

        // Set recycler view
        mRecyclerView = findViewById(R.id.cart_recycler_view);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(CartActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cartAdapter = new CartAdapter(this);
        mRecyclerView.setAdapter(cartAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // 1. Getting the first time call for a list of words.
        // 2. Getting a call thru onChanged method every time underlying database changes
        mUserViewModel.queryOrderData().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> orders) {
                // Update the cached copy of the words in the adapter.
                if(orders != null) {
                    cartAdapter.setOrderData(orders);
                    mTotalBillAmount = setTotalAmount(orders);
                    mOrderedFoodItems = orders;
                }
            }
        });


        // CLEAR CART (i.e. DELETE ALL ITEMS)
        mClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog("Clear Cart");
            }
        });



        // PLACING ORDER
        mOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });



        // DELETING SINGLE ITEM FROM CART
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                deleteConfirm = SnackbarOnDelete();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (deleteConfirm) {
                            RemoveConfirmed(viewHolder);
                            deleteConfirm = false;
                        } else {
                            RemoveReversed();
                        }
                    }
                }, 2500);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter Address :");

        final EditText edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT );

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress); // Added EditText to Alert Dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUserAddress = edtAddress.getText().toString();
                Request request = new Request(
                        Common.currentUser.getName(),
                        Common.currentUser.getPhone(),
                        mUserAddress,
                        mTotalBillAmount,
                        mOrderedFoodItems );

                //Submit to firebase
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                deleteAllCartItems("Thank you, order is placed");
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private String setTotalAmount(List<Order> orders) {
        int total = 0;
        for (int i = 0; i < orders.size(); i++) {
            int price = orders.get(i).getPriceInt();
            int qty = orders.get(i).getQuantityInt();
            total += (price * qty);
        }

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        String amtString = fmt.format(total);
        int stringSize = amtString.length();
        String totalAmount = amtString.substring(0, stringSize-3);
        mAmount.setText(totalAmount);
        return totalAmount;
    }

    private void RemoveConfirmed(RecyclerView.ViewHolder viewHolder) {
        if (!viewHolder.itemView.getTag().equals("")) {
            String orderId = viewHolder.itemView.getTag().toString();
            if (orderId != null) {
                mUserViewModel.deleteOrder(orderId);
            } else {
                Toast.makeText(CartActivity.this, "Order ID missing", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CartActivity.this, "Unable to delete", Toast.LENGTH_SHORT).show();
        }
    }

    private void RemoveReversed(){
        //Need to notify specially because their is no change in database
        cartAdapter.notifyDataSetChanged();
    }

    private void SnackbarLaunch(String snackText) {
        coordinatorLayout = findViewById(R.id.cart_layout);
        Snackbar skb = Snackbar.make(coordinatorLayout, snackText, Snackbar.LENGTH_SHORT);
        View sbView = skb.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(12);
        textView.setTextColor(Color.WHITE);
        skb.show();
    }

    private boolean SnackbarOnDelete() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getText(R.string.snackbar_removed), Snackbar.LENGTH_LONG)
                .setAction(getText(R.string.snackbar_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteConfirm = false;
                    }
                });
        snackbar.setActionTextColor(Color.YELLOW);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
        return true;
    }

    private void ConfirmationDialog(String confirmationText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAllCartItems("Cart Cleared");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        startActivity(new Intent(CartActivity.this, CartActivity.class));
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(confirmationText);
        alert.show();
        setContentView(R.layout.activity_cart);
    }

    private void deleteAllCartItems(String message) {
        mUserViewModel.deleteAllOrder();
        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CartActivity.this, Home.class));
    }
}