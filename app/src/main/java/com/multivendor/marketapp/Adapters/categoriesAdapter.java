package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.viewHolder> {

  public categoriesAdapter(Context mcontext, List<categoriesModel> catModel) {
    this.mcontext = mcontext;
    this.catModel = catModel;
  }

  private Context mcontext;
  private List<categoriesModel> catModel;
  @NonNull
  @Override
  public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(mcontext).inflate(R.layout.categories_lay,parent,false);
    return new viewHolder(view);
  }

  @Override
  public void onBindViewHolder(viewHolder holder, int position) {
    final Transformation transformation = new MaskTransformation(mcontext, R.drawable.rounded_transf);
    Picasso.get().load(catModel.get(position).getImage())
            .resize(200,200).transform(transformation).into(holder.catimg);
    holder.catname.setText(catModel.get(position).getName());

    holder.catcard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        categoriesFragment catFragment=new categoriesFragment();
        Bundle bundle =new Bundle();
        bundle.putString("selectedCategory",String.valueOf(position));
        bundle.putString("selectedCategoryname",catModel.get(position).getName());
        catFragment.setArguments(bundle);
        FragmentTransaction transaction=((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
        transaction.replace(R.id.mainfragment,catFragment);
        transaction.addToBackStack("B");
        transaction.commit();
      }
    });
  }

  @Override
  public int getItemCount() {
    return catModel.size();
  }

  public class viewHolder extends RecyclerView.ViewHolder {
    ImageView catimg;
    TextView catname;
    View catcard;

    public viewHolder(View itemView) {
      super(itemView);
      catimg=itemView.findViewById(R.id.catimg);
      catname=itemView.findViewById(R.id.catname);
      catcard=itemView.findViewById(R.id.catcard);

    }
  }
}
