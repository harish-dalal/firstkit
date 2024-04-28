package com.example.firstkit;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.firstkit.Model.Products;
import com.example.firstkit.Prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment {


    private Button addToCartButton;
    private ElegantNumberButton quantity;
    private TextView pName , pPrice , pDescription;
    private ImageView pImage;
    private String productPid = "" , productImageURL = "";


    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        productPid = getArguments().getString("pid");
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addToCartButton = (Button) getView().findViewById(R.id.addToCart);
        quantity = (ElegantNumberButton) getView().findViewById(R.id.Quantity);
        pName = (TextView) getView().findViewById(R.id.productnamedetails);
        pPrice = (TextView) getView().findViewById(R.id.productpricedetails);
        pDescription = (TextView) getView().findViewById(R.id.productdescriptiondetails);
       pImage = (ImageView) getView().findViewById(R.id.productimagedetails);

        getProductDetails(productPid);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addingItemToCart();
            }
        });



    }


    private void getProductDetails(final String productPid)
    {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products");
        productRef.child(productPid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists()){

                    Products products = dataSnapshot.getValue(Products.class);
                    pName.setText(products.getProduct_name());
                    pPrice.setText("₹ " + products.getPrice());
                    pDescription.setText(products.getDescription());
                    productImageURL = products.getImage();
                    Picasso.get().load(products.getImage()).into(pImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addingItemToCart() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd,MM,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list");

        final HashMap<String , Object> cart = new HashMap<>();
        cart.put("pid" , productPid);
        cart.put("pName" , pName.getText().toString());
        cart.put("pPrice" , pPrice.getText().toString().substring(2));
        cart.put("pImage" , productImageURL);
        cart.put("date" , currentDate.format(calendar.getTime()));
        cart.put("time" , currentTime.format(calendar.getTime()));
        cart.put("quantity" , quantity.getNumber());
        cart.put("disscount" , "");


        cartListRef.child("user view").child(Prevelant.currentOnlineUser.getPhone())
                .child("products").child(productPid).updateChildren(cart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            cartListRef.child("admin view").child(Prevelant.currentOnlineUser.getPhone())
                                    .child("products").child(productPid).updateChildren(cart);

                            Toast.makeText(getActivity(), "Product added to cart", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    }
                });
}
}
