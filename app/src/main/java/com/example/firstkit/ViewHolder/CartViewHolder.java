package com.example.firstkit.ViewHolder;

import com.example.firstkit.Interface.itemClickListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstkit.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView productnamecart , productpricecart , quantity;
    public ImageView productimagecart;
    public ImageButton deletefromcart;

    public itemClickListener itemClickListener;


        public CartViewHolder(View itemView) {
        super(itemView);
        productnamecart = itemView.findViewById(R.id.product_name_cart);
        productpricecart = itemView.findViewById(R.id.product_price_cart);
        quantity = itemView.findViewById(R.id.product_quantity_cart);
        productimagecart = itemView.findViewById(R.id.product_image_cart);
        deletefromcart = itemView.findViewById(R.id.delete_from_cart);
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view , getAdapterPosition() , false);
    }

    public void setItemClickListener(itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
