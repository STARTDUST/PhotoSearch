package com.example.photosearch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {
    ImageView model_iv;
    TextView model_tv_title;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        this.model_iv = itemView.findViewById(R.id.model_iv);
        this.model_tv_title = itemView.findViewById(R.id.model_tv_title);
    }
}
