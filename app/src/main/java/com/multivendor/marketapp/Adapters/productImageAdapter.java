package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.Models.bannermodel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class productImageAdapter extends RecyclerView.Adapter<productImageAdapter.viewHolder> {

    private Context mcontext;
    private List<newProductModel.productImage> bannerlist;

    public productImageAdapter(Context mcontext, List<newProductModel.productImage> bannerlist) {
        this.mcontext = mcontext;
        this.bannerlist = bannerlist;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.banner_lay, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final int radius = 0;
        final int margin = 0;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(bannerlist.get(position).getImage()).transform(transformation).fit().into(holder.img);
        Log.d("img", bannerlist.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        if (bannerlist.size() > 4) {
            return 4;
        } else {
            return bannerlist.size();
        }

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bannerimg);
        }
    }
}
