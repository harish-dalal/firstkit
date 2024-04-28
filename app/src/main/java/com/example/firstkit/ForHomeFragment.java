package com.example.firstkit;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.example.firstkit.Model.Products;
import com.example.firstkit.Prevelant.Prevelant;
import com.example.firstkit.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideChangeListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForHomeFragment extends Fragment {



    private Slider slider;
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    final DatabaseReference cartitemarray = FirebaseDatabase.getInstance().getReference();



    public ForHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_home, container, false);


        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getView().findViewById(R.id.recycler_menu);

        layoutManager = new LinearLayoutManager(getActivity());
        // layoutManager = new GridLayoutManager(this  , 2);
        recyclerView.setLayoutManager(layoutManager);


        ProductRef = FirebaseDatabase.getInstance().getReference().child("products");



        Paper.init(getActivity());
        Slider.init(new picassoImageLoading(getActivity()));

        final ArrayList<String> cartItems = new ArrayList<>();





        setView();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductRef , Products.class).build();


        final FirebaseRecyclerAdapter<Products , ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter
                        <Products, ProductViewHolder>(options) {

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout , parent , false);
                        ProductViewHolder holder = new ProductViewHolder(view);

                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                    {


                        holder.txtproductname.setText(model.getProduct_name());
                        holder.txtproductprice.setText("\u20B9 " + model.getPrice());
                        //////////holder.txtproductdescription.setText(model.getdescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


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
                                        .add(R.id.fragment_container, fragment , "productdetails")
                                        .addToBackStack(null)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .commit();

                                //Intent intent = new Intent(getActivity(), productDetails.class);
                                //intent.putExtra("pid" , model.getPid());
                                //startActivity(intent);
                            }
                        });

                    }

                };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void setView() {
        slider = getView().findViewById(R.id.banner_slider1);

        slider.setAdapter(new mainslideradapter(getActivity()));
        slider.setSelectedSlide(0);
        slider.setSlideChangeListener(new OnSlideChangeListener() {
            @Override
            public void onSlideChange(int position) {
                if(position == 0){
                    slider.setAnimateIndicators(true);
                    slider.setInterval(20000);
                }
            }
        });

    }



}


