package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class cartproditemAdapter extends RecyclerView.Adapter<cartproditemAdapter.viewHolder> {

    private Context mcontext;
    public List<productitemModel> productmodel;
    public List<cartModel.cartqtyandsize> qtylist;
    onproductsClick listener;
    String store_id;
    private List<String> sizelist11 = new ArrayList<>();

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public cartproditemAdapter(Context mcontext, List<productitemModel> productmodel,
                               List<cartModel.cartqtyandsize> qtylist, String store_id) {
        this.mcontext = mcontext;
        this.productmodel = productmodel;
        this.qtylist = qtylist;
        this.store_id = store_id;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cartproditem_lay, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final int radius = 40;
        final int margin = 40;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.get().load(productmodel.get(position).getItemimg())
                    .resize(400, 400).transform(transformation).into(holder.itemimg);

            holder.itemimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentActivity activity = (FragmentActivity) (mcontext);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("image", productmodel.get(position).getItemimg());
                    zoom_imageDialog zoom_imageDialog1 = new zoom_imageDialog();
                    zoom_imageDialog1.setArguments(bundle);
                    zoom_imageDialog1.show(fm, "zoom_imagDialog1");
                }
            });
            holder.itemname.setText(productmodel.get(position).getItemname());

            if (productmodel.get(position).getSizeandquats() != null) {
                for (cartModel.cartqtyandsize size : qtylist) {
                    for (int i = 0; i < productmodel.get(position).getSizeandquats().size(); i++) {
                        if (productmodel.get(position).getSizeandquats().get(i).getVariation_id().equals(size.getSize_id())) {
                            if (!sizelist11.contains(size.getSize_id())) {
                                sizelist11.add(size.getSize_id());
                                holder.addtrigger.setVisibility(View.INVISIBLE);
                                holder.addlay.setVisibility(View.VISIBLE);
                                holder.quat.setText(size.getQty());
                                holder.sizebtn.setText(productmodel.get(position).getSizeandquats().get(i).getSize());
                                holder.sizebtn.setBackgroundTintList(mcontext.getResources().getColorStateList(R.color.secblue));
                                holder.quatavail.setText("Available Qty.:" + productmodel.get(position).getSizeandquats()
                                        .get(i).getQty());
                                holder.itemprice.setText("Rs " + productmodel.get(position)
                                        .getSizeandquats().get(i).getSelling_price() + ".00");
                                holder.itemmrp.setText("Rs " + productmodel.get(position)
                                        .getSizeandquats().get(i).getPrice() + ".00");
                                holder.selitemtxt.setText(String.valueOf(i));
                                holder.itemmrp.setPaintFlags(holder.itemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                return;
                            }

                        }
                    }

                }
            }


            holder.addtrigger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (store_id != null) {
                        if (store_id.equals(productmodel.get(position).getUserid())) {
                            holder.addtrigger.setVisibility(View.INVISIBLE);
                            holder.addlay.setVisibility(View.VISIBLE);
                            holder.quat.setText("1");
                            listener.onaddClick(position, Integer.parseInt(holder.selitemtxt.getText().toString()), productmodel.get(position).getProduct_id(),
                                    productmodel.get(position).getSizeandquats().get(Integer.parseInt(holder.selitemtxt.getText().toString())).getVariation_id(),
                                    productmodel.get(position).getSizeandquats()
                                            .get(Integer.parseInt(holder.selitemtxt.getText().toString())).getSize(), holder.quat.getText().toString());

                        } else {
                            holder.addtrigger.setVisibility(View.INVISIBLE);
                            holder.addlay.setVisibility(View.VISIBLE);
                            holder.quat.setText("1");
                            listener.onaddClick(position, Integer.parseInt(holder.selitemtxt.getText().toString()), productmodel.get(position).getProduct_id(),
                                    productmodel.get(position).getSizeandquats().get(Integer.parseInt(holder.selitemtxt.getText().toString())).getVariation_id(),
                                    productmodel.get(position).getSizeandquats()
                                            .get(Integer.parseInt(holder.selitemtxt.getText().toString())).getSize(), holder.quat.getText().toString());

                        }
                    } else {
                        holder.addtrigger.setVisibility(View.INVISIBLE);
                        holder.addlay.setVisibility(View.VISIBLE);
                        holder.quat.setText("1");
                        listener.onaddClick(position, Integer.parseInt(holder.selitemtxt.getText().toString()), productmodel.get(position).getProduct_id(),
                                productmodel.get(position).getSizeandquats().get(Integer.parseInt(holder.selitemtxt.getText().toString())).getVariation_id(),
                                productmodel.get(position).getSizeandquats()
                                        .get(Integer.parseInt(holder.selitemtxt.getText().toString())).getSize(), holder.quat.getText().toString());


                    }


                    //   notifyDataSetChanged();


                }
            });

    }

    public void searchList(List<productitemModel> searchedList) {
        // nbyshopsModel.clear();
        productmodel = searchedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return qtylist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView itemimg;
        TextView itemname;
        TextView itemmrp;
        TextView itemprice;
        TextView quatavail;
        Button addtrigger;
        View addlay;
        TextView quat;
        View quatinc;
        View quatdec;
        TextView sizebtn;
        TextView selitemtxt;

        public viewHolder(View itemView) {
            super(itemView);
            itemimg = itemView.findViewById(R.id.productimg);
            itemname = itemView.findViewById(R.id.productname);
            itemprice = itemView.findViewById(R.id.pricetxt);
            sizebtn = itemView.findViewById(R.id.size_btn);
            quatavail = itemView.findViewById(R.id.totalquat);
            addtrigger = itemView.findViewById(R.id.addbtn);
            addlay = itemView.findViewById(R.id.qtylay);
            quat = itemView.findViewById(R.id.itemquat);
            quatinc = itemView.findViewById(R.id.quatincr);
            itemmrp = itemView.findViewById(R.id.mrptxt);
            quatdec = itemView.findViewById(R.id.quatdecr);
            selitemtxt = itemView.findViewById(R.id.pricetxt4);
            quatinc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        if (Integer.parseInt(quat.getText().toString()) < Integer.parseInt(productmodel
                                .get(getAdapterPosition()).
                                        getSizeandquats().get(Integer.parseInt(selitemtxt.getText().toString())).getQty())) {

                            quat.setText(String.valueOf(Integer.parseInt(quat.getText().toString()) + 1));
                            listener.onplusClick(getAdapterPosition(), Integer.parseInt(selitemtxt.getText().toString()), productmodel.get(getAdapterPosition())
                                    .getProduct_id(), productmodel.get(getAdapterPosition()).getSizeandquats()
                                    .get(Integer.parseInt(selitemtxt.getText().toString())).getVariation_id(), productmodel.get(getAdapterPosition()).getSizeandquats()
                                    .get(Integer.parseInt(selitemtxt.getText().toString())).getSize(), quat.getText().toString());

                        } else {
                            Toast.makeText(mcontext, "Cannot add more than available quantity!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            quatdec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        if (Integer.parseInt(quat.getText().toString()) == 1) {
                            quat.setText(String.valueOf(0));
                            addlay.setVisibility(View.INVISIBLE);
                            addtrigger.setVisibility(View.VISIBLE);
                            listener.onminusClick(getAdapterPosition(), Integer.parseInt(selitemtxt.getText().toString()), productmodel.get(getAdapterPosition())
                                    .getProduct_id(), productmodel.get(getAdapterPosition()).getSizeandquats()
                                    .get(Integer.parseInt(selitemtxt.getText().toString())).getVariation_id(), productmodel.get(getAdapterPosition()).getSizeandquats()
                                    .get(Integer.parseInt(selitemtxt.getText().toString())).getSize(), quat.getText().toString());
                        } else {
                            quat.setText(String.valueOf(Integer.parseInt(quat.getText().toString()) - 1));
                            listener.onminusClick(getAdapterPosition(), Integer.parseInt(selitemtxt.getText().toString()), productmodel.get(getAdapterPosition())
                                    .getProduct_id(), productmodel.get(getAdapterPosition()).getSizeandquats()
                                    .get(Integer.parseInt(selitemtxt.getText().toString())).getVariation_id(), productmodel.get(getAdapterPosition()).getSizeandquats()
                                    .get(Integer.parseInt(selitemtxt.getText().toString())).getSize(), quat.getText().toString());
                        }

                    }
                }

            });
        }
    }

    public interface onproductsClick {
        void onSizeClick(int position, String size_name);

        void onaddClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty);

        void onplusClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty);

        void onminusClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty);

    }

    public void setonproductsClick(onproductsClick listener) {
        this.listener = listener;
    }
}
