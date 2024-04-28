package com.example.firstkit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private EditText name, password;
    private Button sign_up;

    private AlertDialog dialog;

    private AlertDialog Signupsuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        final String phonenumber = intent.getStringExtra("phone_number_from_otp");

        name = (EditText) findViewById(R.id.name_area_sign_up);
        password = (EditText) findViewById(R.id.password_area_sign_up);

        sign_up = (Button) findViewById(R.id.sign_up);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();



        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setCancelable(false); // if you want user to wait for some process to finish,
        builder1.setView(R.layout.signup_loding);
        Signupsuccess = builder1.create();




        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(name.getText().toString().isEmpty()){

                    name.setError("Name required");
                    name.requestFocus();
                    return;
                }
                else if(password.getText().toString().isEmpty()){

                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }

                ////////adding data to realtimedatabase for the user
                dialog.show();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        HashMap<String , Object> userdata = new HashMap<>();
                        userdata.put("phone" , user.getPhoneNumber());
                        userdata.put("name" , name.getText().toString());
                        userdata.put("password" , password.getText().toString());
                        userdata.put("Uid" , user.getUid());
                        userdata.put("Email" , "");
                        userdata.put("address" , "");
                        userdata.put("alternatemobile" , "");

                        RootRef.child("Users").child(user.getPhoneNumber()).updateChildren(userdata)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()) {

                                            dialog.dismiss();
                                            loadingMessage();

;
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });



        //code = (EditText) findViewById(R.id.code_area_signup);
        //code.setText(user.getPhoneNumber() + user.getDisplayName());



    }

    private void loadingMessage() {


        Signupsuccess.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Signupsuccess.dismiss();
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void
    onStop() {
        super.onStop();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference deleteuser = FirebaseDatabase.getInstance().getReference().child("Users");
        deleteuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(user.getPhoneNumber()).exists()){

                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ///////user is deleted
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       }
    }

    ;




