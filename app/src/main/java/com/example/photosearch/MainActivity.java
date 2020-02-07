package com.example.photosearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);
//        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setLayoutManager(new GridLayoutManager(this, 4));

        myAdapter = new MyAdapter(this, getPlayers());
        rv.setAdapter(myAdapter);
    }

    private ArrayList<Model> getPlayers() {
        ArrayList<Model> models  = new ArrayList<>();

        Model p = new Model();
        p.setTitle("Img1");
        p.setImg(R.drawable.img1);
        models.add(p);

        p = new Model();
        p.setTitle("Img2");
        p.setImg(R.drawable.img2);
        models.add(p);

        p = new Model();
        p.setTitle("Img3");
        p.setImg(R.drawable.img3);
        models.add(p);

        p = new Model();
        p.setTitle("Img4");
        p.setImg(R.drawable.img4);
        models.add(p);

        return models;
    }
}
