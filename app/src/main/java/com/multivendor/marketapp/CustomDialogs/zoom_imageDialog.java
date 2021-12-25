package com.multivendor.marketapp.CustomDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.multivendor.marketapp.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class zoom_imageDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.fullscreenalert);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.zoom_imagedialog,null);
        builder.setView(view);


        TouchImageView image=view.findViewById(R.id.zoomimage);

        Bundle bundle=getArguments();
        String img=bundle.getString("image");
        Picasso.get().load(img).into(image);
        return builder.create();
    }
}
