package com.example.pglap.orderfood.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pglap.orderfood.Home;
import com.example.pglap.orderfood.R;
import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.models.User;
import com.example.pglap.orderfood.room.UserViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeScreen extends AppCompatActivity {

    @BindView(R.id.slogan) TextView mSloganString;
    @BindView(R.id.button_enter) ImageView mEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        ButterKnife.bind(this);

        //Check User registration status
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialising ViewModel
                UserViewModel mUserViewModel = ViewModelProviders
                        .of(WelcomeScreen.this)
                        .get(UserViewModel.class);
                //Accessing & Observing data
                mUserViewModel.queryUserData().observe(WelcomeScreen.this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable final User user) {
                        if (user != null) {
                            Common.currentUser = user;
                            registeredUser();
                        } else {
                            unRegisteredUser();
                        }
                    }
                });
            }
        });
    }

    private void unRegisteredUser() {
        Intent signInIntent = new Intent(WelcomeScreen.this, SignUp.class);
        startActivity(signInIntent);
        finish();
    }

    private void registeredUser() {
        Intent homeIntent = new Intent(WelcomeScreen.this, Home.class);
        startActivity(homeIntent);
        finish();
    }

}
