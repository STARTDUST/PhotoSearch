package com.example.photosearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> implements Filterable {
    Context context;
    ArrayList<Model> models, filterList;
    CustomFilter filter;

    public MyAdapter(Context context, ArrayList<Model> models) {
        this.context = context;
        this.models = models;
        this.filterList = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.model_tv_title.setText(models.get(position).getTitle());


        String image = models.get(position).getImg();
        try {
            File f=new File(image);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.model_iv.setImageBitmap(getResizedBitmap(b));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Integer id = models.get(pos).getId();
                String name = models.get(pos).getTitle();
                String image = models.get(pos).getImg();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Id", id);
                intent.putExtra("iName", name);
                intent.putExtra("iImage", image);
                intent.putExtra("position", position);

                context.startActivity(intent);
                Log.wtf("adapter","name=" + name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new CustomFilter(filterList, this);
        }

        return filter;
    }

    private Bitmap getResizedBitmap(Bitmap image) {
        int maxSize = 854;
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
