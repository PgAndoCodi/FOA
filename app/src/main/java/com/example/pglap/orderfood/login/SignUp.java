package com.example.pglap.orderfood.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pglap.orderfood.Home;
import com.example.pglap.orderfood.R;
import com.example.pglap.orderfood.common.Common;
import com.example.pglap.orderfood.models.User;
import com.example.pglap.orderfood.room.UserRepository;
import com.example.pglap.orderfood.room.UserViewModel;
import com.example.pglap.orderfood.utils.CallbackKey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class SignUp extends AppCompatActivity {

    private static final String TAG = SignUp.class.getSimpleName();
    @BindView(R.id.name_su) EditText mUserName;
    @BindView(R.id.phone_su) EditText mPhoneNumber;
    @BindView(R.id.password_su) EditText mPassword;
    @BindView(R.id.button_su_confirm) Button mSignUpConfirm;
    @BindView(R.id.su_redirect_to_si) TextView mSignUpRedirect;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_su);
        ButterKnife.bind(this);

        //Initialising ViewModel (For locale database)
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        //Initialising Firebase Database (For online database)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference(CallbackKey.USER_KEY);

        //Initialise sign-up process
        mSignUpConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise progress bar, while user data is fetched and verified
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage(getResources().getString(R.string.loading_message));
                mDialog.show();

                //Get User entered text from UI
                final String enteredName = mUserName.getText().toString();
                final String enteredPassword = mPassword.getText().toString();
                final String Key = mPhoneNumber.getText().toString();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user already exits in database
                        if (dataSnapshot.child(Key).exists()) {
                            mDialog.dismiss();
                            redirectToSignInPage(Key);
                            finish();
                        } else {
                            mDialog.dismiss();
                            User user = new User(
                                    enteredName,
                                    enteredPassword );

                            //Save data in online database
                            table_user.child(Key).setValue(user);
                            Toast.makeText(SignUp.this, getResources()
                                    .getText(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
                            redirectToHomePage(Key, user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                    }
                });
            }
        });

        mSignUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSignInPage("");
            }
        });
    }

    private void redirectToSignInPage(String phoneNumber) {
        Intent signInIntent = new Intent(SignUp.this, SignIn.class);
        signInIntent.putExtra(CallbackKey.PHONE_NUMBER, phoneNumber);
        startActivity(signInIntent);
        finish();
    }

    private void redirectToHomePage(String key, User user) {
        //Now add phone to user
        user.setPhone(key);
        //For accessing during current session
        Common.currentUser = user;
        //Save in local db for auto login
        mUserViewModel.insertUser(user);
        //Send user to home page
        startActivity(new Intent(SignUp.this, Home.class));
        finish();
    }
}
