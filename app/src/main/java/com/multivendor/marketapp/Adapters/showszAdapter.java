package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class showszAdapter extends RecyclerView.Adapter<showszAdapter.viewHolder> {

    public showszAdapter(Context context, List<productitemModel.sizeandquat> list) {
        this.context = context;
        this.list = list;
    }

    private Context context;
    onbtnclick listener;
    public Integer selecteditem=0;

    public Integer getSelecteditem() {
        return selecteditem;
    }

    public void setSelecteditem(Integer selecteditem) {
        this.selecteditem = selecteditem;
    }

    public List<productitemModel.sizeandquat> list=new ArrayList<>();
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.showsizeoth_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if(list!=null) {
            if(!list.get(position).getSize().toString().equals("")) {
                holder.fieldtxt.setText(list.get(position).getSize());
            }
            }

        if(selecteditem==position) {
            holder.fieldtxt.setBackgroundResource(R.drawable.fieldselbg);
        }
        else{
            holder.fieldtxt.setBackgroundResource(R.drawable.fieldnotselbg);
        }

    }

    @Override
    public int getItemCount() {
        if(list!=null) {
            return list.size();
        }
        else {
            return 0;
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        Button fieldtxt;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fieldtxt=itemView.findViewById(R.id.size_btn);
            fieldtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        listener.onCLICK(getAdapterPosition());
                        Log.d("size",String.valueOf(getItemCount()));
                        selecteditem=getAdapterPosition();
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
    public interface onbtnclick {
        void onCLICK(int position);
    }
    public void setonbtnclickListener(onbtnclick listener) {
        this.listener=listener;
    }
}
