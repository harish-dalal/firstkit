package com.example.firstkit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.firstkit.Model.Users;
import com.example.firstkit.Prevelant.Prevelant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {


    private Button gotosignup, signin;
    private EditText phone, password;
    FirebaseAuth mAuth;
    private String parentDB = "Users";
    private AlertDialog dialog;
    private Button gotoadmin;
    ////
    private Button gottodelete;
    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signin = findViewById(R.id.admin_sign_in);
        phone = findViewById(R.id.admin_phone_area_signin);
        password = findViewById(R.id.admin_password_area_signin);
        gotosignup = findViewById(R.id.go_to_sign_up);

        Paper.init(this);

        ///////////
        gottodelete = findViewById(R.id.todelete);

        gottodelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this , delete.class);
                startActivity(intent);
            }
        });

        //////////
        gotoadmin = findViewById(R.id.go_to_admin);

        gotoadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this , AdminLogin.class);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();


        mAuth = FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LogInUser();


            }
        });

        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SendOTP.class);
                startActivity(intent);
            }
        });
    }

    private void LogInUser() {

        String phoneNumberSignIn = phone.getText().toString();
        String passwordSignIn = password.getText().toString();

        if(phoneNumberSignIn.isEmpty()){

            phone.setError("Phone Number Required");
            phone.requestFocus();
            return;
        }
        else if(passwordSignIn.isEmpty()){

            password.setError("Password Required");
            password.requestFocus();
            return;
        }


        else{

            dialog.show();
            AllowAccess(phoneNumberSignIn, passwordSignIn);
        }
    }

    private void AllowAccess(final String phoneNumberSignIn, final String passwordSignIn) {




        final DatabaseReference Root;
        Root = FirebaseDatabase.getInstance().getReference();

        Root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDB).child("+91"+phoneNumberSignIn).exists()){
                    Users usersData = dataSnapshot.child(parentDB).child("+91"+phoneNumberSignIn).getValue(Users.class);

                    if(usersData.getPhone().equals("+91"+phoneNumberSignIn)){

                        if(usersData.getPassword().equals(passwordSignIn)){
                            //Storing the data in public variable made in Prevelant class
                            Paper.book().write(Prevelant.UserPhoneKey , phoneNumberSignIn);
                            Paper.book().write(Prevelant.UserPasswordKey , passwordSignIn);
                            Prevelant.currentOnlineUser = usersData;
                            dialog.dismiss();
                            Intent intent = new Intent(SignIn.this , HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                            startActivity(intent);
                        }
                        else
                        {
                            dialog.dismiss();
                            password.setError("Incorrect password");
                            password.requestFocus();
                            return;
                        }
                    }


                }
                else {
                    dialog.dismiss();

                    phone.setError("Number not registered");
                    phone.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}