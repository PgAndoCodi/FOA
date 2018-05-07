package com.example.pglap.orderfood;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.pglap.orderfood.models.Food;
import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.room.UserViewModel;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class SubItemActivity extends AppCompatActivity {

    private static final String TAG = SubItemActivity.class.getSimpleName();
    private TextView food_name, food_price, food_desription;
    private ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton addCart;
    ElegantNumberButton quantityButton;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;
    private Food food;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item);

        //Initialising ViewModel
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference(CallbackKey.FOOD_KEY);

        //Init View
        quantityButton = findViewById(R.id.subitem_quantity);
        addCart = findViewById(R.id.subitem_fab_cart);

        food_image = findViewById(R.id.subitem_image);
        food_name = findViewById(R.id.subitem_name);
        food_price = findViewById(R.id.subitem_price);
        food_desription = findViewById(R.id.subitem_description);

        collapsingToolbarLayout = findViewById(R.id.subitem_collapse);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food Id from intent
        if(getIntent() != null) {
            foodId = getIntent().getStringExtra(CallbackKey.MENU_ITEM_ID);
            if(!foodId.isEmpty()) {
                getDetailFood(foodId);
            }
        }

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order(
                        foodId,
                        food.getName(),
                        quantityButton.getNumber(),
                        food.getPrice(),
                        food.getDiscount());
                Toast.makeText(SubItemActivity.this, getResources()
                        .getText(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
                mUserViewModel.insertOrder(order);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                food = dataSnapshot.getValue(Food.class);

                if (food != null) {
                    //Set Food Image
                    Picasso.get()
                            .load(food.getImage())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(food_image);

                    collapsingToolbarLayout.setTitle(food.getName());
                    food_name.setText(food.getName());
                    food_price.setText(food.getPrice());
                    food_desription.setText(food.getDescription());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
