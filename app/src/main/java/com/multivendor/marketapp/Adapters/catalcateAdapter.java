package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.MaskTransformation;


public class catalcateAdapter extends RecyclerView.Adapter<catalcateAdapter.viewHolder> {

  private Context mcontext;
  public String selectedcategory="";
  private allcategoriesAdapter.oncardclicklistener listener;
  private List<categoriesModel> catModel= new ArrayList<>();

  public catalcateAdapter(Context mcontext,List<categoriesModel> catModel) {
    this.mcontext = mcontext;
    this.catModel=catModel;
  }


  @Override
  public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(mcontext).inflate(R.layout.categories_lay,parent,false);
    return new viewHolder(view);
  }

  @Override
  public void onBindViewHolder( viewHolder holder, int position) {
    if(selectedcategory==String.valueOf(position)) {
      holder.catname.setTextColor(Color.parseColor("#0881E3"));
    }
    else{
      holder.catname.setTextColor(Color.parseColor("#000000"));
    }
    Picasso.get().load(catModel.get(position).getImage())
            .resize(300,300).transform(new CropCircleTransformation()).into(holder.catimg);
    holder.catname.setText(catModel.get(position).getName());

  }

  @Override
  public int getItemCount() {
    if(catModel!=null){
      return catModel.size();
    }
    else {
      return 0;
    }
  }

  public class viewHolder extends RecyclerView.ViewHolder {
    ImageView catimg;
    public TextView catname;
    View catcard;

    public viewHolder( View itemView) {
      super(itemView);
      catimg=itemView.findViewById(R.id.catimg);
      catname=itemView.findViewById(R.id.catname);
      catcard=itemView.findViewById(R.id.catcard);

      catcard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position=getAdapterPosition();
          if(position!=RecyclerView.NO_POSITION && listener!=null) {
            selectedcategory=String.valueOf(position);
            listener.oncardclick(Integer.parseInt(selectedcategory));
            notifyDataSetChanged();
          }
        }
      });

    }
  }
  public interface oncardclicklistener{
    void oncardclick(int position);
  }
  public void setoncardclicklistener(allcategoriesAdapter.oncardclicklistener listener){
    this.listener=listener;
  }
}
