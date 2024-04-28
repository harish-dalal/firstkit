package com.example.firstkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

import static android.app.PendingIntent.getActivity;


public class mainslideradapter extends SliderAdapter {
    String banner1 , banner2, banner3 , banner4;

    Context mContext;


    public mainslideradapter(){


    }


    public mainslideradapter(Context context){
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        getDetailsFromDB();

        switch (position) {
            case 0:
                Picasso.get().load(banner1).into(viewHolder.imageView);
                break;
            case 1:
                Picasso.get().load(banner2).into(viewHolder.imageView);
                break;
            case 2:
                Picasso.get().load(banner3).into(viewHolder.imageView);
                break;
            case 3:
                Picasso.get().load(banner4).into(viewHolder.imageView);

        }
    }

    private void getDetailsFromDB() {


        final DatabaseReference Root;
        Root = FirebaseDatabase.getInstance().getReference();

        Root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                banner1 = dataSnapshot.child("banner images").child("banner1").child("Url").getValue().toString();
                banner2 = dataSnapshot.child("banner images").child("banner2").child("Url").getValue().toString();
                banner3 = dataSnapshot.child("banner images").child("banner3").child("Url").getValue().toString();
                banner4 = dataSnapshot.child("banner images").child("banner4").child("Url").getValue().toString();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
