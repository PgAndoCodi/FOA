package com.example.pglap.orderfood.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pglap.orderfood.Home;
import com.example.pglap.orderfood.R;
import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.models.User;
import com.example.pglap.orderfood.room.UserViewModel;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignIn extends AppCompatActivity {

    private static final String TAG = SignIn.class.getSimpleName();
    @BindView(R.id.phone_si) MaterialEditText mPhoneNumber;
    @BindView(R.id.password_si) MaterialEditText mPassword;
    @BindView(R.id.button_si_confirm) Button mSignInConfirm;
    @BindView(R.id.si_redirect_to_su) TextView mSignUpRedirect;
    private UserViewModel mUserViewModel;
    private DatabaseReference mTable_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_si);
        ButterKnife.bind(this);

        //When user is transferred from sign-up page
        if(getIntent().hasExtra(CallbackKey.PHONE_NUMBER)) {
            mPhoneNumber.setText(getIntent().getStringExtra(CallbackKey.PHONE_NUMBER));
        }

        /** Find User either in LOCALE or ONLINE database */
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.queryUserData().observe(SignIn.this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                if (user != null) {
                    //CASE 1 : When user found in local database, simply allow him access
                    Common.currentUser = user;
                    sendUserToHomePage();
                } else {
                    //CASE 2 : If not, then get online database for user authentication
                    FirebaseDatabase databaseSignIn = FirebaseDatabase.getInstance();
                    mTable_user = databaseSignIn.getReference(CallbackKey.USER_KEY);
                }
            }
        });

        /** Verifying User authenticity in online database on click */
        mSignInConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise progress bar, while user data is fetched and verified
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage(getResources().getString(R.string.loading_message));
                mDialog.show();

                if(mTable_user != null) {
                    mDialog.dismiss();
                    mTable_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get User entered text from UI
                            String Key = mPhoneNumber.getText().toString();

                            /** FIRST check if user exits in data base on the bases of KEY */
                            if (dataSnapshot.child(Key).exists()) {
                                //When Key(user) found in database
                                //Get User information to enable sign in process
                                User user = dataSnapshot.child(Key).getValue(User.class);
                                if (user != null) {
                                    /** SECOND confirm password entered by user */
                                    if (user.getPassword().equals(mPassword.getText().toString())) {
                                        //User will be accessible through out current session
                                        Common.currentUser = user;
                                        //User will be accessible for future login
                                        mUserViewModel.insertUser(user);
                                        sendUserToHomePage();
                                    } else {
                                        Toast.makeText(SignIn.this, "Check your password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //When returned User is a null object
                                    Toast.makeText(SignIn.this, "We are unable to fetch data", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignIn.this, SignIn.class));
                                }
                            } else {
                                //When Key(user) does'nt exist in database
                                Toast.makeText(SignIn.this, "No records found : " + Key, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                } else {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "Oops! can't access internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //When User choose to goto Sign-Up Page
        mSignUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSignUpPage();
            }
        });
    }

    private void sendUserToSignUpPage() {
        Intent intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
        finish();
    }

    private void sendUserToHomePage() {
        Intent intent = new Intent(SignIn.this, Home.class);
        startActivity(intent);
        finish();
    }
}
