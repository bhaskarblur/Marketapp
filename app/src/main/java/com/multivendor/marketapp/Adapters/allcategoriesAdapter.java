package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.R;
import com.multivendor.marketapp.categoriesFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.MaskTransformation;

public class allcategoriesAdapter extends RecyclerView.Adapter<allcategoriesAdapter.viewHolder> {


    public allcategoriesAdapter(Context mcontext, List<categoriesModel> catModel, String selectedcategory) {
        this.mcontext = mcontext;
        this.catModel = catModel;
        this.selectedcategory = selectedcategory;
    }

    private Context mcontext;

    // interface that i want to use to perform action on click
    private oncardclicklistener listener;
    public List<categoriesModel> catModel;
    private String selectedcategory;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.categories_lay, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        if (selectedcategory == String.valueOf(position)) {
            holder.catname.setTextColor(Color.parseColor("#0881E3"));
        } else {
            holder.catname.setTextColor(Color.parseColor("#000000"));
        }
        Picasso.get().load(catModel.get(position).getImage())
                .resize(300, 300).transform(new CropCircleTransformation()).into(holder.catimg);
        holder.catname.setText(catModel.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return catModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView catimg;
        public TextView catname;
        View catcard;

        public viewHolder(View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.catimg);
            catname = itemView.findViewById(R.id.catname);
            catcard = itemView.findViewById(R.id.catcard);

            catcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        selectedcategory = String.valueOf(position);

                        //calling the function inside the interface here
                        listener.oncardclick(Integer.parseInt(selectedcategory));
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //  making an interface
    public interface oncardclicklistener {
      // a function to call when i want it
        void oncardclick(int position);
    }

    // method to set the interface or override the interface from other class.
    public void setoncardclicklistener(oncardclicklistener listener) {
      // overriding as the interface
        this.listener = listener;
    }
}
