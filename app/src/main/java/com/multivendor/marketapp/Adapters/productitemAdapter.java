package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class productitemAdapter extends RecyclerView.Adapter<productitemAdapter.viewHolder> {

    private Context mcontext;
    public List<productitemModel> productmodel;
    private showszAdapter adapter;
    private Boolean sizesloaded = false;
    public List<cartModel.cartqtyandsize> qtylist;
    onproductsClick listener;
    String store_id;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public productitemAdapter(Context mcontext, List<productitemModel> productmodel,
                              List<cartModel.cartqtyandsize> qtylist, String store_id) {
        this.mcontext = mcontext;
        this.productmodel = productmodel;
        this.qtylist = qtylist;
        this.store_id = store_id;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.productitem_lay, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final int radius = 20;
        final int margin = 20;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(productmodel.get(position).getItemimg())
                .resize(200, 200).transform(transformation).into(holder.itemimg);
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
            holder.quatavail.setText("Available Qty.:" + productmodel.get(position).getSizeandquats()
                    .get(0).getQty());
            holder.itemprice.setText("₹ " + productmodel.get(position)
                    .getSizeandquats().get(0).getSelling_price() + ".00");
            holder.itemmrp.setText("₹ " + productmodel.get(position)
                    .getSizeandquats().get(0).getPrice() + ".00");
            holder.selitemtxt.setText("0");
            holder.itemmrp.setPaintFlags(holder.itemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

       // adapter = new showszAdapter(mcontext, productmodel.get(position).getSizeandquats());

        LinearLayoutManager llm = new LinearLayoutManager(mcontext);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        holder.sizerec.setLayoutManager(llm);
        holder.sizerec.setAdapter(adapter);

        holder.quatdec.setVisibility(View.VISIBLE);
           holder.addlay.setVisibility(View.INVISIBLE);
           holder.addtrigger.setVisibility(View.VISIBLE);

        for (cartModel.cartqtyandsize qtydata : qtylist) {
            if (productmodel.get(position).getProduct_id().equals(qtydata.getProduct_id()) &&
                    productmodel.get(position).getSizeandquats().
                            get(adapter.getSelecteditem()).getVariation_id().equals(qtydata.getSize_id())) {
                holder.quat.setText(qtydata.getQty());
                holder.quatdec.setVisibility(View.VISIBLE);
                holder.addlay.setVisibility(View.VISIBLE);
                holder.addtrigger.setVisibility(View.INVISIBLE);

            } else {
              //    holder.quatdec.setVisibility(View.VISIBLE);
                //   holder.addlay.setVisibility(View.INVISIBLE);
               //   holder.addtrigger.setVisibility(View.VISIBLE);
            }


        }
//        adapter.setonbtnclickListener(new showszAdapter.onbtnclick() {
//            @Override
//            public void onCLICK(int pos) {
//                holder.selitemtxt.setText(String.valueOf(pos));
//                holder.quatavail.setText("Available Qty.:" + productmodel.get(position).getSizeandquats()
//                        .get(pos).getQty());
//                holder.itemprice.setText("₹ " + productmodel.get(position)
//                        .getSizeandquats().get(pos).getSelling_price() + ".00");
//
//                holder.itemmrp.setText("₹ " + productmodel.get(position)
//                        .getSizeandquats().get(pos).getPrice() + ".00");
//                holder.itemmrp.setPaintFlags(holder.itemmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                listener.onSizeClick(position, productmodel.get(position).getSizeandquats()
//                        .get(pos).getVariation_id());
//
//                for (cartModel.cartqtyandsize qtydata : qtylist) {
//                    if (qtydata.getProduct_id().equals(productmodel.get(position).getProduct_id())) {
//                        if (qtydata.getSize_id().equals(productmodel.get(position).getSizeandquats().get(pos).getVariation_id())) {
//                            holder.quat.setText(qtydata.getQty());
//                            holder.quatdec.setVisibility(View.VISIBLE);
//                            holder.addlay.setVisibility(View.VISIBLE);
//                            holder.addtrigger.setVisibility(View.INVISIBLE);
//                            return;
//                        } else {
//                            holder.quatdec.setVisibility(View.VISIBLE);
//                            holder.addlay.setVisibility(View.INVISIBLE);
//                            holder.addtrigger.setVisibility(View.VISIBLE);
//
//                        }
//                    }
//
//
//                }
//                // productmodel.get(getAdapterPosition()).getSizeandquats().get(position).getVariation_id();
//
//
//            }
//
//
//        });
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
        return productmodel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView itemimg;
        TextView itemname;
        TextView itemmrp;
        TextView itemprice;
        TextView quatavail;
        Button addtrigger;
        View addlay;
        RecyclerView sizerec;
        TextView quat;
        View quatinc;
        View quatdec;
        TextView selitemtxt;

        public viewHolder(View itemView) {
            super(itemView);
            itemimg = itemView.findViewById(R.id.productimg);
            itemname = itemView.findViewById(R.id.productname);
            itemprice = itemView.findViewById(R.id.pricetxt);
            sizerec = itemView.findViewById(R.id.sizerec);
            quatavail = itemView.findViewById(R.id.totalquat);
            addtrigger = itemView.findViewById(R.id.addbtn);
            addlay = itemView.findViewById(R.id.qtylay);
            quat = itemView.findViewById(R.id.itemquat);
            quatinc = itemView.findViewById(R.id.quatincr);
            itemmrp = itemView.findViewById(R.id.mrptxt);
            quatdec = itemView.findViewById(R.id.quatdecr);
            selitemtxt = itemView.findViewById(R.id.pricetxt2);
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
