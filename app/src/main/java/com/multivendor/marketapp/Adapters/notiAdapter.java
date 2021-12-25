package com.multivendor.marketapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.notiModel;
import com.multivendor.marketapp.R;
import com.multivendor.marketapp.act_quickordsee;
import com.multivendor.marketapp.myordAct;
import com.multivendor.marketapp.ordhisAct;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class notiAdapter extends RecyclerView.Adapter<notiAdapter.ViewHolder> {

    Context context;
    public List<notiModel.notiResult> notilist;

    public notiAdapter(Context context, List<notiModel.notiResult> notilist) {
        this.context = context;
        this.notilist = notilist;
    }

    @NonNull
    @Override
    public notiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noti_lay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notiAdapter.ViewHolder holder, int position) {
        holder.title.setText(notilist.get(position).getTitle());
        holder.body.setText(notilist.get(position).getBody());
        holder.date.setText(notilist.get(position).getDate());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.updateStatus();
                if (notilist.get(position).getNoti_type().equals("order")) {
                    Intent intent = null;

                    if (notilist.get(position).getStatus().equals("Delivered")) {
                        intent = new Intent(context, ordhisAct.class);

                    }
                    else {
                        intent = new Intent(context, myordAct.class);
                    }
                    intent.putExtra("orderid",notilist.get(position).getOrder_id());
                    intent.putExtra("storeid",notilist.get(position).getStore_id());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }

                else if(notilist.get(position).getNoti_type().equals("quick_order")) {
                    Intent intent = new Intent(context, act_quickordsee.class);
                    intent.putExtra("orderid",notilist.get(position).getOrder_id());
                    intent.putExtra("userid",notilist.get(position).getUser_id());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return notilist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        TextView body;
        TextView date;
        View card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.notiimg);
            title = itemView.findViewById(R.id.noti_title);
            body = itemView.findViewById(R.id.noti_body);
            date = itemView.findViewById(R.id.noti_date);
            card = itemView.findViewById(R.id.noticard);

        }

        public void updateStatus() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

            Call<notiModel.notiupdateResp> call=logregApiInterface.updatenotis(notilist.get(getAdapterPosition()).getUser_id(),notilist.get(getAdapterPosition()).getId(),notilist.get(getAdapterPosition()).getNoti_type());

            call.enqueue(new Callback<notiModel.notiupdateResp>() {
                @Override
                public void onResponse(Call<notiModel.notiupdateResp> call, Response<notiModel.notiupdateResp> response) {
                    if(!response.isSuccessful()) {
                        Log.d("errorcode",String.valueOf(response.code()));
                        return;
                    }
                }

                @Override
                public void onFailure(Call<notiModel.notiupdateResp> call, Throwable throwable) {
                    Log.d("Failure",throwable.getMessage());
                }
            });
        }
    }
}
