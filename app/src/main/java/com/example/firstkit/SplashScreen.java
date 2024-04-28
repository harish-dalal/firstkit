package com.example.firstkit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstkit.Model.Users;
import com.example.firstkit.Prevelant.Prevelant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class SplashScreen extends AppCompatActivity {

    Handler handler;
    private TextView delelte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Paper.init(this);

        final String phoneRemember = Paper.book().read(Prevelant.UserPhoneKey);
        final String passwordRemember = Paper.book().read(Prevelant.UserPasswordKey);

        ///////////////

        //detect internet and show the data
        if(isNetworkStatusAvialable (getApplicationContext())) {
           // Toast.makeText(getApplicationContext(), "Internet detected", Toast.LENGTH_SHORT).show();


            /////////


            handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {


                    Log.e("TAG", "running");


                    if (phoneRemember != null && passwordRemember != null){

                        AllowRememberAccess(phoneRemember , passwordRemember);
                    }


                    else {

                        Intent intent = new Intent(SplashScreen.this, SignIn.class);
                        startActivity(intent);
                        finish();
                    }


                }
            },3000);

            ///////////
        } else {
           Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

        }



        /////////////////




    }

    private void AllowRememberAccess(final String phoneRemember, final String passwordRemember)
    {
        final DatabaseReference Root;
        Root = FirebaseDatabase.getInstance().getReference();

        Root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child("+91"+phoneRemember).exists()){
                    //userdata is a object of user class
                    Users usersData = dataSnapshot.child("Users").child("+91"+phoneRemember).getValue(Users.class);

                    if(usersData.getPhone().equals("+91"+phoneRemember)){

                        if(usersData.getPassword().equals(passwordRemember)){


                            //create the object in currentonlineuser again (don't know why)
                            Prevelant.currentOnlineUser = usersData;
                            Intent intent = new Intent(SplashScreen.this , HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                            startActivity(intent);
                        }

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }
}

