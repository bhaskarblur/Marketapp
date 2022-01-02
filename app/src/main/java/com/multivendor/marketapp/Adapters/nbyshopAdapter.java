package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.R;
import com.multivendor.marketapp.categoriesFragment;
import com.multivendor.marketapp.fragmentnewProduct;
import com.multivendor.marketapp.shopsFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class nbyshopAdapter extends RecyclerView.Adapter<nbyshopAdapter.viewHolder> {

    private Context mcontext;

    public nbyshopAdapter(Context mcontext, List<newProductModel.ListProductresp> nbyshopsModel) {
        this.mcontext = mcontext;
        this.nbyshopsModel = nbyshopsModel;
    }

    private List<newProductModel.ListProductresp> nbyshopsModel= new ArrayList<>();
    private List<newProductModel.ListProductresp> originalnbyModel= new ArrayList<>();
    @NonNull
    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        originalnbyModel=nbyshopsModel;
        View view= LayoutInflater.from(mcontext).inflate(R.layout.nbyshops_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder( viewHolder holder, int position) {
        if(nbyshopsModel.get(position).getProduct_image()!=null) {
            Picasso.get().load(nbyshopsModel.get(position).getProduct_image()).fit().into(holder.shopimg);
        }
        else {
            Picasso.get().load(R.drawable.imgsample).fit().into(holder.shopimg);
        }
//        holder.shopimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FragmentActivity activity = (FragmentActivity) (mcontext);
//                FragmentManager fm = activity.getSupportFragmentManager();
//                Bundle bundle=new Bundle();
//                bundle.putString("image",nbyshopsModel.get(position).getShopimg());
//                zoom_imageDialog zoom_imageDialog1=new zoom_imageDialog();
//                zoom_imageDialog1.setArguments(bundle);
//                zoom_imageDialog1.show(fm,"zoom_imagDialog1");
//            }
//
//       });


        holder.shopname.setText(nbyshopsModel.get(position).getProduct_name());
        holder.shoptype.setText(nbyshopsModel.get(position).getProduct_description());
       // holder.shopratingtxt.setText(nbyshopsModel.get(position).getShopratings());
        holder.shoplocat.setText("Rs "+nbyshopsModel.get(position).getProduct_price());
       // holder.shopratingtxt.setText(String.valueOf(nbyshopsModel.get(position).getRating()));
        holder.shopdist.setText("Rs "+nbyshopsModel.get(position).getProduct_cut_price());
        holder.shopdist.setPaintFlags(holder.shopdist.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discount.setText(nbyshopsModel.get(position).getDiscount_rate());
        holder.shopcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentnewProduct shopsFragment=new fragmentnewProduct();
                Bundle bundle =new Bundle();
                bundle.putString("product_id",nbyshopsModel.get(position).getProduct_id());
                FragmentTransaction transaction=((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,shopsFragment);
                transaction.addToBackStack("A");
                shopsFragment.setArguments(bundle);
                transaction.commit();
            }
        });
    }

    public void searchList(List<newProductModel.ListProductresp> searchedList) {
       // nbyshopsModel.clear();
        nbyshopsModel=searchedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return nbyshopsModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView shopimg;
        TextView shopname;
        public TextView shoptype;
        RatingBar shopratingbar;
        TextView shopratingtxt;
        TextView shoplocat;
        TextView shopdist;
        TextView discount;
        View shopcard;

        public viewHolder(View itemView) {
            super(itemView);
            shopimg=itemView.findViewById(R.id.nyshopimg);
            shopname=itemView.findViewById(R.id.nbyshopname);
            shoptype=itemView.findViewById(R.id.nbyshopcat);
            shopratingbar=itemView.findViewById(R.id.nyshopratingbar);
            shopratingtxt=itemView.findViewById(R.id.nbyshopratingtxt);
            shoplocat=itemView.findViewById(R.id.nbyshoplocat);
            shopdist=itemView.findViewById(R.id.nbyshopdist);
            shopcard=itemView.findViewById(R.id.nbyshopcard);
            discount=itemView.findViewById(R.id.disctxt);

        }

    }
}
