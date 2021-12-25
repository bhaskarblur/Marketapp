package com.multivendor.marketapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.Models.quickorderModel;
import com.multivendor.marketapp.R;
import com.multivendor.marketapp.act_quickordsee;

import java.util.List;

public class homequickordAdapter extends RecyclerView.Adapter<homequickordAdapter.viewHolder> {
    private Context mcontext;

    public homequickordAdapter(Context mcontext, List<quickorderModel.quickordResult> newordList) {
        this.mcontext = mcontext;
        this.newordList = newordList;
    }

    private List<quickorderModel .quickordResult> newordList;

    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.homequickord_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.buyername.setText(newordList.get(position).getCustomer_name());
        holder.buyeraddr.setText(newordList.get(position).getCustomer_address());
        if(newordList.get(position).getTotal_price()!=null) {
            holder.amount.setText("Rs " + newordList.get(position).getTotal_price());
        }
        else {
            holder.amount.setText("");
        }
        if(newordList.get(position).getProducts()!=null & newordList.get(position).getProducts()
        .size()>0) {
            holder.prodstxt.setText(newordList.get(position).getProducts().get(0).getProduct_name()+"" +
                    " and "+String.valueOf(newordList.get(position).getProducts().size()));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mcontext, act_quickordsee.class);
                intent.putExtra("orderid",newordList.get(position).getQuick_id());
                intent.putExtra("userid",newordList.get(position).getUser_id());
                mcontext.startActivity(intent);
                ((Activity)mcontext).overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newordList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView buyername;
        TextView buyeraddr;
        TextView amount;
        TextView prodstxt;
        View items;
        View card;
        public viewHolder(View itemView) {
            super(itemView);
            buyername=itemView.findViewById(R.id.buyername);
            buyeraddr=itemView.findViewById(R.id.buyeraddr);
            amount=itemView.findViewById(R.id.amount);
            items=itemView.findViewById(R.id.itemlay);
            card=itemView.findViewById(R.id.newordcard);
            prodstxt=itemView.findViewById(R.id.prodstxt);
        }
    }
}
