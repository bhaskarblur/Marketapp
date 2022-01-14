package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ordproductAdapter extends RecyclerView.Adapter<ordproductAdapter.viewHolder> {

    private Context mcontext;
    private List<newProductModel.ListProductresp> productmodel;
    private List<cartModel.productResult> cartquatModel;
    public ordproductAdapter(Context mcontext, List<newProductModel.ListProductresp> productmodel,
                             List<cartModel.productResult> cartquatModel) {
        this.mcontext = mcontext;
        this.productmodel = productmodel;
        this.cartquatModel=cartquatModel;
    }

    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.orderproditem_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder( viewHolder holder, int position) {
        if(productmodel.size()>0) {
            final int radius = 20;
            final int margin = 20;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.get().load(productmodel.get(position).getProduct_image()).
                    resize(200, 200).transform(transformation).into(holder.itemimg);
            holder.itemname.setText(productmodel.get(position).getProduct_name());
            holder.itemprice.setText("Rs " + cartquatModel.get(position).getRow_price());
            holder.itemsize.setText(String.valueOf(cartquatModel.get(position).getSize()));
            holder.itemselquat.setText(String.valueOf(cartquatModel.get(position).getQty()));
        }
    }

    
    @Override
    public int getItemCount() {
        return cartquatModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView itemimg;
        TextView itemname;
        TextView itemprice;
        TextView itemsize;
        TextView itemselquat;


        public viewHolder(View itemView) {
            super(itemView);
            itemimg=itemView.findViewById(R.id.productimg);
            itemname=itemView.findViewById(R.id.productname);
            itemprice=itemView.findViewById(R.id.pricetxt);
            itemsize=itemView.findViewById(R.id.seltsize);
            itemselquat=itemView.findViewById(R.id.seltqty);
        }
    }
}
