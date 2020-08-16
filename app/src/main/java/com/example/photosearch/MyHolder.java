package com.example.photosearch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView model_iv;
    TextView model_tv_title;
    ItemClickListener itemClickListener;
    CardView card;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        this.model_iv = itemView.findViewById(R.id.model_iv);
        this.model_tv_title = itemView.findViewById(R.id.model_tv_title);
        this.card = itemView.findViewById(R.id.card);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());
    }

    public void setItemClickListener (ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
