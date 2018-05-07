package com.example.pglap.orderfood;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pglap.orderfood.common.ItemClickListener;
import com.example.pglap.orderfood.models.Food;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.example.pglap.orderfood.viewholders.FoodsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SubMenuActivity extends AppCompatActivity {

    private static final String TAG = SubMenuActivity.class.getSimpleName();
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private RecyclerView mRecyclerView;
    private String pageTitle;
    private String categoryId;
    private FirebaseRecyclerAdapter<Food, FoodsViewHolder> smAdapter;

    //Search Functionality
    private FirebaseRecyclerAdapter<Food, FoodsViewHolder> smSearchAdapter;
    private List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);

        // Firebase init
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference(CallbackKey.FOOD_KEY);

        //Set-up RecyclerView
        mRecyclerView = findViewById(R.id.sm_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if(intent != null) {
            pageTitle = intent.getStringExtra(CallbackKey.MENU_ITEM_NAME);
            categoryId = intent.getStringExtra(CallbackKey.MENU_ITEM_ID);
        }

        /*
        if(pageTitle != null) {
            // Setting Up TOOLBAR
            Toolbar toolbar = findViewById(R.id.submenu_toolbar);
            toolbar.setTitle(pageTitle);
            setSupportActionBar(toolbar);
        }*/

        if(!categoryId.isEmpty() && categoryId != null){
            loadSubMenu(categoryId);
            setUpSearchBar();
        }
    }

    private void setUpSearchBar() {
        Log.e(TAG, "setUpSearchBar");                                                           //3
        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint("Enter your food");
        loadSuggest();
        searchBar.setLastSuggestions(suggestList);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Suggest list will change on type of user
                List<String> suggest = new ArrayList<>();
                for(String search:suggestList) {
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar is closed, restore original adapter
                if(!enabled)
                    mRecyclerView.setAdapter(smAdapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search is finished, show searched result
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        Log.e(TAG, "startSearch");                                                              //7
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        smSearchAdapter = new FirebaseRecyclerAdapter<Food, FoodsViewHolder>(
                Food.class,
                R.layout.sub_menu_item,
                FoodsViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodsViewHolder viewHolder, final Food model, int position) {
                Log.e(TAG, "startSearch - populateViewHolder");                                 //8
                viewHolder.smName.setText(model.getName());
                viewHolder.smName.setTypeface(typeface);
                Picasso.get()
                        .load(model.getImage())
                        .resize(405, 213)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewHolder.smImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(SubMenuActivity.this, SubItemActivity.class);
                        intent.putExtra(CallbackKey.MENU_ITEM_ID, smAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(smSearchAdapter); //Set adapter for recycler view in search result
    }

    private void loadSuggest() {
        Log.e(TAG, "LoadSuggest");                                                               //4
        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG, "LoadSuggest - onDataChange");                                //5
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                            Food item = postSnapshot.getValue(Food.class);
                            if(item != null) {
                                suggestList.add(item.getName()); //Add Food name to suggest list
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadSubMenu(String categoryId) {
        Log.e(TAG, "loadSubMenu");                                                              //2
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Qlassik_TB.otf");
        smAdapter = new FirebaseRecyclerAdapter<Food, FoodsViewHolder>(
                Food.class,
                R.layout.sub_menu_item,
                FoodsViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodsViewHolder viewHolder, final Food model, int position) {
                Log.e(TAG, "loadSubMenu - populateViewHolder");                                 //6
                viewHolder.smName.setText(model.getName());
                viewHolder.smName.setTypeface(typeface);
                Picasso.get()
                        .load(model.getImage())
                        .resize(405, 213)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewHolder.smImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(SubMenuActivity.this, SubItemActivity.class);
                        intent.putExtra(CallbackKey.MENU_ITEM_ID, smAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(smAdapter);
    }
}
