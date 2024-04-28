package com.example.firstkit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {
    EditText phoneNumber, OTP;
    Button sendCode, Next, goToSignIn;
    FirebaseAuth mAuth;


    String phoneNumberForAA;
    String codeSent;


    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);


        mAuth = FirebaseAuth.getInstance();
        
        phoneNumber = findViewById(R.id.phone_area_otp);
        OTP = findViewById(R.id.otp_area);
        sendCode = (Button) findViewById(R.id.send_code);
        Next = (Button) findViewById(R.id.Next);
        Next.setEnabled(false);
        goToSignIn = (Button) findViewById(R.id.go_to_sign_in);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();



        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendOTP.this , SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                startActivity(intent);
                finish();

            }
        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWhetherTheDetailsIsFilled();


            }
            
            
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyCode();
            }
        });
        
    }

    private void checkWhetherTheDetailsIsFilled() {

        phoneNumberForAA = phoneNumber.getText().toString();


        if(phoneNumberForAA.isEmpty()){
            dialog.dismiss();
            phoneNumber.setError("Please enter a mobile number");
            phoneNumber.requestFocus();
            return;
        }
        else if(phoneNumberForAA.length()!=10){

            dialog.dismiss();
            phoneNumber.setError("Please enter a valid mobile number");
            phoneNumber.requestFocus();
            return;

        }
        dialog.show();

        CheckUserInDB(phoneNumberForAA);

    }

    private void CheckUserInDB(String toString) {

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //If this number not exist then send verification code
                if(!(dataSnapshot.child("Users").child("+91"+phoneNumber.getText().toString()).exists())){

                    sendVerificationCode();

                }
                else{

                    dialog.dismiss();
                    phoneNumber.setError("User already exist");
                    phoneNumber.requestFocus();
                    return;
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void verifyCode() {

        String getcode = OTP.getText().toString();

        if(OTP.getText().toString().isEmpty())
        {
            phoneNumber.setError("OTP is required");
            phoneNumber.requestFocus();
            return;
        }


        dialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, getcode);
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Successfull verified", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SendOTP.this, SignUp.class);
                            intent.putExtra("phone_number_from_otp",phoneNumberForAA);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                            startActivity(intent);

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                dialog.dismiss();
                                OTP.setError("Invalid OTP");
                                OTP.requestFocus();
                                return;
                            }


                        }

                    }
                });
    }




    private void sendVerificationCode() {

        String phonenumber1 = phoneNumber.getText().toString();
        String phone = "+91"+phonenumber1;





        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                90,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);



    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            dialog.dismiss();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(SendOTP.this, "sent", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            codeSent = s;
            sendCode.setEnabled(false);
            Next.setEnabled(true);

        }};

}
