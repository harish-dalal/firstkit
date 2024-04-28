package com.example.firstkit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstkit.Interface.itemClickListener;
import com.example.firstkit.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductname , txtproductprice , txtproductdescription ;
    public ImageView imageView;
    public itemClickListener itemClick;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtproductname = (TextView) itemView.findViewById(R.id.product_name);
        txtproductprice = (TextView) itemView.findViewById(R.id.product_price);
        ////productdescription
    }


    public void setItemOnClickListener(itemClickListener listener)
    {
        this.itemClick = listener;

    }


    @Override
    public void onClick(View view) {

        itemClick.onClick(view , getAdapterPosition() , false );

    }
}
