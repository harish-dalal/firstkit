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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class AdminLogin extends AppCompatActivity {

    private EditText adminphone , adminpassword;
    private Button adminsignin , gotouser;

    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminphone = findViewById(R.id.admin_phone_area_signin);
        adminpassword = findViewById(R.id.admin_password_area_signin);
        adminsignin = findViewById(R.id.admin_sign_in);
        gotouser = findViewById(R.id.go_to_usersign_in);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();


        gotouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogin.this , SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }


        });

        adminsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(adminphone.getText().toString().isEmpty()){

                    adminphone.setError("Phone Number Required");
                    adminphone.requestFocus();
                    return;
                }
                else if(adminpassword.getText().toString().isEmpty()){

                    adminpassword.setError("Password Required");
                    adminpassword.requestFocus();
                    return;
                }

                dialog.show();
                AllowAdmin(adminphone.getText().toString() , adminpassword.getText().toString());
            }
        });
    }

    private void AllowAdmin(final String phoneNumberSignIn, final String passwordSignIn) {



        final DatabaseReference Root;
        Root = FirebaseDatabase.getInstance().getReference();

        Root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Admins").child("+91"+phoneNumberSignIn).exists()){

                    if(dataSnapshot.child("Admins").child("+91"+phoneNumberSignIn).child("phone").getValue().equals("+91"+phoneNumberSignIn)){

                        if(dataSnapshot.child("Admins").child("+91"+phoneNumberSignIn).child("password").getValue().equals(passwordSignIn)){

                            dialog.dismiss();
                            Intent intent = new Intent(AdminLogin.this , AdminPanel.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                            startActivity(intent);
                        }
                        else
                        {
                            dialog.dismiss();
                            adminpassword.setError("Incorrect password");
                            adminpassword.requestFocus();
                            return;
                        }
                    }


                }
                else {
                    dialog.dismiss();

                    adminphone.setError("Number not registered");
                    adminphone.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
