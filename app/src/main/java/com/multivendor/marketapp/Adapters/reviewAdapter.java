package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.Viewholder> {
    public Context context;
    public List<newProductModel.reviewResult> list;

    public reviewAdapter(Context context, List<newProductModel.reviewResult> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.review_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Picasso.get().load(list.get(position).getUser_image()).transform(new CropCircleTransformation())
                .into(holder.userimage);

        if(list.get(position).getRating()!=null) {
            holder.ratingBar.setRating(Float.parseFloat(list.get(position).getRating()));
        }

        holder.username.setText(list.get(position).getUser_name());
        holder.userreview.setText(list.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView userimage;
        RatingBar ratingBar;
        TextView username;
        TextView userreview;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            userimage=itemView.findViewById(R.id.revimage);
            ratingBar=itemView.findViewById(R.id.revratebar);
            username=itemView.findViewById(R.id.revusername);
            userreview=itemView.findViewById(R.id.revdescr);

        }
    }
}
