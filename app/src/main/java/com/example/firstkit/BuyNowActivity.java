package com.example.firstkit;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.firstkit.Model.Cart;
import com.example.firstkit.Prevelant.Prevelant;
import com.example.firstkit.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class BuyNowActivity extends AppCompatActivity {


    ///////////A lot of things is copied from CartFragment just little bit modified

    private TextView Subtotal;
    private Button buyNowFinal;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DecimalFormat format = new DecimalFormat("##,##,###");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        Subtotal = (TextView) findViewById(R.id.subtotsl_final);
        recyclerView = (RecyclerView) findViewById(R.id.buy_list);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference cartproduct = FirebaseDatabase.getInstance().getReference().child("cart list");



        /////////////

        final FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartproduct.child("user view").child(Prevelant.currentOnlineUser.getPhone()).child("products") , Cart.class)
                        .build();

        final FirebaseRecyclerAdapter<Cart , CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout , parent , false);
                CartViewHolder holder = new CartViewHolder(view);


                //////setting up the Total ammouunt
                Subtotal.setText("Subtotal  \u20B9" + String.valueOf(format.format(totalprice())) + " ("+String.valueOf(totalquantity())+" items)");

                return holder;




            }



            public int totalprice(){
                int TotalPrice = 0;
                int count = getItemCount();
                for(int t=0; t<=count-1 ; t++)
                {
                    //getItem takes the position of the card and and retrieve any value from it
                    TotalPrice = TotalPrice + (Integer.valueOf(getItem(t).getpPrice())*Integer.valueOf(getItem(t).getQuantity()));
                }

                return TotalPrice;
            }

            public int totalquantity(){
                int TotalQuantity = 0;
                int count = getItemCount();
                for(int t = 0; t<=count-1; t++ )
                {
                    TotalQuantity = TotalQuantity + Integer.valueOf(getItem(t).getQuantity());
                }
                return TotalQuantity;
            }



            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {


                holder.productnamecart.setText(model.getpName());
                holder.productpricecart.setText("\u20B9"+model.getpPrice());
                holder.quantity.setText("Quantity " +model.getQuantity());
                Picasso.get().load(model.getpImage()).into(holder.productimagecart);
                holder.deletefromcart.setVisibility(View.GONE);


                //////setting up the Total ammouunt
                Subtotal.setText("Subtotal  \u20B9" + String.valueOf(format.format(totalprice())) + " ("+String.valueOf(totalquantity())+" items)");


            }


            /////////////

        };


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
