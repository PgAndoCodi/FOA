package com.example.pglap.orderfood;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.common.ItemClickListener;
import com.example.pglap.orderfood.login.SignIn;
import com.example.pglap.orderfood.models.Category;
import com.example.pglap.orderfood.room.UserViewModel;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.example.pglap.orderfood.viewholders.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference category;

    private TextView mUserName;
    private TextView mUserEmail;
    private ImageView mUserPic;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> mmAdapter;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting Up TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialising ViewModel
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // Firebase init
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, CartActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set Name for User
        View headerView = navigationView.getHeaderView(0);
        mUserName = headerView.findViewById(R.id.nav_name);
        mUserEmail = headerView.findViewById(R.id.nav_email);
        mUserPic = headerView.findViewById(R.id.nav_image);

        mUserName.setText(Common.currentUser.getName());
        mUserEmail.setText(Common.currentUser.getPhone());

        // Set recycler view
        mRecyclerView = findViewById(R.id.mm_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        loadMenu();
    }

    private void loadMenu() {
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");

        mmAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.main_menu_item,
                CategoryViewHolder.class,
                category) {

            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.menuName.setText(model.getName());
                viewHolder.menuName.setTypeface(typeface);
                Picasso.get()
                        .load(model.getImage())
                        .resize(405, 220)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewHolder.menuImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(Home.this, SubMenuActivity.class);
                        intent.putExtra(CallbackKey.MENU_ITEM_ID, mmAdapter.getRef(position).getKey());
                        intent.putExtra(CallbackKey.MENU_ITEM_NAME, model.getName());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mmAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_menu :
                Toast.makeText(Home.this, "Menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_gallery :
                Toast.makeText(Home.this, "Gallery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_history :
                startActivity(new Intent(Home.this, OrderActivity.class));
                break;
            case R.id.nav_location :
                Toast.makeText(Home.this, "Location", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout :
//                Intent signOutIntent = new Intent(Home.this, SignIn.class);
//                signOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(signOutIntent);
                mUserViewModel.deleteUser(Common.currentUser);
                Common.currentUser = null;
                Toast.makeText(Home.this, "SignOut", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.nav_rate :
                Toast.makeText(Home.this, "Rate Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback :
                Toast.makeText(Home.this, "Feedback", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_call :
                Toast.makeText(Home.this, "Contact Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share :
                Toast.makeText(Home.this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about :
                Toast.makeText(Home.this, "About", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int width = source.getWidth();
            int height = source.getHeight();
            int size = Math.min(width, height);
            int x = (width - size) / 2;
            int y = (height - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
