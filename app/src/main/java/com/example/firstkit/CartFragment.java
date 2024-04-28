package com.example.firstkit;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstkit.Model.Cart;
import com.example.firstkit.Prevelant.Prevelant;
import com.example.firstkit.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private TextView Subtotal;
    private Button Buynow;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartproduct;
    DecimalFormat format = new DecimalFormat("##,##,###");

    public  int tQuantity = 0 ;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Subtotal = (TextView) getView().findViewById(R.id.subtotsl);
        Buynow = (Button) getView().findViewById(R.id.Buy_Now);
        recyclerView = (RecyclerView) getView().findViewById(R.id.cart_list);



        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        cartproduct = FirebaseDatabase.getInstance().getReference().child("cart list");



        Buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(getActivity(), Prevelant.currentOnlineUser.getAddress(), Toast.LENGTH_SHORT).show();

                // if address is present
                if(!Prevelant.currentOnlineUser.getAddress().isEmpty()){
                    Intent intent = new Intent(getActivity() , BuyNowActivity.class);
                    startActivity(intent);
                }

                else{////if no address then give a toast and then move him to userAccountFragment
                    Toast.makeText(getContext(), "Add delivery address", Toast.LENGTH_SHORT).show();
                    String checkingcurrentfragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();

                    /////////

                    if(!checkingcurrentfragment.equals("UserAccountSettingFragment")) {


                        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() >= 3){

                            if(checkingcurrentfragment.equals("ProductDetailsFragment")){
                                UserAccountSettingFragment fragment = new UserAccountSettingFragment();

                                ///goto back and start again therefore no increase in backstacks(fragments)

                                getActivity().getSupportFragmentManager().popBackStack(null ,  getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .hide(getActivity().getSupportFragmentManager().findFragmentByTag("HOME"))
                                        .add(R.id.fragment_container, fragment)
                                        .addToBackStack(null)
                                        .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .commit();

                            }}
                        else {
                            UserAccountSettingFragment fragment = new UserAccountSettingFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                                    .add(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();

                        }
                    }


                }

            }
        });





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
                holder.deletefromcart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ////////deleting Items from cart
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setMessage("Delete")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {


                                        //////setting up the Total ammouunt
                                        Subtotal.setText("Subtotal  \u20B9" + String.valueOf(format.format(totalprice())) + " ("+String.valueOf(totalquantity())+" items)");

                                        deletefromcart(model.getPid());

                                    }})

                                .setNegativeButton(android.R.string.no, null).show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(20);



                    }
                });

                //////setting up the Total ammouunt
                Subtotal.setText("Subtotal  \u20B9" + String.valueOf(format.format(totalprice())) + " ("+String.valueOf(totalquantity())+" items)");
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view)
                   {


                       ProductDetailsFragment fragment = new ProductDetailsFragment();
                       Bundle args = new Bundle();
                       args.putString("pid", model.getPid());
                       fragment.setArguments(args);


                       getFragmentManager().beginTransaction()
                               .hide(getFragmentManager().findFragmentById(R.id.fragment_container))
                               .add(R.id.fragment_container, fragment)
                               .addToBackStack(null)
                               .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                               .commit();

                   }
               });

                //cant do this here as it is called multiple times
               // Subtotal.setText("Subtotal  \u20B9" +String.valueOf(TotalPrice) + " ("+String.valueOf(tQuantity)+" items)");
            }

            private void deletefromcart(final String getPid) {
                final DatabaseReference deleteitem  = FirebaseDatabase.getInstance().getReference().child("cart list");


                deleteitem.child("user view").child(Prevelant.currentOnlineUser.getPhone())
                        .child("products").child(getPid).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    deleteitem.child("admin view").child(Prevelant.currentOnlineUser.getPhone())
                                            .child("products").child(getPid).removeValue();

                                    //////setting up the Total ammouunt again after delete of an item
                                    Subtotal.setText("Subtotal  \u20B9" + String.valueOf(format.format(totalprice())) + " ("+String.valueOf(totalquantity())+" items)");


                                }
                            }
                        });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();



        }



}
